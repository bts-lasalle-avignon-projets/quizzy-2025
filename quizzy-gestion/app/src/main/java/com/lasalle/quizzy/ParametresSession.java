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

public class ParametresSession extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ParametresSession"; //!< TAG pour les logs (cf. Logcat)

    /**
     * Ressources GUI
     */
    private Button boutonRetourAccueil;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_parametres_session);
        Log.d(TAG, "onCreate()");

        initialiserRessources();
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
    }
}