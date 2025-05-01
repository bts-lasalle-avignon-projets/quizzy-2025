package com.lasalle.quizzy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import java.util.ArrayList;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

public class ParametresSession extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ParametresSession"; //!< TAG pour les logs (cf. Logcat)

    /**
     * Attributs
     */
    private CommunicationBluetoothClient communicationBluetoothClient = null;
    private Handler                      handler =
      null; //!< Handler permettant entre la CommunicationBluetoothClient et la GUI

    /**
     * Ressources GUI
     */
    private Button boutonRetourAccueil;
    private Button boutonlancementCreationJoueur;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_parametres_session);
        Log.d(TAG, "onCreate()");

        initialiserHandler();
        initialiserCommunicationBluetooth();
        initialiserRessources();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        // communicationBluetoothClient.deconnecterEcran();
    }

    /**
     * @brief Initialise le handler pour communiquer avec les threads de la classe
     * CommunicationBluetoothClient
     */
    private void initialiserHandler()
    {
        this.handler = new Handler(this.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message message)
            {
                // Log.d(TAG, "handleMessage() id message = " + message.what);
                // Log.d(TAG, "handleMessage() message = " + message.obj.toString());
                switch(message.what)
                {
                    case CommunicationBluetoothClient.CONNEXION_BLUETOOTH:
                        Log.d(TAG, "handleMessage() CONNEXION_BLUETOOTH");
                        afficherConnexion("Connexion écran !");
                        break;
                    case CommunicationBluetoothClient.DECONNEXION_BLUETOOTH:
                        Log.d(TAG, "handleMessage() DECONNEXION_BLUETOOTH");
                        afficherConnexion("Déconnexion écran !");
                        break;
                    case CommunicationBluetoothClient.ERREUR_BLUETOOTH:
                        Log.d(TAG, "handleMessage() ERREUR_BLUETOOTH");
                        afficherConnexion("Échec connexion écran !");
                        break;
                }
            }
        };
    }

    /**
     * @brief Initialise la communication Bluetooth
     */
    private void initialiserCommunicationBluetooth()
    {
        communicationBluetoothClient =
          CommunicationBluetoothClient.creerInstance(this, this.handler);
        communicationBluetoothClient.connecterEcran();
    }

    /**
     * @brief Initialise les ressources graphiques de l'activité
     */
    private void initialiserRessources()
    {
        this.boutonRetourAccueil = findViewById(R.id.boutonRetourAccueil);

        boutonRetourAccueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        this.boutonlancementCreationJoueur = findViewById(R.id.boutonlancementCreationJoueur);

        boutonlancementCreationJoueur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String trame = "@@S;"
                               + "\n";
                communicationBluetoothClient.envoyer(trame);
            }
        });
        BaseDeDonnees baseDeDonnees = BaseDeDonnees.getInstance(this);

        ArrayList<String> themes       = baseDeDonnees.getThemes();
        Spinner           spinnerTheme = findViewById(R.id.spinner_theme);

        ArrayAdapter<String> adapterTheme =
          new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, themes);
        adapterTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(adapterTheme);

        ArrayList<String> participants   = baseDeDonnees.getParticipants();
        Spinner           spinnerJoueur1 = findViewById(R.id.spinner_joueur1);

        ArrayAdapter<String> adapterParticipant1 =
          new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, participants);
        adapterParticipant1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJoueur1.setAdapter(adapterParticipant1);

        Spinner spinnerJoueur2 = findViewById(R.id.spinner_joueur2);

        ArrayAdapter<String> adapterParticipant2 =
          new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, participants);
        adapterParticipant2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJoueur2.setAdapter(adapterParticipant2);

        Spinner spinnerTemps = findViewById(R.id.spinner_tempsQuestion);

        String[] valeurTemps = { "10", "15", "20", "30", "60" };
        ArrayAdapter<String> adapterTemps =
          new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valeurTemps);
        adapterTemps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemps.setAdapter(adapterTemps);

        Button boutonLancerSession = findViewById(R.id.lancementSession);

        boutonLancerSession.setOnClickListener(v -> {
            String theme =
              ((Spinner)findViewById(R.id.spinner_theme)).getSelectedItem().toString().trim();
            String temps = spinnerTemps.getSelectedItem().toString().trim();
            String nombreQuestions =
              ((EditText)findViewById(R.id.nbrQuestion)).getText().toString().trim();

            if(theme.isEmpty() || temps.isEmpty() || nombreQuestions.isEmpty())
            {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            String trame = "@@C;" + theme + ";" + temps + ";" + nombreQuestions + "##";
            communicationBluetoothClient.envoyer(trame);
        });
    }

    private void afficherConnexion(String message)
    {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}