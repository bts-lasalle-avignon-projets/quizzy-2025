package com.lasalle.quizzy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class ConnexionBluetoothClient extends Thread {
    private static final String TAG = "_ConnexionBluetooth";
    private static final UUID UUID_SERIE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothDevice appareilEcran;
    private final BluetoothDevice appareilPupitre;

    private BluetoothSocket socketEcran;
    private BluetoothSocket socketPupitre;

    private OutputStream outputStreamEcran;
    private OutputStream outputStreamPupitre;
    private InputStream inputStreamPupitre;

    private final Context context;
    private final String adresseMAC_Ecran;
    private final String adresseMAC_Pupitre;

    private volatile boolean isRunning = false;

    // Définition de l'interface pour les trames reçues
    public interface OnTrameReceivedListener {
        void onTrameReceived(int joueur, char couleur);
    }
    private OnTrameReceivedListener listener;

    // Constructeur de la connexion Bluetooth
    public ConnexionBluetoothClient(Context context, String adresseMAC_Ecran, String adresseMAC_Pupitre) {
        this.context = context;
        this.adresseMAC_Ecran = adresseMAC_Ecran;
        this.adresseMAC_Pupitre = adresseMAC_Pupitre;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        appareilEcran = bluetoothAdapter.getRemoteDevice(adresseMAC_Ecran);
        appareilPupitre = bluetoothAdapter.getRemoteDevice(adresseMAC_Pupitre);
    }

    // Méthode pour enregistrer un listener pour les trames reçues
    public void setOnTrameReceivedListener(OnTrameReceivedListener listener) {
        this.listener = listener;
    }

    // Lancement du thread pour gérer la connexion Bluetooth
    @Override
    public void run() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission BLUETOOTH_CONNECT non accordée");
            return;
        }

        // Connexion à l'écran
        try {
            socketEcran = appareilEcran.createRfcommSocketToServiceRecord(UUID_SERIE);
            socketEcran.connect();
            outputStreamEcran = socketEcran.getOutputStream();
            Log.i(TAG, "Connexion à l'écran réussie");
        } catch (IOException e) {
            Log.e(TAG, "Erreur connexion écran", e);
        }

        // Connexion au pupitre
        try {
            socketPupitre = appareilPupitre.createRfcommSocketToServiceRecord(UUID_SERIE);
            socketPupitre.connect();
            outputStreamPupitre = socketPupitre.getOutputStream();
            inputStreamPupitre = socketPupitre.getInputStream();
            Log.i(TAG, "Connexion au pupitre réussie");
        } catch (IOException e) {
            Log.e(TAG, "Erreur connexion pupitre", e);
        }

        // Si la connexion au pupitre est réussie, on commence à lire les trames
        if (inputStreamPupitre != null) {
            isRunning = true;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStreamPupitre));
            try {
                while (isRunning) {
                    String trame = reader.readLine();  // Attend une trame complète terminée par \n
                    if (trame != null) {
                        analyserTrame(trame.trim());  // Analyser et traiter la trame
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Erreur lecture trame", e);
            }
        }
    }

    private void analyserTrame(String trame) {
        if (!trame.startsWith("$A;")) {
            Log.w(TAG, "Trame ignorée (mauvais format): " + trame);
            return;
        }

        String[] parts = trame.split(";");
        if (parts.length != 3) {
            Log.w(TAG, "Trame ignorée (nombre de segments incorrect): " + trame);
            return;
        }

        try {
            int joueur = Integer.parseInt(parts[1]);
            char couleur = parts[2].charAt(0);

            Log.i(TAG, "Trame reçue : joueur=" + joueur + ", couleur=" + couleur);

            if (listener != null) {
                listener.onTrameReceived(joueur, couleur);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            Log.e(TAG, "Erreur d'analyse trame : " + trame, e);
        }
    }

    public void envoyerEcran(String trame) {
        try {
            if (socketEcran != null && socketEcran.isConnected() && outputStreamEcran != null) {
                outputStreamEcran.write(trame.getBytes());
                outputStreamEcran.flush();
                Log.i(TAG, "Trame envoyée à l'écran : " + trame);
            } else {
                Log.e(TAG, "Pas de connexion active à l'écran");
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de l'envoi à l'écran", e);
        }
    }

    public void envoyerPupitre(String trame) {
        try {
            if (socketPupitre != null && socketPupitre.isConnected() && outputStreamPupitre != null) {
                outputStreamPupitre.write(trame.getBytes());
                outputStreamPupitre.flush();
                Log.i(TAG, "Trame envoyée au pupitre : " + trame);
            } else {
                Log.e(TAG, "Pas de connexion active au pupitre");
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de l'envoi au pupitre", e);
        }
    }

    public void closeConnexion() {
        isRunning = false;

        try {
            if (outputStreamEcran != null) outputStreamEcran.close();
            if (socketEcran != null) socketEcran.close();
            outputStreamEcran = null;
            socketEcran = null;

            if (outputStreamPupitre != null) outputStreamPupitre.close();
            if (inputStreamPupitre != null) inputStreamPupitre.close();
            if (socketPupitre != null) socketPupitre.close();

            outputStreamPupitre = null;
            inputStreamPupitre = null;
            socketPupitre = null;

            Log.d(TAG, "Connexions Bluetooth fermées");
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la fermeture de la connexion", e);
        }
    }
}
