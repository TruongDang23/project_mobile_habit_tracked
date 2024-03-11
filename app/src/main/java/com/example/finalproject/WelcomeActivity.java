package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    Button btnLogin, btnSignup, btnForgotPassword;
    EditText etUsername, etPassword;
    TextView tvWelcomeTitle;
    Animation animation_btn_bottom, animation_et_right, animation_et_left, animation_tvHomeTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Set up Animation
        btnLogin = findViewById(R.id.btnLogin);
        animation_btn_bottom = AnimationUtils.loadAnimation(this, R.anim.anime_btn_bottom);
        btnLogin.setAnimation(animation_btn_bottom);

        btnSignup = findViewById(R.id.btnSignup);
        animation_btn_bottom = AnimationUtils.loadAnimation(this, R.anim.anime_btn_bottom);
        btnSignup.setAnimation(animation_btn_bottom);

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        btnForgotPassword = findViewById(R.id.btnForgot);
        btnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, ForgotActivity.class);
            startActivity(intent);
        });

        etUsername = findViewById(R.id.et_username);
        animation_et_right = AnimationUtils.loadAnimation(this, R.anim.anime_et_right);
        etUsername.setAnimation(animation_et_right);

        etPassword = findViewById(R.id.et_password);
        animation_et_left = AnimationUtils.loadAnimation(this, R.anim.anime_et_left);
        etPassword.setAnimation(animation_et_left);

        tvWelcomeTitle = findViewById(R.id.tvWelcomeTitle);
        animation_tvHomeTitle = AnimationUtils.loadAnimation(this, R.anim.anime_tv);
        tvWelcomeTitle.setAnimation(animation_tvHomeTitle);
    }
}