package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class Progress_total extends AppCompatActivity {

    ImageButton ibHome, ibMusic, ibClock, ibSetting;
    TextView txtHabitDone, txtBestSteaks, txtPerfectDay;
    private Account acc = new Account();
    String idUser;
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    int bestStreaks;
    int habitDone;
    int perfectDay;
    int numHabit;
    int[] perfectArr = new int[32];
    private MaterialCalendarView calendar;
    private Set<CalendarDay> habitDays = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_total);
        getData();
        getEvents();
    }
    public void getData()
    {
        Bundle b = getIntent().getExtras();
        acc = (Account) b.getSerializable("user_account");
        idUser = getIntent().getStringExtra("idTaiKhoan");

        txtHabitDone = (TextView) findViewById(R.id.txtHabitDone);
        txtBestSteaks = (TextView) findViewById(R.id.txtBestStreaks);
        txtPerfectDay = (TextView) findViewById(R.id.txtPerfectDay);

        ibHome = (ImageButton) findViewById(R.id.ib_home);
        ibMusic = (ImageButton) findViewById(R.id.ib_music);
        ibClock = (ImageButton) findViewById(R.id.ib_clock);

        calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        getHabitDone();
        getPerfectDay();
        highlightDays();
        txtBestSteaks.setText(Integer.toString(bestStreaks));

    }
    public void getEvents()
    {
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent và đặt Bundle vào Intent
                Intent intent = new Intent(Progress_total.this, Home_Activity.class);
                // Tạo Bundle và đặt đối tượng Account vào Bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_account", acc);
                intent.putExtra("idTaiKhoan", idUser);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ibMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Progress_total.this, SongsActivity.class);
                startActivity(intent);
            }
        });

        ibClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Progress_total.this, Pomorodo.class);
                startActivity(intent);
            }
        });

        ibSetting = (ImageButton) findViewById(R.id.ib_settings);
        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Progress_total.this, Setting.class);
                startActivity(intent);
            }
        });
    }
    public void getConnection(String id)
    {
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference("Habit_Tracker").child("Du_Lieu").child(id);
    }
    public void highlightDays() {
        getConnection(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot timeSnapshot : habitSnapshot.child("ThoiGianThucHien").getChildren()) {
                        String time = timeSnapshot.getKey();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
                        LocalDateTime parsedDateTime = LocalDateTime.parse(time, formatter);
                        CalendarDay day = CalendarDay.from(parsedDateTime.getYear(), parsedDateTime.getMonthValue(), parsedDateTime.getDayOfMonth());
                        habitDays.add(day);
                    }
                }
                setupCalendar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Progress_total.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCalendar() {
        calendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        calendar.setDateSelected(CalendarDay.today(), true);
        for (CalendarDay day : habitDays) {
            calendar.setDateSelected(day, true);
        }
    }

    public void getHabitDone()
    {
        getConnection(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    habitDone = 0; //Vì habitDone là bien toàn cục nên phải reset rồi mới cộng
                    resetArray(perfectArr);
                    for (DataSnapshot habitSnapshot : snapshot.getChildren())
                    {
                        String status = habitSnapshot.child("TrangThai").getValue(String.class);
                        if(status.equals("Đã hoàn thành"))
                            habitDone = habitDone + 1; // habitDone là biến toàn cục để có thể gọi trong hàm onDataChange
                    }
                    txtHabitDone.setText(Integer.toString(habitDone));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Progress_total.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getPerfectDay()
    {
        getConnection(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    numHabit = 0; //Vì numHabit là biến toàn cục nên phải reset rồi mới cộng
                    resetArray(perfectArr);
                    for (DataSnapshot habitSnapshot : snapshot.getChildren())
                    {
                        String status = habitSnapshot.child("TrangThai").getValue(String.class);
                        if(status.equals("Đang thực hiện"))
                            numHabit = numHabit + 1; // numHabit là biến toàn cục để có thể gọi trong hàm onDataChange
                    }

                    for (DataSnapshot habitSnapshot : snapshot.getChildren())
                    {
                        String status = habitSnapshot.child("TrangThai").getValue(String.class);
                        if(status.equals("Đang thực hiện"))
                            calculatePerfectDay(habitSnapshot.child("ThoiGianThucHien"));
                    }
                    countPerfectDay();
                    countBestStreaks();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Progress_total.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void calculatePerfectDay(DataSnapshot snapshot)
    {
        int oldIndex = 0;
        for(DataSnapshot timeSnapshot : snapshot.getChildren())
        {
            String time = timeSnapshot.getKey();
            if(isThisMonth(time))
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
                LocalDateTime parsedDateTime = LocalDateTime.parse(time, formatter);
                int index = parsedDateTime.getDayOfMonth();
                if (index != oldIndex)
                {
                    perfectArr[index] = perfectArr[index] + 1;
                    oldIndex = index;
                }
            }
        }
    }
    public boolean isThisMonth(String time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
        LocalDateTime parsedDateTime = LocalDateTime.parse(time, formatter);
        return (parsedDateTime.getMonth().equals(LocalDateTime.now().getMonth())) && (parsedDateTime.getYear() == (LocalDateTime.now().getYear()));
        //Nếu tháng & năm trên firebase trùng với tháng, năm ở thời điểm hiện tại thì TRUE, ngược lại FALSE
    }
    public void countPerfectDay()
    {
        /*
        Hàm này có chức năng tăng bit ở ngày tương ứng lên 1 đơn vị khi ngày đó nguười dùng có thực hiện thói quen
        Vì mảng có 32 phần từ: Từ 0 đến 31 sẽ tương ứng cho 31 ngày trong tháng. Phần tử nào có giá trị = số lượng thói quen đang thực hiện
        ==> Ngày hôm đó thực hiện đầy đủ các thói quen (Bất kể khối lượng nhiều hay ít) ==> Perfect Day
        */
        perfectDay = 0;
        for(int i = 0; i < 32; i++)
        {
            if(perfectArr[i] == numHabit)
                perfectDay++;
        }
        txtPerfectDay.setText(Integer.toString(perfectDay));
    }
    public void countBestStreaks()
    {
        /*
            Hàm này cũng dựa vào mảng perfectArr để xác định BestStreaks.
            Khi một chuỗi perfectDay liên tiếp thì sẽ được xem là ứng cử viên cho BestStreak
            BestStreak thật sự là số lớn nhất trong các ứng cử viên đó
         */
        bestStreaks = 1;
        int maxBestStreak = 1;
        for(int i = 0; i < 31; i++)
        {
            if(perfectArr[i] == numHabit && perfectArr[i+1] == numHabit) //Kiểm tra đang là chuỗi perfect liên tiếp
                bestStreaks++;
            if(perfectArr[i] < numHabit)// Bị ngắt chuỗi
            {
                //Khi bị ngắt chuỗi thì sẽ tiến hành xem xét ứng cử viên cho BestStreak và reset lại biến bestStreak
                if(maxBestStreak <= bestStreaks)
                    maxBestStreak = bestStreaks;
                bestStreaks = 1;
            }
        }
        //Khi chưa có chuỗi perfect thì maxBestStreak sẽ mang giá trị mặc định: 1
        //Tuy nhiên không tồn tại chuỗi 1 nên phải thay bằng số 0, chuỗi là phải từ 2 trở lên
        maxBestStreak = (maxBestStreak == 1) ? 0 : maxBestStreak;
        txtBestSteaks.setText(Integer.toString(maxBestStreak));
    }
    public void resetArray(int[] arr)
    {
        Arrays.fill(arr, 0);
    }
}