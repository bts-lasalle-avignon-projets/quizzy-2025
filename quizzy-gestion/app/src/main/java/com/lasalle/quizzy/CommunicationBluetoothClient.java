package com.lasalle.quizzy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class CommunicationBluetoothClient
{
    private static final String TAG     = "_CommunicationBluetooth";
    private static final UUID   MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String PREFIXE_NOM_APPAREIL = "quizzy"; // pour la recherche
    private static final String NOM_ECRAN            = "sedatech";
    public final static int     CONNEXION_BLUETOOTH  = 0;
    // public final static int RECEPTION_BLUETOOTH   = 1;
    public final static int DECONNEXION_BLUETOOTH = 2;
    public final static int ERREUR_BLUETOOTH      = 3;
    private final Context   context;

    private static CommunicationBluetoothClient communicationBluetoothClient =
      null; //!< unique instance de la classe (singleton)
    // public static Map<String, Boolean> ecrans; // @todo si plusieurs écrans
    private BluetoothAdapter interfaceBluetooth;
    private BluetoothDevice  appareilEcran = null;
    private Thread           threadReception;
    private Handler          handler         = null;
    private BluetoothSocket  socketBluetooth = null;
    // private InputStream                inputStream         = null;
    private OutputStream outputStream = null;

    public synchronized static CommunicationBluetoothClient creerInstance(Context context,
                                                                          Handler handler)
    {
        if(communicationBluetoothClient == null)
            communicationBluetoothClient = new CommunicationBluetoothClient(context, handler);
        return communicationBluetoothClient;
    }

    public CommunicationBluetoothClient(Context context, Handler handler)
    {
        this.context = context;
        this.handler = handler;
        activer();
    }

    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }

    public void activer()
    {
        this.interfaceBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(this.interfaceBluetooth == null)
        {
            Log.e(TAG, "Bluetooth non supporté par cet appareil");
        }
        else if(!this.interfaceBluetooth.isEnabled())
        {
            Log.d(TAG, "Activation de l'interface Bluetooth");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                if(ContextCompat.checkSelfPermission(
                     context,
                     android.Manifest.permission.BLUETOOTH_CONNECT) !=
                   PackageManager.PERMISSION_GRANTED)
                {
                    Log.e(TAG, "Permission BLUETOOTH_CONNECT non accordée");
                    return;
                }
            }
            this.interfaceBluetooth.enable();
        }
        else
        {
            Log.d(TAG, "Bluetooth activé");
        }
    }

    public void rechercherEcran()
    {
        if(this.interfaceBluetooth.isEnabled())
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            {
                if(ContextCompat.checkSelfPermission(
                     context,
                     android.Manifest.permission.BLUETOOTH_CONNECT) !=
                   PackageManager.PERMISSION_GRANTED)
                {
                    Log.e(TAG, "Permission BLUETOOTH_CONNECT non accordée");
                    return;
                }
            }
            this.interfaceBluetooth.startDiscovery();
            Set<BluetoothDevice> peripheriquesAppaires = this.interfaceBluetooth.getBondedDevices();
            if(!peripheriquesAppaires.isEmpty())
            {
                for(BluetoothDevice appareil: peripheriquesAppaires)
                {
                    if(appareil.getName().equals(CommunicationBluetoothClient.NOM_ECRAN))
                    {
                        this.appareilEcran = appareil;
                        Log.d(TAG, "rechercherEcran() ecran = " + appareil.getName());
                        break;
                    }
                }
            }
            else
            {
                Log.d(TAG, "Aucun appareil Bluetooth appairé !");
            }
        }
        else
        {
            Log.d(TAG, "Le bluetooth est désactivé !");
        }
    }

    public boolean connecterEcran()
    {
        rechercherEcran();

        if(this.appareilEcran == null)
        {
            Log.d(TAG, "connecterEcran() Ecran non trouvé !");
            return false;
        }
        else
        {
            Log.d(TAG, "connecterEcran()");
            creerCommunication();
            return true;
        }
    }

    public void deconnecterEcran()
    {
        if(this.appareilEcran == null)
        {
            Log.d(TAG, "deconnecterEcran() Ecran non trouvé !");
            return;
        }
        else
        {
            Log.d(TAG, "deconnecterEcran()");
            try
            {
                /*if (inputStream != null) {
                    inputStream.close();
                }*/
                if(outputStream != null)
                {
                    outputStream.close();
                }
                if(socketBluetooth != null)
                {
                    socketBluetooth.close();
                }
                if(handler != null)
                {
                    Message messageHandler = new Message();
                    messageHandler.what    = DECONNEXION_BLUETOOTH;
                    handler.sendMessage(messageHandler);
                }
            }
            catch(Exception e)
            {
                Log.e(TAG, "Erreur lors de la fermeture des connexions !");
            }
        }
    }

    public void creerCommunication()
    {
        new Thread() {
            @Override
            public void run()
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    if(ContextCompat.checkSelfPermission(
                         context,
                         android.Manifest.permission.BLUETOOTH_CONNECT) !=
                       PackageManager.PERMISSION_GRANTED)
                    {
                        Log.e(TAG, "Permission BLUETOOTH_CONNECT non accordée");
                        return;
                    }
                }
                Log.d(TAG,
                      "creerCommunication() Ecran trouvé : " + appareilEcran.getName() + " " +
                        appareilEcran.getAddress());
                // créer la socket ?
                if(socketBluetooth == null)
                {
                    try
                    {
                        socketBluetooth = appareilEcran.createRfcommSocketToServiceRecord(
                          CommunicationBluetoothClient.MY_UUID);
                    }
                    catch(IOException e)
                    {
                        Log.e(TAG, "Erreur lors de la creation de la socket !");
                    }
                }
                // connecter la socket ?
                if(!socketBluetooth.isConnected())
                {
                    try
                    {
                        socketBluetooth.connect();
                        // inputStream  = socketBluetooth.getInputStream();
                        outputStream = socketBluetooth.getOutputStream();
                        if(handler != null)
                        {
                            Message messageHandler = new Message();
                            messageHandler.what    = CONNEXION_BLUETOOTH;
                            messageHandler.obj     = appareilEcran.getName();
                            handler.sendMessage(messageHandler);
                        }
                        Log.d(TAG,
                              "creerCommunication() Ecran connecté : " + appareilEcran.getName() +
                                " " + appareilEcran.getAddress());
                    }
                    catch(IOException e)
                    {
                        Log.e(TAG, "Erreur lors de la connexion !");
                        if(handler != null)
                        {
                            Message messageHandler = new Message();
                            messageHandler.what    = ERREUR_BLUETOOTH;
                            messageHandler.obj     = appareilEcran.getName();
                            handler.sendMessage(messageHandler);
                            try
                            {
                                socketBluetooth.close();
                            }
                            catch(IOException closeException)
                            {
                                Log.e(TAG, "Erreur lors de la fermeture du socket !");
                            }
                        }
                    }
                }
                else
                {
                    Log.d(TAG,
                          "creerCommunication() Ecran déjà connecté : " + appareilEcran.getName() +
                            " " + appareilEcran.getAddress());
                }
            }
        }.start();
    }

    public void envoyer(String message)
    {
        if(socketBluetooth == null)
            return;

        new Thread() {
            @Override
            public void run()
            {
                try
                {
                    if(!socketBluetooth.isConnected())
                    {
                        Log.d(TAG, "envoyer() socket non connectée !");
                    }
                    else
                    {
                        Log.d(TAG, "envoyer() message : " + message);
                        outputStream.write(message.getBytes());
                        outputStream.flush();
                    }
                }
                catch(IOException e)
                {
                    Log.e(TAG, "Erreur lors de l'envoi du message !");
                }
            }
        }.start();
    }
}
