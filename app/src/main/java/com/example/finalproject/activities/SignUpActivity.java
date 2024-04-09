package com.example.finalproject.activities;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    TextView tvWelcomeTitle,tvHaveAccount;
    Button btnSignup, btnLogin;
    EditText edtUsername, edtPassword, edtRepassword;
    Animation animation_btn_bottom, animation_et_right, animation_et_left, animation_tvHomeTitle;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> {
            SignUp();
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
    public void SignUp() {

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Habit_Tracker").child("Tai_Khoan");

        String desiredUsername = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String rePassword = edtRepassword.getText().toString();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long count = dataSnapshot.getChildrenCount() + 1;
                    for (DataSnapshot taiKhoanSnapshot : dataSnapshot.getChildren()) {
                        String currentUsername = taiKhoanSnapshot.child("username").getValue(String.class);
                        if (desiredUsername.equals(currentUsername)) {
                            Toast.makeText(SignUpActivity.this, "Tên Đăng Nhập Đã Tồn Tại", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (password.equals(rePassword)) {

                        String newId = null;
                        if(count >= 100)
                            newId = "User" + count;
                        else if(count >= 10)
                            newId = "User0" + count;
                        else if(count >= 0)
                            newId = "User00" + count;



                        Account newAccount = new Account();

                        newAccount.setUsername(desiredUsername);
                        newAccount.setPassword(password);
                        newAccount.setAvatar("https://img.freepik.com/free-psd/3d-illustration-person-with-sunglasses_23-2149436188.jpg?w=1380&t=st=1710666815~exp=1710667415~hmac=8310b40214501960fa50ce9c3e26a3e3f58e1eac65d592f5235ec1b744b92044");
                        newAccount.setName("Chưa thiết lập");
                        newAccount.setSex("Nam");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        Date ngaySinhDate = null;
                        try {
                            // Chuyển đổi chuỗi thành đối tượng Date
                            ngaySinhDate = dateFormat.parse("01-01-1900");

                            // Cắt bỏ thông tin giờ, phút và giây từ đối tượng Date
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(ngaySinhDate);
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            calendar.set(Calendar.MILLISECOND, 0);
                            ngaySinhDate = calendar.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        newAccount.setBorn("01-01-1970");
                        newAccount.setGmail("Chưa thiết lập");

                        newAccount.setPhone("Chưa thiết lập");

                        ref.child(newId).setValue(newAccount);
                        Toast.makeText(SignUpActivity.this, "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    } else {
                        // Mật khẩu và repassword không khớp
                        Toast.makeText(SignUpActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, "Không thể kết nối FireBase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}