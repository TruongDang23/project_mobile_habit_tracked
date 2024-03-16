package com.example.finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import android.graphics.Color;

import java.util.ArrayList;

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
        pieChart = view.findViewById(R.id.pieChart);
        setUpPieChart();
        loadPieChartData();

        // Inflate the layout for this fragment
        return view;
    }
    private void setUpPieChart() {
        pieChart.setUsePercentValues(true);
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
        l.setEnabled(true);
    }
    private void loadPieChartData() {
        // creating data values
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(70.5f, ""));
        entries.add(new PieEntry(29.5f, ""));

        // creating dataset
        PieDataSet dataset = new PieDataSet(entries, "");
        dataset.setSliceSpace(3f);
        dataset.setSelectionShift(5f);
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