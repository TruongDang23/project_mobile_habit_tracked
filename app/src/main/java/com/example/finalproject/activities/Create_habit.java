package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.Habit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class Create_habit extends AppCompatActivity  {

    private FirebaseDatabase database;
    private DatabaseReference ref;

    private Button btnComplete;
    private String idTaiKhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        idTaiKhoan = getIntent().getStringExtra("idTaiKhoan");
        btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHabit();
            }
        });
    }
    private void connectionFirebase(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Habit_Tracker").child("Du_Lieu").child(idTaiKhoan);
    }
    private void addHabit(){
        connectionFirebase();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long count = dataSnapshot.getChildrenCount() + 1;
                    String idThoiQuen = null;
                    if(count >= 100)
                        idThoiQuen = "ThoiQuen" + count;
                    else if(count >= 10)
                        idThoiQuen = "ThoiQuen0" + count;
                    else if(count >= 0)
                        idThoiQuen = "ThoiQuen00" + count;

                    Habit habit = new Habit();
                    habit.setTen("Chạy bộ");
                    habit.setDonVi("km");
                    habit.setDonViTang(0.1);
                    habit.setKhoangThoiGian("Day");
                    habit.setLoiNhacNho("Chạy thôi, chạy thoi");
                    habit.setMauSac("#187BCE");
                    habit.setMoTa("Chạy bộ");
                    habit.setMucTieu(5);
                    habit.setThoiDiem("Anytime");
                    habit.setThoiGianBatDau("06-03-2024");
                    habit.setThoiGianKetThuc("15-04-2024");
                    habit.setThoiGianNhacNho("1:32AM");
                    // Set thời gian thực hiện (nếu cần)
                    // thoiQuen.setThoiGianThucHien(...);
                    habit.setTrangThai("Đang thực hiện");

                    ref.child(idThoiQuen).setValue(habit);
                    Intent intent = new Intent(Create_habit.this, Home_Activity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Create_habit.this, "Không thể kết nối FireBase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}