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
import java.sql.*;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.widget.TextView;

public class ParametresSession extends AppCompatActivity
{

    private static final String TAG = "_ParametresSession"; //!< TAG pour les logs (cf. Logcat)
    private ConnexionBluetoothClient connexionBluetooth;

    private Button boutonRetourAccueil;
    private Button boutonLancementCreationJoueur;
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
        String adresseMAC = "2C:CF:67:94:F2:3D";
        connexionBluetooth = new ConnexionBluetoothClient(this, adresseMAC);
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

    private void initialiserRessources()
    {
        this.boutonRetourAccueil = findViewById(R.id.boutonRetourAccueil);

        boutonRetourAccueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "clic boutonRetourAccueil");
                Intent activitePrincipale = new Intent(ParametresSession.this, Quizzy.class);
                startActivity(activitePrincipale);
            }
        });

        this.boutonLancementCreationJoueur = findViewById(R.id.boutonLancementCreationJoueur);

        boutonLancementCreationJoueur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "clic boutonLancemntCreationJoueur");
                String joueurCreer = ((EditText) findViewById(R.id.creationJoueur)).getText().toString().trim();

                if (joueurCreer.isEmpty()) {
                    Toast.makeText(ParametresSession.this, "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    BaseDeDonnees baseDeDonnees = BaseDeDonnees.getInstance(getApplicationContext());
                    SQLiteDatabase db = baseDeDonnees.getWritableDatabase();

                    ContentValues valeurs = new ContentValues();
                    valeurs.put("prenom", joueurCreer);

                    long resultat = db.insert("table_participant", null, valeurs);

                    if (resultat != -1) {
                        Toast.makeText(ParametresSession.this, "Joueur ajouté avec succès", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ParametresSession.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                    }
                    Intent activiteParametresSession = new Intent(ParametresSession.this, ParametresSession.class);
                    startActivity(activiteParametresSession);
                }
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

        Spinner spinnerTemps = findViewById(R.id.spinner_TempsQuestion);

        String[] valeurTemps = {"10", "15", "30"};
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
            String joueur1 = spinnerJoueur1.getSelectedItem().toString().trim();
            String joueur2 = spinnerJoueur2.getSelectedItem().toString().trim();

            if (theme.isEmpty() || temps.isEmpty() || nombreQuestions.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            String trame = "@@C;" + theme + ";" + temps + ";" + nombreQuestions + "\n";
            connexionBluetooth.envoyer(trame);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            trame = "@@J;" + joueur1 + ";" + joueur2 + "\n";
            connexionBluetooth.envoyer(trame);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            connexionBluetooth.envoyer("@@S;\n");

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int themeID = -1;
            ArrayList<String> tousLesThemes = baseDeDonnees.getThemes();
            for (int i = 0; i < tousLesThemes.size(); i++) {
                if (tousLesThemes.get(i).equals(theme)) {
                    themeID = i + 1; // IDs dans la BDD commencent à 1
                    break;
                }
            }

            if (themeID == -1) {
                Toast.makeText(this, "Thème introuvable", Toast.LENGTH_SHORT).show();
                return;
            }

            String trameQuestion = baseDeDonnees.getQuestionAleatoireParTheme(themeID);
            if (!trameQuestion.isEmpty()) {
                connexionBluetooth.envoyer(trameQuestion);
            } else {
                Toast.makeText(this, "Aucune question trouvée pour ce thème", Toast.LENGTH_SHORT).show();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

                    int themeID = -1;
                    ArrayList<String> tousLesThemes = baseDeDonnees.getThemes();
                    for (int i = 0; i < tousLesThemes.size(); i++) {
                        if (tousLesThemes.get(i).equals(theme)) {
                            themeID = i + 1; // IDs dans la BDD commencent à 1
                            break;
                        }
                    }

                    if (themeID == -1) {
                        Toast.makeText(this, "Thème introuvable", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String trameQuestion = baseDeDonnees.getQuestionAleatoireParTheme(themeID);
                    if (!trameQuestion.isEmpty()) {
                        connexionBluetooth.envoyer(trameQuestion);
                    } else {
                        Toast.makeText(this, "Aucune question trouvée pour ce thème", Toast.LENGTH_SHORT).show();
                    }

                    try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            trame = "@@S;" + "\n";
            connexionBluetooth.envoyer(trame);

            SharedPreferences prefs = getSharedPreferences("etat_partie", MODE_PRIVATE);
            SharedPreferences.Editor editeur = prefs.edit();
            editeur.putBoolean("partie_en_cours", true);
            editeur.apply();

            Intent activitePrincipale = new Intent(ParametresSession.this, Quizzy.class);
            activitePrincipale.putExtra("themeID", themeID);
            activitePrincipale.putExtra("tempsParQuestion", Integer.parseInt(temps));
            activitePrincipale.putExtra("nombreQuestions", Integer.parseInt(nombreQuestions));
            activitePrincipale.putExtra("lancerTimer", true);
            startActivity(activitePrincipale);
        });
    }
}
