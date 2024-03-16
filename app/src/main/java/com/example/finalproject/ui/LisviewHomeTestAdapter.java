package com.example.finalproject.ui;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.finalproject.R;
import com.example.finalproject.model.ListviewHomeTest;

import java.util.ArrayList;

public class LisviewHomeTestAdapter extends ArrayAdapter<ListviewHomeTest> {
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
    }
    @NonNull
    @Override
    public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
        android.view.View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            view = android.view.View.inflate(context, R.layout.list_item_home, null);
            viewHolder = new ViewHolder();
            viewHolder.nameHabit = view.findViewById(R.id.tvHomeListTitle);
            viewHolder.timeHabit = view.findViewById(R.id.tvHomeListTime);
            viewHolder.doneProgress = view.findViewById(R.id.tvDoneProgress);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ListviewHomeTest listviewHomeTest = listviewHomeTestsArrayList.get(position);
        viewHolder.nameHabit.setText(listviewHomeTest.getNameHabit());
        viewHolder.timeHabit.setText(listviewHomeTest.getTimeHabit());
        viewHolder.doneProgress.setText(listviewHomeTest.getDoneProgress());
        return view;
    }
}
