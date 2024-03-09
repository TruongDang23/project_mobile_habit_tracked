package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {
    TextView tvWelcomeTitle,tvHaveAccount;
    Button btnSignup, btnLogin;
    EditText edtUsername, edtPassword, edtRepassword;
    Animation animation_btn_bottom, animation_et_right, animation_et_left, animation_tvHomeTitle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnLogin = findViewById(R.id.btnLogin_SignUp);
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        tvWelcomeTitle = findViewById(R.id.tvWelcomeTitle);
        animation_tvHomeTitle = AnimationUtils.loadAnimation(this, R.anim.anime_tv);
        tvWelcomeTitle.setAnimation(animation_tvHomeTitle);

        edtUsername = findViewById(R.id.edtUsername);
        animation_et_left = AnimationUtils.loadAnimation(this,R.anim.anime_et_left);
        edtUsername.setAnimation(animation_et_left);

        edtPassword = findViewById(R.id.edtPassword);
        animation_et_right = AnimationUtils.loadAnimation(this,R.anim.anime_et_right);
        edtPassword.setAnimation(animation_et_right);

        edtRepassword = findViewById(R.id.edtRepassword);
        animation_et_left = AnimationUtils.loadAnimation(this,R.anim.anime_et_left);
        edtRepassword.setAnimation(animation_et_left);

        ShowPass(edtPassword);
        ShowPass(edtRepassword);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void ShowPass(EditText pass){
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (pass.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wel_password, 0, R.drawable.wel_eyes, 0);
                        } else {
                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wel_password, 0, R.drawable.wel_eyes_close, 0);
                        }
                        return true;
                    }
                }
                return false;
            }
        });

    }
}