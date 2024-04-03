package com.example.finalproject.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressMonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressMonthFragment extends Fragment {
    private MaterialCalendarView calendar;
    private Set<CalendarDay> habitDays = new HashSet<>();
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProgressMonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressMonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressMonthFragment newInstance(String param1, String param2) {
        ProgressMonthFragment fragment = new ProgressMonthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProgressMonthFragment newInstance(String idHabit, String idTaiKhoan, String test) {
        ProgressMonthFragment fragment = new ProgressMonthFragment();
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
    public void getConnection(String userId,String habbitId){
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference("Habit_Tracker").child("Du_Lieu").child(userId).child(habbitId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_month, container, false);
        // Lấy dữ liệu
        String idHabit = getArguments().getString("idHabit");
        String idTaiKhoan = getArguments().getString("idTaiKhoan");
        Log.d("idHabit m", idHabit);
        Log.d("idTaiKhoan m", idTaiKhoan);
        calendar = view.findViewById(R.id.calendarView);
        setupCalendar();
        highlightDays();
        // Inflate the layout for this fragment
        return view;
    }

    private void setupCalendar() {
        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        calendar.setDateSelected(CalendarDay.today(), true);
        calendar.setDateSelected(CalendarDay.today(), true);
        for (CalendarDay day : habitDays) {
            calendar.setDateSelected(day, true);
        }


    }
    public void highlightDays() {
        String idHabit = getArguments().getString("idHabit");
        String idTaiKhoan = getArguments().getString("idTaiKhoan");
        getConnection(idTaiKhoan,idHabit);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot timeSnapshot : snapshot.child("ThoiGianThucHien").getChildren()) {
                        String time = timeSnapshot.getKey();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
                        LocalDateTime parsedDateTime = LocalDateTime.parse(time, formatter);
                        CalendarDay day = CalendarDay.from(parsedDateTime.getYear(), parsedDateTime.getMonthValue(), parsedDateTime.getDayOfMonth());
                        habitDays.add(day);
                    }

                setupCalendar();
            }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });
    }
}