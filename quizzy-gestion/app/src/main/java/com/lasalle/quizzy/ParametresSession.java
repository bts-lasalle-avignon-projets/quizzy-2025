package com.lasalle.quizzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
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


public class ParametresSession extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ParametresSession"; //!< TAG pour les logs (cf. Logcat)
    private ConnexionBluetoothClient connexionBluetooth;

    /**
     * Ressources GUI
     */
    private Button boutonRetourAccueil;
    private Button boutonlancementCreationJoueur;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                android.Manifest.permission.BLUETOOTH_CONNECT,
                                android.Manifest.permission.BLUETOOTH_SCAN,
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        1
                );
            }
        }
        connexionBluetooth = new ConnexionBluetoothClient(this);
        connexionBluetooth.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_parametres_session);
        Log.d(TAG, "onCreate()");

        initialiserRessources();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (connexionBluetooth != null) {
            connexionBluetooth.closeConnexion();
        }
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
                String trame = "@@S;" + "\n";
                connexionBluetooth.envoyer(trame);
            }
        });
        BaseDeDonnees baseDeDonnees = BaseDeDonnees.getInstance(this);

        ArrayList<String> themes = baseDeDonnees.getThemes();
        Spinner spinnerTheme = findViewById(R.id.spinner_theme);

        ArrayAdapter<String> adapterTheme = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                themes
        );
        adapterTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(adapterTheme);


        ArrayList<String> participants = baseDeDonnees.getParticipants();
        Spinner spinnerJoueur1 = findViewById(R.id.spinner_joueur1);

        ArrayAdapter<String> adapterParticipant1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                participants
        );
        adapterParticipant1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJoueur1.setAdapter(adapterParticipant1);


        Spinner spinnerJoueur2 = findViewById(R.id.spinner_joueur2);

        ArrayAdapter<String> adapterParticipant2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                participants
        );
        adapterParticipant2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJoueur2.setAdapter(adapterParticipant2);


        Spinner spinnerTemps = findViewById(R.id.spinner_tempsQuestion);

        String[] valeurTemps = {"10", "15", "20", "30", "60"};
        ArrayAdapter<String> adapterTemps = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                valeurTemps
        );
        adapterTemps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemps.setAdapter(adapterTemps);


        Button boutonLancerSession = findViewById(R.id.lancementSession);

        boutonLancerSession.setOnClickListener(v -> {
            String theme = ((Spinner) findViewById(R.id.spinner_theme)).getSelectedItem().toString().trim();
            String temps = spinnerTemps.getSelectedItem().toString().trim();
            String nombreQuestions = ((EditText) findViewById(R.id.nbrQuestion)).getText().toString().trim();

            if (theme.isEmpty() || temps.isEmpty() || nombreQuestions.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            String trame = "@@C;" + theme + ";" + temps + ";" + nombreQuestions + "##";

            connexionBluetooth.envoyer(trame);
            Toast.makeText(this, "Trame envoyée : " + trame, Toast.LENGTH_SHORT).show();
        });
    }
}