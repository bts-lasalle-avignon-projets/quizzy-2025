package com.lasalle.quizzy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class ConnexionBluetoothClient extends Thread {
    private static final String TAG = "ConnexionBluetooth";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter bluetoothAdapter;
    private final Context context;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    public ConnexionBluetoothClient(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void run() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Permission BLUETOOTH_CONNECT non accordée");
                    return;
                }
            }

            Set<BluetoothDevice> appareilsAppaires = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : appareilsAppaires) {
                try {
                    BluetoothSocket tentativeSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    tentativeSocket.connect();

                    socket = tentativeSocket;
                    outputStream = socket.getOutputStream();
                    Log.i(TAG, "Connecté à : " + device.getName());
                    return;

                } catch (IOException e) {
                    Log.w(TAG, "Connexion échouée avec " + device.getName() + ", on essaie le suivant...");
                }
            }

            Log.e(TAG, "Aucun appareil appairé ne correspond à l'UUID donné ou connexion échouée.");

        } catch (SecurityException e) {
            Log.e(TAG, "Permission refusée pour accéder aux appareils Bluetooth", e);
        }
    }

    public void envoyer(String trame) {
        try {
            if (outputStream != null) {
                outputStream.write(trame.getBytes());
                outputStream.flush();
                Log.i(TAG, "Trame envoyée : " + trame);
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de l'envoi", e);
        }
    }

    public void closeConnexion() {
        try {
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la fermeture", e);
        }
    }
}
