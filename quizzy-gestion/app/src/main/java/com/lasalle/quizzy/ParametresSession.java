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
    }
}