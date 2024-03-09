package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotActivity extends AppCompatActivity {
    Button btnGetpass;
    EditText edt_username, edt_email, edt_yourpassword;
    TextView tvWelcomeTitle;
    Animation animation_et_right, animation_et_left, animation_tvHomeTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        tvWelcomeTitle = findViewById(R.id.tvWelcomeTitle);
        animation_tvHomeTitle = AnimationUtils.loadAnimation(this, R.anim.anime_tv);
        tvWelcomeTitle.setAnimation(animation_tvHomeTitle);

        edt_username = findViewById(R.id.edt_username);
        animation_et_left = AnimationUtils.loadAnimation(this,R.anim.anime_et_left);
        edt_username.setAnimation(animation_et_left);

        edt_email = findViewById(R.id.edt_email);
        animation_et_left = AnimationUtils.loadAnimation(this,R.anim.anime_et_left);
        edt_email.setAnimation(animation_et_left);

        edt_yourpassword = findViewById(R.id.edtYourpassword);
        animation_et_right = AnimationUtils.loadAnimation(this,R.anim.anime_et_right);
        edt_yourpassword.setAnimation(animation_et_right);
    }
}