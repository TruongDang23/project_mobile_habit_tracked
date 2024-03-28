package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Progress_total extends AppCompatActivity {

    ImageButton ibHome, ibMusic, ibClock, ibSetting;
    TextView txtHabitDone, txtBestSteaks, txtPerfectDay;
    private Account acc = new Account();
    String idUser;
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    int bestStreaks = 1;
    int habitDone;
    int perfectDay = 2;
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

        txtHabitDone = (TextView) findViewById(R.id.txtHabitDone);
        txtBestSteaks = (TextView) findViewById(R.id.txtBestStreaks);
        txtPerfectDay = (TextView) findViewById(R.id.txtPerfectDay);

        ibHome = (ImageButton) findViewById(R.id.ib_home);
        ibMusic = (ImageButton) findViewById(R.id.ib_music);
        ibClock = (ImageButton) findViewById(R.id.ib_clock);

        getBestStreaks();
        getHabitDone();
        getPerfectDay();

        txtBestSteaks.setText(Integer.toString(bestStreaks));
        txtPerfectDay.setText(Integer.toString(perfectDay));
    }
    public void getEvents()
    {
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

        ibMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Progress_total.this, SongsActivity.class);
                startActivity(intent);
            }
        });

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
    public void getConnection(String id)
    {
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference("Habit_Tracker").child("Du_Lieu").child(id);
    }
    public void getBestStreaks()
    {

    }
    public void getHabitDone()
    {
        getConnection(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    habitDone = 0; //Vì habitDone là bien toàn cục nên phải reset rồi mới cộng
                    for (DataSnapshot habitSnapshot : snapshot.getChildren())
                    {
                        String status = habitSnapshot.child("TrangThai").getValue(String.class);
                        if(status.equals("Đã hoàn thành"))
                            habitDone = habitDone + 1; // habitDone là biến toàn cục để có thể gọi trong hàm onDataChange
                    }
                    txtHabitDone.setText(Integer.toString(habitDone));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Progress_total.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getPerfectDay()
    {

    }
}