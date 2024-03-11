package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Button btnWelcome, btnHomeMain, btnProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWelcome = findViewById(R.id.btnWelcome);
        btnWelcome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        btnHomeMain = findViewById(R.id.btnHomeMain);
        btnHomeMain.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Home_Activity.class);
            startActivity(intent);
        });

        btnProgress=findViewById(R.id.button2);
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_progress_total);
            }
        });
    }
}