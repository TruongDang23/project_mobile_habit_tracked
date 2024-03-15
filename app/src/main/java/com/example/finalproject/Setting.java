package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalproject.databinding.ActivityMainBinding;
import com.example.finalproject.modal.Account;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Setting extends AppCompatActivity {
    Handler mainHandler = new Handler();


    TextView txtName, txtGender, txtBorn, txtPhone, txtGmail;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        doFormWidget();
        getData();
    }
    public void doFormWidget(){
        txtName = findViewById(R.id.txtname);
        txtGender = findViewById(R.id.txtgender);
        txtBorn = findViewById(R.id.txtBirthday);
        txtPhone = findViewById(R.id.txtPhone);
        txtGmail = findViewById(R.id.txtgmail);
        imageView = findViewById(R.id.imvAvatar);
    }
    public void getData(){
        // Nhận Bundle từ Intent

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Lấy đối tượng Account từ Bundle
            Account account = (Account) bundle.getSerializable("user_account");
            if (account != null) {
                txtName.setText(account.getName());
                txtGender.setText(account.getSex());
                txtPhone.setText(Long.toString(account.getPhone()));
                txtGmail.setText(account.getGmail());
                Date born = account.getBorn();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String strBorn = dateFormat.format(born);
                txtBorn.setText(strBorn);
              //  Glide.with(this).load(account.getAvatar()).into(imageView);
            }
        }

    }
}