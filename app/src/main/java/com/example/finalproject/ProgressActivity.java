package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.finalproject.ui.MyPagerProgressAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        TabLayout tabLayout = findViewById(R.id.tabLayoutProgress);
        TabItem tabDay = findViewById(R.id.tabDay);
        TabItem tabWeek = findViewById(R.id.tabWeek);
        TabItem tabMonth = findViewById(R.id.tabMonth);

        ViewPager viewPagerProgress = findViewById(R.id.viewpagerProgress);

        MyPagerProgressAdapter paperProgressAdapter = new MyPagerProgressAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPagerProgress.setAdapter(paperProgressAdapter);

        // Dung cho viec chuyen tab
        tabLayout.setupWithViewPager(viewPagerProgress);
    }
}