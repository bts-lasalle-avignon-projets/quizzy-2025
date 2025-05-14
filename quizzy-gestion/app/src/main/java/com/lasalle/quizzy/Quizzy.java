/**
 * @file Quizzy.java
 * @brief Déclaration de l'activité principale
 * @author Lenny GASSE
 */

package com.lasalle.quizzy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import java.util.ArrayList;

/**
 * @class Quizzy
 * @brief L'activité principale
 */
public class Quizzy extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_Quizzy"; //!< TAG pour les logs (cf. Logcat)

    /**
     * Ressources GUI
     */
    private Button boutonParametrerSession; //!< Le bouton permettant de créer une session
    private Button boutonAfficherCredits;   //!< Le bouton permettant d'afficher les credits

    /**
     * Attributs
     */

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_principale);
        Log.d(TAG, "onCreate()");

        initialiserPermissionsBluetooth();
        initialiserRessources();
    }

    /**
     * @brief Méthode appelée au démarrage après le onCreate() ou un restart
     * après un onStop()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    /**
     * @brief Méthode appelée après onStart() ou après onPause()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    /**
     * @brief Méthode appelée après qu'une boîte de dialogue s'est affichée (on
     * reprend sur un onResume()) ou avant onStop() (activité plus visible)
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    /**
     * @brief Méthode appelée lorsque l'activité n'est plus visible
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    /**
     * @brief Méthode appelée à la destruction de l'application (après onStop()
     * et détruite par le système Android)
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    /**
     * @brief Initialise les ressources graphiques de l'activité
     */
    private void initialiserRessources()
    {
        this.boutonParametrerSession = findViewById(R.id.boutonParametrerSession);
        this.boutonAfficherCredits   = findViewById(R.id.boutonAfficherCredits);

        boutonParametrerSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "clic boutonParametrerSession");
                Intent activiteParametresSession = new Intent(Quizzy.this, ParametresSession.class);
                startActivity(activiteParametresSession);
            }
        });
    }

    /**
     * @brief Initialise les permissions pour utiliser le Bluetooth
     */
    private void initialiserPermissionsBluetooth()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            if(ContextCompat.checkSelfPermission(this,
                                                 android.Manifest.permission.BLUETOOTH_CONNECT) !=
               PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(
                  this,
                  new String[] { android.Manifest.permission.BLUETOOTH_CONNECT,
                                 android.Manifest.permission.BLUETOOTH_SCAN,
                                 android.Manifest.permission.ACCESS_FINE_LOCATION,
                                 android.Manifest.permission.ACCESS_COARSE_LOCATION },
                  1);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed()");
        finish();
    }
}