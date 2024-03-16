package com.example.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.model.Account;

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