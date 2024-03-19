package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.Account;
import com.example.finalproject.model.ListviewHomeTest;
import com.example.finalproject.ui.LisviewHomeTestAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Home_Activity extends AppCompatActivity {
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    private Account acc = new Account();
    private DayScrollDatePicker mPicker;
    ListView listHome;
    ArrayList<ListviewHomeTest> arrayListHome;
    LisviewHomeTestAdapter adapterHome;
    Button btnNew;
    ImageButton ibGraph, ibMusic, ibClock, ibSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle b = getIntent().getExtras();
        acc = (Account) b.getSerializable("user_account");

        mPicker = findViewById(R.id.day_date_picker);
        mPicker.setStartDate(1, 3, 2024);
        mPicker.setEndDate(1, 3, 2025);
        mPicker.getSelectedDate(
                new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@Nullable Date date) {
                        if (date != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH) + 1;
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            Toast.makeText(Home_Activity.this, "Selected date is " + day + " / " + month + " / " + year, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        listHome = findViewById(R.id.lvHome);
        arrayListHome = new ArrayList<>();
        adapterHome = new LisviewHomeTestAdapter(arrayListHome, Home_Activity.this);
        listHome.setAdapter(adapterHome);

        btnNew = findViewById(R.id.btnNew);
        btnNew.setOnClickListener(v -> {
            Intent intent = new Intent(Home_Activity.this, Create_habit.class);
            startActivity(intent);
        });

        ibGraph = findViewById(R.id.ib_graph);
        ibGraph.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Progress_total.class);
            startActivity(i);
        });

        ibMusic = findViewById(R.id.ib_music);
        ibMusic.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, SongsActivity.class);
            startActivity(i);
        });

        ibClock = findViewById(R.id.ib_clock);
        ibClock.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Pomorodo.class);
            startActivity(i);
        });
        
        ibSettings = findViewById(R.id.ib_settings);
        ibSettings.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Setting.class);
            startActivity(i);
        });

//        listHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),arrayListHome.get(position).getNameHabit(),Toast.LENGTH_SHORT).show();
//            }
//        }); Chưa xử lý sự kiện click vào item của listView
        getListHabit();
    }
    public void getConnection(String id)
    {
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference("Habit_Tracker").child("Du_Lieu").child(id);
    }
    public void getListHabit()
    {
        String idUser = getIntent().getStringExtra("idTaiKhoan");
        getConnection(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot habitSnapshot : snapshot.getChildren())
                    {
                        String habitId = habitSnapshot.getKey();
                        int indexItem = checkHabitNotInList(habitId);

                        String nameHabit = habitSnapshot.child("Ten").getValue(String.class);
                        String time = habitSnapshot.child("ThoiGianNhacNho").getValue(String.class);
                        String donVi = habitSnapshot.child("DonVi").getValue(String.class);
                        double target = habitSnapshot.child("MucTieu").getValue(Double.class);
                        double doing = getHistoryData(habitSnapshot.child("ThoiGianThucHien"));
                        String period = habitSnapshot.child("KhoangThoiGian").getValue(String.class);
                        target = calculateTarget(target, period);
                        double donViTang = habitSnapshot.child("DonViTang").getValue(Double.class);
                        String done = doing + "/" + target + " " + donVi;

                        if(indexItem == -1)
                        {
                            arrayListHome.add(new ListviewHomeTest(habitId,nameHabit,time,done,(int) Math.ceil(doing * 100.0 / target),donViTang));
                            adapterHome.notifyDataSetChanged();
                        }
                        else
                        {
                            arrayListHome.set(indexItem, new ListviewHomeTest(habitId,nameHabit,time,done,(int) Math.ceil(doing * 100.0 / target),donViTang));
                            adapterHome.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home_Activity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public int calculateTarget(double target, String period)
    {
        int result = 0;
        if(period.equals("Day"))
            return (int)target;
        else if(period.equals("Week"))
            return (int) Math.ceil(target * 1.0 / 7);
        else if (period.equals("Month"))
            return (int) Math.ceil(target * 1.0 / 30);
        return result;
    }
    public double getHistoryData(DataSnapshot snapshot)
    {
        double result = 0;
        for(DataSnapshot timeSnapshot : snapshot.getChildren())
        {
            String time = timeSnapshot.getKey();
            if(isToday(time))
                result += timeSnapshot.getValue(Double.class);
        }
        return result;
    }
    public boolean isToday(String time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
        LocalDateTime parsedDateTime = LocalDateTime.parse(time, formatter);
        return parsedDateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
    public int checkHabitNotInList(String id)
    {
        for(int i = 0; i < arrayListHome.size(); i++)
        {
            if(id.equals(arrayListHome.get(i).getHabitId()))
                return i;
        }
        return -1;
    }
}