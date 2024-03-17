package com.example.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.model.Account;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Setting extends AppCompatActivity {
    Handler mainHandler = new Handler();

    private Account getAccount = new Account();

    TextView txtName, txtGender, txtBorn, txtPhone, txtGmail;
    ImageButton imgBtnClcok, imgBtnMusic, imgBtnGraph, imgBtnHome;
    Button btnEdit;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        doFormWidget();
        getData();
    }
    public void doFormWidget(){
        String idTaiKhoan = getIntent().getStringExtra("idTaiKhoan");
        txtName = findViewById(R.id.txtname);
        txtGender = findViewById(R.id.txtgender);
        txtBorn = findViewById(R.id.txtBirthday);
        txtPhone = findViewById(R.id.txtPhone);
        txtGmail = findViewById(R.id.txtgmail);
        imageView = findViewById(R.id.imvAvatar);
        btnEdit = findViewById(R.id.btnEdit);
        imgBtnGraph = findViewById(R.id.ib_graph);
        imgBtnClcok = findViewById(R.id.ib_clock);
        imgBtnHome = findViewById(R.id.ib_home);
        imgBtnMusic = findViewById(R.id.ib_music);
        imgBtnGraph.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, Progress_total.class);
            startActivity(intent);
        });
        imgBtnClcok.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, Pomorodo.class);
            startActivity(intent);
        });
        imgBtnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, Home_Activity.class);
            startActivity(intent);
        });
        imgBtnMusic.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, SongsActivity.class);
            startActivity(intent);
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Setting.this, SettingEditInfor.class );
                // Tạo Bundle và đặt đối tượng Account vào Bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_account", getAccount);
                i.putExtra("idTaiKhoan", idTaiKhoan);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }
    public void getData(){
        // Nhận Bundle từ Intent

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Lấy đối tượng Account từ Bundle
            Account account = (Account) bundle.getSerializable("user_account");
            getAccount = account;
            if (account != null) {
                txtName.setText(account.getName());
                txtGender.setText(account.getSex());
                txtPhone.setText(Long.toString(account.getPhone()));
                txtGmail.setText(account.getGmail());
                Date born = account.getBorn();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String strBorn = dateFormat.format(born);
                txtBorn.setText(strBorn);
                Glide.with(this).load(account.getAvatar()).into(imageView);
            }
        }

    }
}