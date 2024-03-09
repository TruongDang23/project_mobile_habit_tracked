package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finalproject.modal.ListviewHomeTest;
import com.example.finalproject.ui.LisviewHomeTestAdapter;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Home_Activity extends AppCompatActivity {
    private DayScrollDatePicker mPicker;
    ListView listHome;
    ArrayList<ListviewHomeTest> arrayListHome;
    LisviewHomeTestAdapter adapterHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        arrayListHome.add(new ListviewHomeTest("Reading", "10:00", "100/500"));
        arrayListHome.add(new ListviewHomeTest("Running", "11:00", "200/500"));
        arrayListHome.add(new ListviewHomeTest("Swimming", "12:00", "300/500"));
        arrayListHome.add(new ListviewHomeTest("Cycling", "13:00", "400/500"));
        arrayListHome.add(new ListviewHomeTest("Reading", "10:00", "100/500"));
        arrayListHome.add(new ListviewHomeTest("Running", "11:00", "200/500"));
        arrayListHome.add(new ListviewHomeTest("Swimming", "12:00", "300/500"));


        adapterHome = new LisviewHomeTestAdapter(arrayListHome, Home_Activity.this);
        listHome.setAdapter(adapterHome);
    }
}