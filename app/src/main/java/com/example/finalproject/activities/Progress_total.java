package com.example.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.finalproject.R;
import com.example.finalproject.model.Account;

public class Progress_total extends AppCompatActivity {

    ImageButton ibHome, ibMusic, ibClock, ibSetting;
    private Account acc = new Account();
    String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_total);
        getData();
        getEvents();
    }
    public void getData()
    {
        Bundle b = getIntent().getExtras();
        acc = (Account) b.getSerializable("user_account");
        idUser = getIntent().getStringExtra("idTaiKhoan");

    }
    public void getEvents()
    {
        ibHome = (ImageButton) findViewById(R.id.ib_home);
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent và đặt Bundle vào Intent
                Intent intent = new Intent(Progress_total.this, Home_Activity.class);
                // Tạo Bundle và đặt đối tượng Account vào Bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_account", acc);
                intent.putExtra("idTaiKhoan", idUser);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ibMusic = (ImageButton) findViewById(R.id.ib_music);
        ibMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Progress_total.this, SongsActivity.class);
                startActivity(intent);
            }
        });

        ibClock = (ImageButton) findViewById(R.id.ib_clock);
        ibClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Progress_total.this, Pomorodo.class);
                startActivity(intent);
            }
        });

        ibSetting = (ImageButton) findViewById(R.id.ib_settings);
        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Progress_total.this, Setting.class);
                startActivity(intent);
            }
        });
    }
}