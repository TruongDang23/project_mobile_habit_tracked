package com.example.finalproject.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressDayFragment extends Fragment {
    private PieChart pieChart;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    private TextView tvTodayProgress;
    private TextView tvCurrentProgress;

    public ProgressDayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressDayFragment newInstance(String param1, String param2) {
        ProgressDayFragment fragment = new ProgressDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProgressDayFragment newInstance(String idHabit, String idTaiKhoan, String test) {
        ProgressDayFragment fragment = new ProgressDayFragment();
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
        View view = inflater.inflate(R.layout.fragment_progress_day, container, false);
        // Lấy dữ liệu
        String idHabit = getArguments().getString("idHabit");
        String idTaiKhoan = getArguments().getString("idTaiKhoan");

        pieChart = view.findViewById(R.id.pieChart);
        tvTodayProgress = view.findViewById(R.id.tvTodayProgress);
        tvCurrentProgress = view.findViewById(R.id.tvCurrentProgress);

        setUpPieChart();
        getProgressDay(idTaiKhoan,idHabit);

        // Inflate the layout for this fragment
        return view;
    }
    private void getConnection(String idTaiKhoan, String idHabit){
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference("Habit_Tracker").child("Du_Lieu").child(idTaiKhoan).child(idHabit);
    }
    private void getProgressDay(String idTaiKhoan, String idHabit){
        getConnection(idTaiKhoan,idHabit);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    try {
                        double target = dataSnapshot.child("MucTieu").getValue(Double.class);
                        String period = dataSnapshot.child("KhoangThoiGian").getValue(String.class);
                        target = calculateTarget(target, period);
                        String donvi = dataSnapshot.child("DonVi").getValue(String.class);
                        tvTodayProgress.setText("Target "+String.format("%.1f", target)+" "+donvi);

                        // Tính tổng khối lượng công việc đã thực hiện trong ngày
                        double tongKhoiLuong = 0;
                        for (DataSnapshot snapshot : dataSnapshot.child("ThoiGianThucHien").getChildren()) {
                            String thoiGian = snapshot.getKey();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ssa");
                            Date date = sdf.parse(thoiGian);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            Calendar today = Calendar.getInstance();
                            if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                    cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                                tongKhoiLuong += snapshot.getValue(Double.class);
                            }
                        }

                        tvCurrentProgress.setText(String.format("%.1f", tongKhoiLuong)+" "+donvi);
                        loadPieChartData(tongKhoiLuong,target);
                        pieChart.invalidate();
                    }catch (Exception e){
                        Log.d("Error", e.getMessage());
                    }
                }
                else
                    Toast.makeText(getContext(), "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public double calculateTarget(double target, String period)
    {
        double result = 0;
        if(period.equals("Day"))
            return target;
        else if(period.equals("Week"))
            return (target/ 7);
        else if (period.equals("Month"))
            return (target/ 30);
        return result;
    }
    private void setUpPieChart() {
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(0);
        pieChart.setTransparentCircleRadius(61f);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }
    private void loadPieChartData(double tongKhoiLuong, double muctieu) {
        // creating data values
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) (tongKhoiLuong / muctieu * 100), ""));
        entries.add(new PieEntry((float) (100 - tongKhoiLuong / muctieu * 100), ""));

        // creating dataset
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setSliceSpace(3f);
        dataset.setSelectionShift(5f);
        dataset.setDrawValues(false);
//        dataset.setColors(ColorTemplate.JOYFUL_COLORS);
        int [] colors = {R.color.Blue, R.color.white};
        dataset.setColors(colors, getContext());

        // creating data
        PieData data = new PieData(dataset);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);

        // setting data
        pieChart.setData(data);
    }
}