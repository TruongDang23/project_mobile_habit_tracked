package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnWelcome, btnHomeMain, btnSettings, btnCreateHabit, btnProgress, btnSong;

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

        btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity(intent);
        });

        btnCreateHabit = findViewById(R.id.btnCreateHabit);
        btnCreateHabit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Create_habit.class);
            startActivity(intent);
        });

        btnProgress = findViewById(R.id.btnProgress);
        btnProgress.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
            startActivity(intent);
        });

        btnSong = findViewById(R.id.btnSong);
        btnSong.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SongsActivity.class);
            startActivity(intent);
        });
    }
}