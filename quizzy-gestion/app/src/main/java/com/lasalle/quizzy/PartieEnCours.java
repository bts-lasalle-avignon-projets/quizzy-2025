package com.lasalle.quizzy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.os.Build;
import android.widget.Toast;

import java.sql.*;

public class PartieEnCours extends AppCompatActivity {

    private static final String TAG = "_PartieEnCours";
    private ConnexionBluetoothClient connexionBluetooth;

    private Button boutonRetourAccueil;
    private Button boutonQuestionSuivante;
    private Button boutonAfficherScore;
    private Button boutonAbandonnerPartie;

    private int themeID;
    private int tempsParQuestion;
    private int nombreQuestions;
    private int questionsEnvoyees = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie_en_cours);
        Log.d(TAG, "onCreate()");

        initialiserRessources();

        Intent intent = getIntent();
        boolean lancerTimer = intent.getBooleanExtra("lancerTimer", false);

        if (lancerTimer) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{
                                    android.Manifest.permission.BLUETOOTH_CONNECT,
                                    android.Manifest.permission.BLUETOOTH_SCAN,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                            1
                    );
                }
            }
            connexionBluetooth = new ConnexionBluetoothClient(
                    this,
                    "00:E0:4C:6D:20:A3", // écran
                    "24:6F:28:10:5A:46"  // pupitre
            );
            connexionBluetooth.start();

            initialiserBluetoothEtParams(intent);
            planifierReponseInitiale();
        }
    }

private void initialiserRessources() {
    this.boutonRetourAccueil = findViewById(R.id.boutonRetourAccueil);
    this.boutonQuestionSuivante = findViewById(R.id.boutonQuestionSuivante);
    this.boutonAfficherScore = findViewById(R.id.boutonAfficherScore);
    this.boutonAbandonnerPartie = findViewById(R.id.boutonAbandonnerPartie);

    boutonRetourAccueil.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "clic boutonRetourAccueil");
            Intent activitePrincipale = new Intent(PartieEnCours.this, Quizzy.class);
            startActivity(activitePrincipale);
        }
    });

        boutonQuestionSuivante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clic boutonQuestionSuivante");
                recevoirS();
            }
        });

        boutonAfficherScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clic boutonAfficherScore");
                afficherScore();
        }
    });
        boutonAbandonnerPartie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clic boutonAbandonnerPartie");
                connexionBluetooth.closeConnexion();
                Toast.makeText(PartieEnCours.this, "Connexion bluetooth fermée. Retour a l'accueil dans 10 secondes ", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent activitePrincipale = new Intent(PartieEnCours.this, Quizzy.class);
                startActivity(activitePrincipale);
        }
    });

    }

    private void initialiserBluetoothEtParams(Intent intent)
    {
        themeID = intent.getIntExtra("themeID", 1);
        tempsParQuestion = intent.getIntExtra("tempsParQuestion", 10);
        nombreQuestions = intent.getIntExtra("nombreQuestions", 1);
        Log.d(TAG, "Bluetooth initialisé avec themeID=" + themeID + ", temps=" + tempsParQuestion + ", nbQuestions=" + nombreQuestions);
    }

    private void planifierReponseInitiale()
    {
        new Handler().postDelayed(() -> {
            connexionBluetooth.envoyerEcran("@@S;\n");
            Log.d(TAG, "Révélation réponse première question");
        }, tempsParQuestion * 1000);
    }

    public void afficherScore()
    {
        runOnUiThread(() -> {
            if (questionsEnvoyees >= nombreQuestions) {
                Log.d(TAG, "Toutes les questions ont été envoyées");

                connexionBluetooth.envoyerEcran("@@R;17;18\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connexionBluetooth.envoyerEcran("@@S\n");
                return;
            }
            else {
                Toast.makeText(PartieEnCours.this, "Veuillez terminer le Quizz avant de consulter le score ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void recevoirS()
    {
        runOnUiThread(() -> {
            if (questionsEnvoyees >= nombreQuestions) {
                Log.d(TAG, "Toutes les questions ont été envoyées");
                return;
            }

            BaseDeDonnees baseDeDonnees = BaseDeDonnees.getInstance(getApplicationContext());
            String trameQuestion = baseDeDonnees.getQuestionAleatoireParTheme(themeID);
            connexionBluetooth.envoyerEcran(trameQuestion);

            Log.d(TAG, "Question envoyée : " + trameQuestion);
            questionsEnvoyees++;

            new Handler().postDelayed(() -> {
                connexionBluetooth.envoyerEcran("@@S\n");
                Log.d(TAG, "Trame suivant envoyée (affichage question)");

                new Handler().postDelayed(() -> {
                    connexionBluetooth.envoyerEcran("@@S\n");
                    Log.d(TAG, "Trame suivant envoyée (affichage réponse)");
                }, tempsParQuestion * 1000);

            }, 1000);
        });
    }
}