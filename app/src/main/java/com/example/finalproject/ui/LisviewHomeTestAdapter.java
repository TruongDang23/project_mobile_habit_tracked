package com.example.finalproject.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalproject.R;
import com.example.finalproject.activities.Home_Activity;
import com.example.finalproject.activities.MainActivity;
import com.example.finalproject.model.ListviewHomeTest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LisviewHomeTestAdapter extends ArrayAdapter<ListviewHomeTest> {
    Activity context;
    int resourcedId;
    private ArrayList<ListviewHomeTest> listviewHomeTestsArrayList;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String idUser = "User001";

    public LisviewHomeTestAdapter(ArrayList<ListviewHomeTest> listviewHomeTestsArrayList, Activity context, int resourceID) {
        super(context, resourceID, listviewHomeTestsArrayList);
        this.listviewHomeTestsArrayList = listviewHomeTestsArrayList;
        this.context = context;
        this.resourcedId = resourceID;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = context.getLayoutInflater().inflate(resourcedId,null);
        }
        TextView nameHabit = (TextView) convertView.findViewById(R.id.tvHomeListTitle);
        TextView section = (TextView) convertView.findViewById(R.id.tvHomeListSection);
        TextView timeHabit = (TextView) convertView.findViewById(R.id.tvHomeListTime);
        TextView doneProgress = (TextView) convertView.findViewById(R.id.tvDoneProgress);
        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.pbHomeListProgress);

        ListviewHomeTest listviewHomeTest = listviewHomeTestsArrayList.get(position);
        nameHabit.setText(listviewHomeTest.getNameHabit());
        section.setText(listviewHomeTest.getSection());
        timeHabit.setText(listviewHomeTest.getTimeHabit());
        doneProgress.setText(listviewHomeTest.getDoneProgress());
        progressBar.setProgress(listviewHomeTest.getDone());

        ImageButton ibPlus = (ImageButton) convertView.findViewById(R.id.ibHomeListPlus);
        ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConnection(idUser, listviewHomeTest.getHabitId());
                increaseData(listviewHomeTestsArrayList.get(position).getDonViTang());
            }
        });
        ImageButton ibMinus = (ImageButton) convertView.findViewById(R.id.ibHomeListMinus);
        ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConnection(idUser, listviewHomeTest.getHabitId());
                decreaseData(listviewHomeTestsArrayList.get(position).getDonViTang());
            }
        });
        return convertView;
    }
    public void getConnection(String idUser, String idHabit)
    {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Habit_Tracker").child("Du_Lieu").child(idUser).child(idHabit).child("ThoiGianThucHien");
    }
    public void increaseData(double number)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
        String parsedDateTime = LocalDateTime.now().format(formatter);
        ref.child(parsedDateTime).setValue(number);
    }
    public void decreaseData(double number)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
        String parsedDateTime = LocalDateTime.now().format(formatter);
        ref.child(parsedDateTime).setValue(-number);
    }
}
