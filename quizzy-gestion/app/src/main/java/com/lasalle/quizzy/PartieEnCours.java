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
    private int scoreJoueur1 = 0;
    private int scoreJoueur2 = 0;
    private int bonneReponseActuelle = -1;

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
                    "2C:CF:67:94:CF:DE", // écran
                    "24:6F:28:10:5A:46"  // pupitre
            );

            connexionBluetooth.setOnTrameReceivedListener((joueur, couleur) -> {
                runOnUiThread(() -> {
                    // Réagir à la réception d'une trame (ex: mise à jour UI)
                    Log.d("_PartieEnCours", "pupitre " + joueur + " bouton " + couleur);
                    Toast.makeText(this, "Joueur " + joueur + " a pressé bouton " + couleur, Toast.LENGTH_SHORT).show();
                    gestionReponseJoueur(joueur, couleur);
                });
            });

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
            connexionBluetooth.envoyerEcran("@@F\n");
            Intent activitePrincipale = new Intent(PartieEnCours.this, Quizzy.class);
            startActivity(activitePrincipale);
        }
    });

        boutonQuestionSuivante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clic boutonQuestionSuivante");
                questionSuivante();
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
                connexionBluetooth.envoyerEcran("@@F\n");
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
        bonneReponseActuelle = intent.getIntExtra("bonneReponseInitiale", -1);
        Log.d(TAG, "Bonne réponse initiale : " + bonneReponseActuelle);

        Log.d(TAG, "Bluetooth initialisé avec themeID=" + themeID + ", temps=" + tempsParQuestion + ", nbQuestions=" + nombreQuestions);
    }

    private void planifierReponseInitiale()
    {
        new Handler().postDelayed(() -> {
            connexionBluetooth.envoyerEcran("@@S\n");
            Log.d(TAG, "Révélation réponse première question");
        }, tempsParQuestion * 1000);
    }

    public void afficherScore() {
        runOnUiThread(() -> {
            if (questionsEnvoyees >= nombreQuestions) {
                Log.d(TAG, "Toutes les questions ont été envoyées");

                connexionBluetooth.envoyerEcran("@@R;" + scoreJoueur1 + ";" + scoreJoueur2 + "\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connexionBluetooth.envoyerEcran("@@S\n");
            } else {
                Toast.makeText(PartieEnCours.this, "Veuillez terminer le Quizz avant de consulter le score ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void questionSuivante()
    {
        runOnUiThread(() -> {
            if (questionsEnvoyees >= nombreQuestions) {
                Log.d(TAG, "Toutes les questions ont été envoyées");
                return;
            }

            BaseDeDonnees baseDeDonnees = BaseDeDonnees.getInstance(getApplicationContext());
            String trameQuestion = baseDeDonnees.getQuestionAleatoireParTheme(themeID);

            String[] questionDetails = trameQuestion.split(";");
            if (questionDetails.length >= 6) {
                try {
                    bonneReponseActuelle = Integer.parseInt(questionDetails[6].trim());
                    Log.d(TAG, "Nouvelle bonne réponse : " + bonneReponseActuelle);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Erreur en extrayant la bonne réponse", e);
                    bonneReponseActuelle = -1;
                }
            }

            connexionBluetooth.envoyerEcran(trameQuestion);
            Log.d(TAG, "Question envoyée : " + trameQuestion);
            questionsEnvoyees++;

            new Handler().postDelayed(() -> {
                connexionBluetooth.envoyerEcran("@@S\n");
                Log.d(TAG, "Trame suivant envoyée (affichage question)");
                connexionBluetooth.envoyerPupitre("$S\n");

                new Handler().postDelayed(() -> {
                    connexionBluetooth.envoyerEcran("@@S\n");
                    Log.d(TAG, "Trame suivant envoyée (affichage réponse)");
                }, tempsParQuestion * 1000);

            }, 1000);
        });
    }

    private void gestionReponseJoueur(int joueur, char couleur) {
        int reponseDonnee;

        switch (couleur) {
            case 'R':
                reponseDonnee = 1;
                break;
            case 'J':
                reponseDonnee = 2;
                break;
            case 'B':
                reponseDonnee = 3;
                break;
            case 'V':
                reponseDonnee = 4;
                break;
            default:
                Log.w(TAG, "Réponse invalide reçue : " + couleur);
                return;
        }
        if (reponseDonnee == bonneReponseActuelle) {
            if (joueur == 1) {
                scoreJoueur1++;
                Log.i(TAG, "Bonne réponse du Joueur 1. Score actuel : " + scoreJoueur1);
            } else if (joueur == 2) {
                scoreJoueur2++;
                Log.i(TAG, "Bonne réponse du Joueur 2. Score actuel : " + scoreJoueur2);
            } else {
                Log.w(TAG, "Numéro de joueur inconnu : " + joueur);
            }
        } else {
            Log.i(TAG, "Mauvaise réponse du Joueur " + joueur);
        }
    }
}