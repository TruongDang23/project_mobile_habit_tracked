package com.example.finalproject.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.example.finalproject.R;
import com.example.finalproject.model.HabitWeek;
import com.example.finalproject.ui.MyPagerProgressAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    private HabitWeek habitWeek = new HabitWeek();
    private BarChart barChart;
    private List<String> x_values = Arrays.asList("", "", "", "", "", "", "");

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
        loadBarChartData(idHabit, idTaiKhoan);
        setUpBarChart();


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

    private void loadBarChartData(String idHabit, String idTaiKhoan) {
        getConnection(idTaiKhoan, idHabit);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        double muctieu = dataSnapshot.child("MucTieu").getValue(Double.class);
                        habitWeek.setMucTieu(muctieu);
                        String thoiGianBatDau = dataSnapshot.child("ThoiGianBatDau").getValue(String.class);
                        String thoiGianKetThuc = dataSnapshot.child("ThoiGianKetThuc").getValue(String.class);
                        int thoiGianThucHien = getThoiGianThucHien(thoiGianBatDau, thoiGianKetThuc);
                        habitWeek.setSoNgayThucHien(thoiGianThucHien);
                        Log.d("t4", String.valueOf(habitWeek.getSoNgayThucHien()));
                        String khoangThoiGian = dataSnapshot.child("KhoangThoiGian").getValue(String.class);
                        int khoangThoiGianInt = getKhoangThoiGian(khoangThoiGian);
                        habitWeek.setKhoangThoiGian(khoangThoiGianInt);
                        String trangThai = dataSnapshot.child("TrangThai").getValue(String.class);
                        habitWeek.setTrangThai(trangThai);
                        double donViTang = dataSnapshot.child("DonViTang").getValue(Double.class);
                        habitWeek.setDonViTang(donViTang);
                        float mucTieuNgay = calculateMaxAxis(muctieu, thoiGianThucHien);
                        habitWeek.setMucTieuNgay(mucTieuNgay);

                        // Set up BarChart
                        ArrayList<BarEntry> barEntriesArrayList = new ArrayList<>();
                        // Nếu không có dữ liệu thì mặc định là 0
                        for (int i = 0; i < 7; i++) {
                            if (x_values.get(i).equals("")) {
                                barEntriesArrayList.add(new BarEntry(i, null));
                            } else {
                                float percentDay = calculateIncrease(dataSnapshot.child("ThoiGianThucHien"), mucTieuNgay, i+1, donViTang);
                                barEntriesArrayList.add(new BarEntry(i, percentDay));
                            }
                        }
                        YAxis yAxis = barChart.getAxisLeft();
                        yAxis.setAxisMinimum(0);

                        yAxis.setAxisMaximum(100);
                        yAxis.setGranularity(1f);
                        yAxis.setLabelCount(10);

                        BarDataSet barDataSet = new BarDataSet(barEntriesArrayList, "Weekly Progress (%)");
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
                    catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }
                }
                else {
                    Toast.makeText(getContext(), "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private int getThoiGianThucHien(String thoiGianBatDau, String thoiGianKetThuc) {
        // thoiGianBatDau và thoiGianKetThuc có dạng "dd-MM-yyyy"
        String[] thoiGianBatDauArr = thoiGianBatDau.split("-");
        String[] thoiGianKetThucArr = thoiGianKetThuc.split("-");
        LocalDate ngayBatDau = LocalDate.of(Integer.parseInt(thoiGianBatDauArr[2]), Integer.parseInt(thoiGianBatDauArr[1]), Integer.parseInt(thoiGianBatDauArr[0]));
        LocalDate ngayKetThuc = LocalDate.of(Integer.parseInt(thoiGianKetThucArr[2]), Integer.parseInt(thoiGianKetThucArr[1]), Integer.parseInt(thoiGianKetThucArr[0]));
        int thoiGianThucHien = ngayKetThuc.getDayOfYear() - ngayBatDau.getDayOfYear() + 1;
        return thoiGianThucHien;
    }
    private float calculateMaxAxis(double mucTieu, int thoiGianThucHien) {
        //int maxAxis = (int) Math.ceil(mucTieu*1.0 / thoiGianThucHien);
        double result = mucTieu * 1.0 / thoiGianThucHien;
        double roundedResult = Math.round(result * 100.0) / 100.0;
        return (float) roundedResult;
    }
    private int getKhoangThoiGian(String khoangThoiGian) {
        if (khoangThoiGian.equals("Day")) {
            return 1;
        } else if (khoangThoiGian.equals("Week")) {
            return 7;
        } else if (khoangThoiGian.equals("Month")) {
            return 30;
        }
        return 0;
    }
    // Hàm tính toán độ tăng của một thói quen trong ngày
    private float calculateIncrease(DataSnapshot dataSnapshot, float mucTieuNgay, int ngay, double donViTang) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
        int currentMonth = LocalDateTime.now().getMonthValue();
        double value = 0;
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            String key = snapshot.getKey();
            String[] keyArr = key.split(" ");
            String[] dateArr = keyArr[0].split("-");
            int day = Integer.parseInt(dateArr[0]);
            int month = Integer.parseInt(dateArr[1]);
            if (day == ngay && month == currentMonth) {
                value += snapshot.getValue(Double.class);
            }
        }
        // Tính phần trăm value so với mucTieuNgay
        double increase = Math.round((value* 100.0* 100.0 / mucTieuNgay)/ 100.0);
        return (float) increase;
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
    public void getConnection(String idUser, String idHabit) {
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference("Habit_Tracker").child("Du_Lieu").child(idUser).child(idHabit);
    }
}