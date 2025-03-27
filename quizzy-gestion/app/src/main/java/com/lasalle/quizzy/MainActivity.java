package com.lasalle.quizzy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    
    private Button creer;
    private Button gameHistory;
    private Button credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.creer = findViewById(R.id.create);
        this.gameHistory = findViewById(R.id.history);
        this.credit = findViewById(R.id.credits);

        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityParameters = new Intent(getApplicationContext(), ParametersActivity.class);
                startActivity(activityParameters);
                finish();

            }
        });

        gameHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityHistory = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(activityHistory);
                finish();
            }
        });

        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}