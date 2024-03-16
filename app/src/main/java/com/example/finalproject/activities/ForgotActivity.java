package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.GmailSender;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;
    Button btnGetPass;
    ImageButton imgLogin;
    GmailSender Gmail = new GmailSender();
    EditText edt_username, edt_email;

    TextView tvWelcomeTitle;
    Animation animation_et_left, animation_tvHomeTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        imgLogin = findViewById(R.id.imgLogin_Forgot);
        imgLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        tvWelcomeTitle = findViewById(R.id.tvWelcomeTitle);
        animation_tvHomeTitle = AnimationUtils.loadAnimation(this, R.anim.anime_tv);
        tvWelcomeTitle.setAnimation(animation_tvHomeTitle);

        edt_username = findViewById(R.id.edt_username);
        animation_et_left = AnimationUtils.loadAnimation(this,R.anim.anime_et_left);
        edt_username.setAnimation(animation_et_left);

        edt_email = findViewById(R.id.edt_email);
        animation_et_left = AnimationUtils.loadAnimation(this,R.anim.anime_et_left);
        edt_email.setAnimation(animation_et_left);

        btnGetPass = findViewById(R.id.btnGetpass);
        btnGetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPass();
                Intent intent = new Intent(ForgotActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });
    }
    public void getPass(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Habit_Tracker").child("Tai_Khoan");

        final String username = edt_username.getText().toString();
        final String gmail = edt_email.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(gmail)) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập và gmail", Toast.LENGTH_SHORT).show();
            return;
        }
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot taiKhoanSnapshot : dataSnapshot.getChildren()) {
                        String currentUsername = taiKhoanSnapshot.child("TenDangNhap").getValue(String.class);
                        String currentGmail = taiKhoanSnapshot.child("Gmail").getValue(String.class);
                        if (username.equals(currentUsername) && gmail.equals(currentGmail)) {
                            String currentPassword = taiKhoanSnapshot.child("MatKhau").getValue(String.class);
                            String senderEmail = "01215165330asd@gmail.com"; // Địa chỉ Gmail của bạn
                            String senderPassword = "uwcpdnjcsxhchtcv"; // Mật khẩu Gmail của bạn
                            String recipientEmail = gmail; // Địa chỉ Gmail của người nhận
                            String subject = "Gửi lại mật khẩu";
                            String message = "Password của bạn: " + currentPassword;
                            Gmail.sendEmail(senderEmail, senderPassword, recipientEmail, subject, message);
                            return;
                        }
                    }
                    Toast.makeText(ForgotActivity.this, "Tài khoản và  gmail không khớp với nhau", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi đọc dữ liệu từ Firebase
                Toast.makeText(ForgotActivity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}