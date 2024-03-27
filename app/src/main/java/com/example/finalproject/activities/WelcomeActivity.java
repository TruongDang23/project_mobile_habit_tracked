package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityMainBinding;
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

public class WelcomeActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String idTaiKhoan;
    ActivityMainBinding binding;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    private String avatarTaiKhoan;

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionFirebase();
                loginUser();
            }
        });
    }
    private void connectionFirebase(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Habit_Tracker").child("Tai_Khoan");
    }
    private void loginUser() {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot taiKhoanSnapshot : dataSnapshot.getChildren()) {
                        String currentUsername = taiKhoanSnapshot.child("username").getValue(String.class);
                        String currentPassword = taiKhoanSnapshot.child("password").getValue(String.class);
                        if (username.equals(currentUsername) && password.equals(currentPassword)) {
                            idTaiKhoan = (String) taiKhoanSnapshot.getKey();
                            // Tên đăng nhập và mật khẩu đúng
                            String avatar = taiKhoanSnapshot.child("avatar").getValue(String.class);
                            String sex = taiKhoanSnapshot.child("sex").getValue(String.class);
                            String gmail = taiKhoanSnapshot.child("gmail").getValue(String.class);
                            String name = taiKhoanSnapshot.child("name").getValue(String.class);
                            Date bornDate = null;
                           /* String bornStr = taiKhoanSnapshot.child("born").getValue(String.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                            try {
                                bornDate = dateFormat.parse(bornStr);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            */

                            // Lấy đối tượng born từ taiKhoanSnapshot
                            DataSnapshot bornSnapshot = taiKhoanSnapshot.child("born");

                            // Kiểm tra xem born có tồn tại hay không
                            if (bornSnapshot.exists()) {
                                // Lấy các giá trị con của born: date, month, year
                                int date = 0;
                                int hours = 0;
                                int minutes = 0;
                                int month = 0;
                                int seconds =0;
                                int year = 0;

                                // Tạo một đối tượng Calendar và đặt các giá trị date, month, year vào
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DATE, date);
                                calendar.set(Calendar.HOUR_OF_DAY, hours);
                                calendar.set(Calendar.MINUTE, minutes);
                                calendar.set(Calendar.SECOND, seconds);
                                calendar.set(Calendar.MILLISECOND, 0);

                                // Lấy đối tượng Date từ Calendar
                                bornDate = calendar.getTime();

                                // In ra ngày sinh đã được chuyển đổi
                            } else {
                                // Xử lý trường hợp born không tồn tại trong dữ liệu
                                System.out.println("Không tìm thấy thông tin về ngày sinh.");
                            }


                            String phone = taiKhoanSnapshot.child("phone").getValue(String.class);
                            // ... Lấy thêm thông tin khác từ Firebase

                            Account account = new Account();
                            account.setUsername(currentUsername);
                            account.setPassword(currentPassword);
                            account.setAvatar(avatar);
                            account.setName(name);
                            account.setSex(sex);
                            account.setBorn(bornDate);
                            account.setGmail(gmail);
                            account.setPhone(phone);

                            // Tạo Intent và đặt Bundle vào Intent
                            Intent intent = new Intent(WelcomeActivity.this, Home_Activity.class);
                            // Tạo Bundle và đặt đối tượng Account vào Bundle
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user_account", account);
                            intent.putExtra("idTaiKhoan", idTaiKhoan);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            return;
                        }
                    }
                    // Xử lý trường hợp không tìm thấy tài khoản
                    Toast.makeText(WelcomeActivity.this, "Tài khoản không tồn tại hoặc sai mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi đọc dữ liệu từ Firebase
                Toast.makeText(WelcomeActivity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}