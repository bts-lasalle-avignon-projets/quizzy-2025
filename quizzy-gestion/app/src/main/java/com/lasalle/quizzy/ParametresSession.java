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
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;


public class ParametresSession extends AppCompatActivity
{
    private static final String TAG = "_ParametresSession";
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_parametres_session);
        Log.d(TAG, "onCreate()");


        connexionBluetooth = new ConnexionBluetoothClient(
                this,
                "00:E0:4C:6D:20:A3", // écran
                "24:6F:28:10:5A:46"  // pupitre
        );
        connexionBluetooth.start();


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
        boutonRetourAccueil = findViewById(R.id.boutonRetourAccueil);
        boutonLancementCreationJoueur = findViewById(R.id.boutonLancementCreationJoueur);


        boutonRetourAccueil.setOnClickListener(v -> {
            Log.d(TAG, "clic boutonRetourAccueil");
            startActivity(new Intent(this, Quizzy.class));
        });


        boutonLancementCreationJoueur.setOnClickListener(v -> {
            String joueurCreer = ((EditText) findViewById(R.id.creationJoueur)).getText().toString().trim();
            if (joueurCreer.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                return;
            }


            BaseDeDonnees base = BaseDeDonnees.getInstance(getApplicationContext());
            SQLiteDatabase db = base.getWritableDatabase();


            ContentValues valeurs = new ContentValues();
            valeurs.put("prenom", joueurCreer);
            long resultat = db.insert("table_participant", null, valeurs);


            Toast.makeText(this, resultat != -1 ?
                    "Joueur ajouté avec succès" :
                    "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();


            startActivity(new Intent(this, ParametresSession.class));
        });


        BaseDeDonnees base = BaseDeDonnees.getInstance(this);


        ArrayList<String> themes = base.getThemes();
        Spinner spinnerTheme = findViewById(R.id.spinner_theme);
        spinnerTheme.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, themes));


        ArrayList<String> participants = base.getParticipants();
        Spinner spinnerJoueur1 = findViewById(R.id.spinner_joueur1);
        Spinner spinnerJoueur2 = findViewById(R.id.spinner_joueur2);
        ArrayAdapter<String> participantAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, participants);
        spinnerJoueur1.setAdapter(participantAdapter);
        spinnerJoueur2.setAdapter(participantAdapter);


        Spinner spinnerTemps = findViewById(R.id.spinner_TempsQuestion);
        spinnerTemps.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"10", "15", "30"}));


        findViewById(R.id.lancementSession).setOnClickListener(v -> {
            String theme = spinnerTheme.getSelectedItem().toString().trim();
            String temps = spinnerTemps.getSelectedItem().toString().trim();
            String nombreQuestions = ((EditText) findViewById(R.id.nbrQuestion)).getText().toString().trim();
            String joueur1 = spinnerJoueur1.getSelectedItem().toString().trim();
            String joueur2 = spinnerJoueur2.getSelectedItem().toString().trim();


            if (theme.isEmpty() || temps.isEmpty() || nombreQuestions.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }


            connexionBluetooth.envoyerEcran("@@C;" + theme + ";" + temps + ";" + nombreQuestions + "\n");
            pause(1000);


            connexionBluetooth.envoyerEcran("@@J;" + joueur1 + ";" + joueur2 + "\n");
            pause(1000);


            connexionBluetooth.envoyerEcran("@@S;\n");
            pause(4000);


            int themeID = -1;
            ArrayList<String> tousLesThemes = base.getThemes();
            for (int i = 0; i < tousLesThemes.size(); i++) {
                if (tousLesThemes.get(i).equals(theme)) {
                    themeID = i + 1;
                    break;
                }
            }


            if (themeID == -1) {
                Toast.makeText(this, "Thème introuvable", Toast.LENGTH_SHORT).show();
                return;
            }


            String trameQuestion = base.getQuestionAleatoireParTheme(themeID);
            if (!trameQuestion.isEmpty()) {
                connexionBluetooth.envoyerEcran(trameQuestion);
                pause(500);
                connexionBluetooth.envoyerEcran("@@S;\n");
            } else {
                Toast.makeText(this, "Aucune question trouvée pour ce thème", Toast.LENGTH_SHORT).show();
            }


            Intent partie = new Intent(this, PartieEnCours.class);
            partie.putExtra("themeID", themeID);
            partie.putExtra("tempsParQuestion", Integer.parseInt(temps));
            partie.putExtra("nombreQuestions", Integer.parseInt(nombreQuestions));
            partie.putExtra("lancerTimer", true);
            startActivity(partie);
        });
    }


    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
