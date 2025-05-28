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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_principale);
        Log.d(TAG, "onCreate()");
        initialiserRessources();
    }

    private void initialiserRessources() {
        boutonParametrerSession = findViewById(R.id.boutonParametrerSession);
        boutonAfficherCredits = findViewById(R.id.boutonAfficherCredits);

        boutonParametrerSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clic boutonParametrerSession");
                Intent intent = new Intent(Quizzy.this, ParametresSession.class);
                startActivity(intent);
            }
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