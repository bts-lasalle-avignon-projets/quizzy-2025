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
import java.util.UUID;

public class ConnexionBluetoothClient extends Thread {
    private static final String TAG = "ConnexionBluetooth";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter bluetoothAdapter;
    private final Context context;
    private final String adresseMAC;

    private BluetoothSocket socket;
    private OutputStream outputStream;

    public ConnexionBluetoothClient(Context context, String adresseMAC) {
        this.context = context;
        this.adresseMAC = adresseMAC;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void run() {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth non supporté sur cet appareil");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Permission BLUETOOTH_CONNECT non accordée");
                return;
            }
        }

        try {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(adresseMAC);

            if (device == null) {
                Log.e(TAG, "Appareil Bluetooth introuvable pour l'adresse : " + adresseMAC);
                return;
            }

            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothAdapter.cancelDiscovery(); // Important pour éviter les ralentissements
            socket.connect();

            outputStream = socket.getOutputStream();
            Log.i(TAG, "Connexion réussie à : " + device.getName());

        } catch (IOException e) {
            Log.e(TAG, "Erreur de connexion à l'appareil : " + adresseMAC, e);
        } catch (SecurityException e) {
            Log.e(TAG, "Permission Bluetooth refusée", e);
        }
    }

    public void envoyer(String trame) {
        try {
            if (outputStream != null) {
                outputStream.write(trame.getBytes());
                outputStream.flush();
                Log.i(TAG, "Trame envoyée : " + trame);
            } else {
                Log.e(TAG, "Connexion non établie. Impossible d'envoyer la trame.");
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
