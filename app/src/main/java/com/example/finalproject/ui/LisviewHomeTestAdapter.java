package com.example.finalproject.ui;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalproject.R;
import com.example.finalproject.model.ListviewHomeTest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LisviewHomeTestAdapter extends ArrayAdapter<ListviewHomeTest> {
    private ListView listView; // Thêm trường ListView
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ArrayList<ListviewHomeTest> listviewHomeTestsArrayList;
    Context context;
    public LisviewHomeTestAdapter(ArrayList<ListviewHomeTest> listviewHomeTestsArrayList, Context context) {
        super(context, R.layout.list_item_home, listviewHomeTestsArrayList);
        this.listviewHomeTestsArrayList = listviewHomeTestsArrayList;
        this.context = context;
    }
    private static class ViewHolder {
        public TextView nameHabit;
        public TextView timeHabit;
        public TextView doneProgress;
        public ProgressBar progressBar;
        public int progressDone;
        public ImageButton ibPlus, ibMinus;
    }
    @NonNull
    @Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        android.view.View view = convertView;
        ViewHolder viewHolder;
        if (view == null)
        {
            view = android.view.View.inflate(context, R.layout.list_item_home, null);
            viewHolder = new ViewHolder();
            viewHolder.nameHabit = view.findViewById(R.id.tvHomeListTitle);
            viewHolder.timeHabit = view.findViewById(R.id.tvHomeListTime);
            viewHolder.doneProgress = view.findViewById(R.id.tvDoneProgress);
            viewHolder.progressBar = view.findViewById(R.id.pbHomeListProgress);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        ListviewHomeTest listviewHomeTest = listviewHomeTestsArrayList.get(position);
        viewHolder.nameHabit.setText(listviewHomeTest.getNameHabit());
        viewHolder.timeHabit.setText(listviewHomeTest.getTimeHabit());
        viewHolder.doneProgress.setText(listviewHomeTest.getDoneProgress());
        viewHolder.progressBar.setProgress(listviewHomeTest.getDone());

        viewHolder.ibPlus = view.findViewById(R.id.ibHomeListPlus);
        viewHolder.ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConnection("User001", listviewHomeTest.getHabitId());
                increaseData(listviewHomeTestsArrayList.get(position).getDonViTang());
            }
        });
        viewHolder.ibMinus = view.findViewById(R.id.ibHomeListMinus);
        viewHolder.ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConnection("User001", listviewHomeTest.getHabitId());
                decreaseData(listviewHomeTestsArrayList.get(position).getDonViTang());
            }
        });
        return view;
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
