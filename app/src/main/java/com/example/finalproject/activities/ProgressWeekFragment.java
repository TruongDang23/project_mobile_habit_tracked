package com.example.finalproject.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.finalproject.R;
import com.example.finalproject.ui.MyPagerProgressAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressWeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressWeekFragment extends Fragment {
    private BarChart barChart;
    private List<String> x_values = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProgressWeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressWeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressWeekFragment newInstance(String param1, String param2) {
        ProgressWeekFragment fragment = new ProgressWeekFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProgressWeekFragment newInstance(String idHabit, String idTaiKhoan, String test) {
        ProgressWeekFragment fragment = new ProgressWeekFragment();
        Bundle args = new Bundle();
        args.putString("idHabit", idHabit);
        args.putString("idTaiKhoan", idTaiKhoan);
        args.putString("test", test);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_week, container, false);
        // Lấy dữ liệu
        String idHabit = getArguments().getString("idHabit");
        String idTaiKhoan = getArguments().getString("idTaiKhoan");
        Log.d("idHabit w", idHabit);
        Log.d("idTaiKhoan w", idTaiKhoan);

        // Load x_values
        loadXValues();

        barChart = view.findViewById(R.id.barChart);
        setUpBarChart();
        loadBarChartData();

        // Inflate the layout for this fragment
        return view;
    }

    private void setUpBarChart() {
        barChart.getAxisRight().setDrawLabels(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBorders(true);
        barChart.setBorderColor(getResources().getColor(R.color.Blue));
    }

    private void loadBarChartData() {
        ArrayList<BarEntry> barEntriesArrayList = new ArrayList<>();
//        barEntriesArrayList.add(new BarEntry(0f, 4));
//        barEntriesArrayList.add(new BarEntry(1f, 6));
//        barEntriesArrayList.add(new BarEntry(2f, 8));
//        barEntriesArrayList.add(new BarEntry(3f, 2));
//        barEntriesArrayList.add(new BarEntry(4f, 4));
//        barEntriesArrayList.add(new BarEntry(5f, 1));
//        barEntriesArrayList.add(new BarEntry(6f, 3));
        // Nếu không có dữ liệu thì mặc định là 0
        for (int i = 0; i < 7; i++) {
            if (x_values.get(i).equals("")) {
                barEntriesArrayList.add(new BarEntry(i, null));
            } else {
                int random = (int) (Math.random() * 10 + 1);
                barEntriesArrayList.add(new BarEntry(i, random));
            }
        }

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setAxisMaximum(10);
        yAxis.setGranularity(1f);
        yAxis.setLabelCount(10);

        BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "Weekly Progress");
        int color = getResources().getColor(R.color.Blue);
        barDataSet.setColor(color);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();


        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(x_values));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
    }

    // Hàm lấy ngày hiện tại trong tuần cho vào x_values
    private void loadXValues() {
        int currentDay = LocalDateTime.now().getDayOfMonth();
        int currentMonth = LocalDateTime.now().getMonthValue();
        //int currentDay = 9;
        Log.d("currentDayIndex", String.valueOf(currentDay));
        if (currentDay > 7) {
            for (int i = 0; i < 7; i++) {
                int day = currentDay - i;
                if (day <= 0) {
                    day += 30;
                }
                String dayMonth = day + "/" + currentMonth;
                x_values.set(6 - i, dayMonth);
            }
        } else if (currentDay <= 7) {
            for (int i = 1; i < currentDay + 1; i++) {
                String dayMonth = i + "/" + currentMonth;
                x_values.set(i-1, dayMonth);
            }
            for (int i = currentDay +1; i < 8; i++) {
                x_values.set(i-1, "");
            }
        }
        Log.d("x_values", x_values.toString());

    }
}