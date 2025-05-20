package com.lasalle.quizzy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Quizzy extends AppCompatActivity
{
    private static final String TAG = "_Quizzy";

    private Button boutonParametrerSession;
    private Button boutonAfficherCredits;
    private ConnexionBluetoothClient connexionBluetooth;
    private Button boutonQuestionSuivante;

    private int themeID;
    private int tempsParQuestion;
    private int nombreQuestions;
    private int questionsEnvoyees = 1; // première déjà envoyée depuis ParametresSession

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
        String adresseMAC = "00:E0:4C:6D:20:A3";
        connexionBluetooth = new ConnexionBluetoothClient(this, adresseMAC);
        connexionBluetooth.start();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_principale);
        Log.d(TAG, "onCreate()");

        initialiserRessources();
        afficherEtatPartie();

        Intent intent = getIntent();
        boolean lancerTimer = intent.getBooleanExtra("lancerTimer", false);

        if (lancerTimer) {
            initialiserBluetoothEtParams(intent);
            planifierReponseInitiale();
        }
    }

    private void initialiserRessources() {
        boutonParametrerSession = findViewById(R.id.boutonParametrerSession);
        boutonAfficherCredits = findViewById(R.id.boutonAfficherCredits);
        boutonQuestionSuivante = findViewById(R.id.BoutonQuestionSuivante);

        boutonParametrerSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clic boutonParametrerSession");
                Intent intent = new Intent(Quizzy.this, ParametresSession.class);
                startActivity(intent);
            }
        });

        boutonQuestionSuivante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clic boutonQuestionSuivante");
                recevoirS();
            }
        });
    }

    private void afficherEtatPartie()
    {
        TextView texteEtatPartie = findViewById(R.id.texteEtatPartie);
        SharedPreferences prefs = getSharedPreferences("etat_partie", MODE_PRIVATE);
        boolean partieEnCours = prefs.getBoolean("partie_en_cours", false);
        texteEtatPartie.setText(partieEnCours ? "Partie en cours" : "Pas de partie en cours");
    }

    private void initialiserBluetoothEtParams(Intent intent)
    {
        themeID = intent.getIntExtra("themeID", 1);
        tempsParQuestion = intent.getIntExtra("tempsParQuestion", 10);
        nombreQuestions = intent.getIntExtra("nombreQuestions", 1);

        connexionBluetooth = new ConnexionBluetoothClient(this, "00:E0:4C:6D:20:A3");
        connexionBluetooth.start();
        Log.d(TAG, "Bluetooth initialisé avec themeID=" + themeID + ", temps=" + tempsParQuestion + ", nbQuestions=" + nombreQuestions);
    }

    private void planifierReponseInitiale()
    {
        new Handler().postDelayed(() -> {
            connexionBluetooth.envoyer("@@S;\n");
            Log.d(TAG, "Révélation réponse première question");
        }, tempsParQuestion * 1000);
    }

    public void recevoirS()
    {
        runOnUiThread(() -> {
            if (questionsEnvoyees >= nombreQuestions) {
                Log.d(TAG, "Toutes les questions ont été envoyées");

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connexionBluetooth.envoyer("@@S;\n");
                return;
            }

            BaseDeDonnees baseDeDonnees = BaseDeDonnees.getInstance(getApplicationContext());
            String trameQuestion = baseDeDonnees.getQuestionAleatoireParTheme(themeID);
            connexionBluetooth.envoyer(trameQuestion);

            Log.d(TAG, "Question envoyée : " + trameQuestion);
            questionsEnvoyees++;

            new Handler().postDelayed(() -> {
                connexionBluetooth.envoyer("@@S\n");
                Log.d(TAG, "Trame suivant envoyée (affichage question)");

                new Handler().postDelayed(() -> {
                    connexionBluetooth.envoyer("@@S\n");
                    Log.d(TAG, "Trame suivant envoyée (affichage réponse)");
                }, tempsParQuestion * 1000);

            }, 1000);
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed()");
        finish();
    }
    @Override protected void onStart()
    {
        super.onStart(); Log.d(TAG, "onStart()");
    }
    @Override protected void onResume()
    {
        super.onResume(); Log.d(TAG, "onResume()");
    }
    @Override protected void onPause()
    {
        super.onPause(); Log.d(TAG, "onPause()");
    }
    @Override protected void onStop()
    {
        super.onStop(); Log.d(TAG, "onStop()");
    }
    @Override protected void onDestroy()
    {
        super.onDestroy(); Log.d(TAG, "onDestroy()");
    }
}
