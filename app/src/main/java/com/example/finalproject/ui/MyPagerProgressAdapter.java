package com.example.finalproject.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.finalproject.activities.ProgressDayFragment;
import com.example.finalproject.activities.ProgressMonthFragment;
import com.example.finalproject.activities.ProgressWeekFragment;

public class MyPagerProgressAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public MyPagerProgressAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    public MyPagerProgressAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProgressDayFragment();
            case 1:
                return new ProgressWeekFragment();
            case 2:
                return new ProgressMonthFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Day";
                break;
            case 1:
                title = "Week";
                break;
            case 2:
                title = "Month";
                break;
        }
        return title;
    }
}
