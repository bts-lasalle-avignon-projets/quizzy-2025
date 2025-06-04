package com.lasalle.quizzy;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


public class ConnexionBluetoothClient extends Thread
{
    private static final String TAG = "_ConnexionBluetooth";
    private static final UUID UUID_SERIE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private final BluetoothAdapter bluetoothAdapter;
    private final BluetoothDevice appareilEcran;
    private final BluetoothDevice appareilPupitre;


    private BluetoothSocket socketEcran;
    private BluetoothSocket socketPupitre;


    private OutputStream outputStreamEcran;
    private OutputStream outputStreamPupitre;


    private final Context context;
    private final String adresseMAC_Ecran;
    private final String adresseMAC_Pupitre;


    public ConnexionBluetoothClient(Context context, String adresseMAC_Ecran, String adresseMAC_Pupitre)
    {
        this.context = context;
        this.adresseMAC_Ecran = adresseMAC_Ecran;
        this.adresseMAC_Pupitre = adresseMAC_Pupitre;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        appareilEcran = bluetoothAdapter.getRemoteDevice(adresseMAC_Ecran);
        appareilPupitre = bluetoothAdapter.getRemoteDevice(adresseMAC_Pupitre);
    }


    @Override
    public void run() {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Permission BLUETOOTH_CONNECT non accordée");
                return;
            }


            socketEcran = appareilEcran.createRfcommSocketToServiceRecord(UUID_SERIE);
            socketPupitre = appareilPupitre.createRfcommSocketToServiceRecord(UUID_SERIE);


            socketEcran.connect();
            Log.i(TAG, "Connexion à l'écran réussie");


            socketPupitre.connect();
            Log.i(TAG, "Connexion au pupitre réussie");


            outputStreamEcran = socketEcran.getOutputStream();
            outputStreamPupitre = socketPupitre.getOutputStream();


        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la connexion Bluetooth", e);
        } catch (SecurityException e) {
            Log.e(TAG, "Erreur de permission Bluetooth", e);
        }
    }


    public void envoyerEcran(String trame) {
        if (outputStreamEcran != null) {
            try {
                outputStreamEcran.write(trame.getBytes());
                outputStreamEcran.flush();
                Log.i(TAG, "Trame envoyée à l'écran : " + trame);
            } catch (IOException e) {
                Log.e(TAG, "Erreur lors de l'envoi à l'écran", e);
            }
        } else {
            Log.e(TAG, "Pas de connexion à l'écran");
        }
    }

    public void envoyerPupitre(String trame) {
        if (outputStreamPupitre != null) {
            try {
                outputStreamPupitre.write(trame.getBytes());
                outputStreamPupitre.flush();
                Log.i(TAG, "Trame envoyée au pupitre : " + trame);
            } catch (IOException e) {
                Log.e(TAG, "Erreur lors de l'envoi au pupitre", e);
            }
        } else {
            Log.e(TAG, "Pas de connexion au pupitre");
        }
    }


    public void closeConnexion()
    {
        try {
            if (outputStreamEcran != null) outputStreamEcran.close();
            if (socketEcran != null) socketEcran.close();


            if (outputStreamPupitre != null) outputStreamPupitre.close();
            if (socketPupitre != null) socketPupitre.close();


            Log.d(TAG, "Connexions Bluetooth fermées");
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la fermeture de la connexion", e);
        }
    }
}
