package com.lasalle.quizzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

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
        w
        BaseDeDonnees baseDeDonnees = new BaseDeDonnees(this);

        ArrayList<String> themes = baseDeDonnees.getThemes();
        Spinner spinnerTheme = findViewById(R.id.theme);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                themes

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTheme.setAdapter(adapter);
        );
    }
}