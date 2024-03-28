package com.example.finalproject.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.Account;
import com.example.finalproject.model.ListviewHomeTest;
import com.example.finalproject.model.MyBroadcastReceiver;
import com.example.finalproject.ui.LisviewHomeTestAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class Home_Activity extends AppCompatActivity {
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    private Account acc = new Account();
    private DayScrollDatePicker mPicker;
    private TextView currentDay;
    ListView listHome;
    ArrayList<ListviewHomeTest> arrayListHome;

    LisviewHomeTestAdapter adapterHome;
    Button btnNew;
    ImageButton ibGraph, ibMusic, ibClock, ibSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle b = getIntent().getExtras();
        acc = (Account) b.getSerializable("user_account");
        String idTaiKhoan = getIntent().getStringExtra("idTaiKhoan");

        Calendar calendar = Calendar.getInstance();
        mPicker = findViewById(R.id.day_date_picker);
        mPicker.setStartDate(1, 3, 2024);
        mPicker.setEndDate(1, 3, 2025);
        currentDay = findViewById(R.id.txtCurrentDay);

        Calendar currCalendar = Calendar.getInstance();
        currentDay.setText(currCalendar.getTime().toString());

        listHome = findViewById(R.id.lvHome);
        arrayListHome = new ArrayList<>();
        adapterHome = new LisviewHomeTestAdapter(arrayListHome, Home_Activity.this, R.layout.list_item_home);
        listHome.setAdapter(adapterHome);

        listHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Home_Activity.this, ProgressActivity.class);
                // Tạo Bundle và đặt đối tượng Account vào Bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_account", acc);
                intent.putExtras(bundle);
                intent.putExtra("idThoiQuen", arrayListHome.get(position).getHabitId());
                intent.putExtra("idTaiKhoan", idTaiKhoan);
                Log.d("HabitID", arrayListHome.get(position).getHabitId());
                Log.d("Username", acc.getUsername());
                Log.d("idTaiKhoan", idTaiKhoan);
                startActivity(intent);
            }
        });

        btnNew = findViewById(R.id.btnNew);
        btnNew.setOnClickListener(v -> {
            Intent intent = new Intent(Home_Activity.this, Create_habit.class);
            // Tạo Bundle và đặt đối tượng Account vào Bundle
            Bundle bundle = new Bundle();
            intent.putExtra("idTaiKhoan", "User001");
            intent.putExtras(bundle);
            startActivity(intent);
        });

        ibGraph = findViewById(R.id.ib_graph);
        ibGraph.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Progress_total.class);
            startActivity(i);
        });

        ibMusic = findViewById(R.id.ib_music);
        ibMusic.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, SongsActivity.class);
            startActivity(i);
        });

        ibClock = findViewById(R.id.ib_clock);
        ibClock.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Pomorodo.class);
            startActivity(i);
        });
        
        ibSettings = findViewById(R.id.ib_settings);
        ibSettings.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Setting.class);
            startActivity(i);
        });

        getListHabit();
        getReminder();
        notification();
    }
    public void getConnection(String id)
    {
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference("Habit_Tracker").child("Du_Lieu").child(id);
    }
    public void getListHabit()
    {
        String idUser = getIntent().getStringExtra("idTaiKhoan");
        getConnection(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot habitSnapshot : snapshot.getChildren())
                    {
                        String habitId = habitSnapshot.getKey();
                        int indexItem = checkHabitNotInList(habitId);

                        String nameHabit = habitSnapshot.child("Ten").getValue(String.class);
                        String time = habitSnapshot.child("ThoiGianNhacNho").getValue(String.class);
                        String donVi = habitSnapshot.child("DonVi").getValue(String.class);
                        String reminder = habitSnapshot.child("LoiNhacNho").getValue(String.class);
                        double target = habitSnapshot.child("MucTieu").getValue(Double.class);
                        double doing = getHistoryData(habitSnapshot.child("ThoiGianThucHien"));
                        String period = habitSnapshot.child("KhoangThoiGian").getValue(String.class);
                        target = calculateTarget(target, period);
                        double donViTang = habitSnapshot.child("DonViTang").getValue(Double.class);
                        String done = doing + "/" + target + " " + donVi;


                        if(indexItem == -1)
                        {
                            arrayListHome.add(new ListviewHomeTest(habitId,nameHabit,time,done,(int) Math.ceil(doing * 100.0 / target),donViTang));
                            adapterHome.notifyDataSetChanged();
                        }
                        else
                        {
                            arrayListHome.set(indexItem, new ListviewHomeTest(habitId,nameHabit,time,done,(int) Math.ceil(doing * 100.0 / target),donViTang));
                            adapterHome.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home_Activity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public int calculateTarget(double target, String period)
    {
        int result = 0;
        if(period.equals("Day"))
            return (int)target;
        else if(period.equals("Week"))
            return (int) Math.ceil(target * 1.0 / 7);
        else if (period.equals("Month"))
            return (int) Math.ceil(target * 1.0 / 30);
        return result;
    }
    public double getHistoryData(DataSnapshot snapshot)
    {
        double result = 0;
        for(DataSnapshot timeSnapshot : snapshot.getChildren())
        {
            String time = timeSnapshot.getKey();
            if(isToday(time))
                result += timeSnapshot.getValue(Double.class);
        }
        return result;
    }
    public boolean isToday(String time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
        LocalDateTime parsedDateTime = LocalDateTime.parse(time, formatter);
        return parsedDateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
    public int checkHabitNotInList(String id)
    {
        for(int i = 0; i < arrayListHome.size(); i++)
        {
            if(id.equals(arrayListHome.get(i).getHabitId()))
                return i;
        }
        return -1;
    }

    public void getReminder() {
        String idUser = getIntent().getStringExtra("idTaiKhoan");
        getConnection(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot habitSnapshot : snapshot.getChildren()) {
                        String habitId = habitSnapshot.getKey();

                        String time = habitSnapshot.child("ThoiGianNhacNho").getValue(String.class);
                        String reminder = habitSnapshot.child("LoiNhacNho").getValue(String.class);

                        Log.d("ReminderDebug", "Time: " + time + ", Reminder: " + reminder); // In thông tin debug

                        Calendar cal_alarm = convertTimeStringToCalendar(time);
                        int hour = cal_alarm.get(Calendar.HOUR_OF_DAY);
                        int minute = cal_alarm.get(Calendar.MINUTE);

                        // Lấy thời gian và thông báo từ Firebase

                        // Đặt hẹn giờ và gửi thông báo
                        setTimer(hour, minute, reminder);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home_Activity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTimer(int hour, int minute, String message) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Date date = new Date();

        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY, hour);
        cal_alarm.set(Calendar.MINUTE, minute);
        cal_alarm.set(Calendar.SECOND, 0);

        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }

        Intent i = new Intent(Home_Activity.this, MyBroadcastReceiver.class);
        i.putExtra("message", message); // Đưa thông báo vào intent

        // Sử dụng một requestCode duy nhất cho mỗi PendingIntent
        int requestCode = generateRequestCode(); // Phương thức để tạo requestCode duy nhất
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Home_Activity.this, requestCode, i, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
    }
    private int generateRequestCode() {
        return (int) System.currentTimeMillis(); // Sử dụng thời gian hiện tại làm requestCode
    }
    private Calendar convertTimeStringToCalendar(String timeString) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mma", Locale.getDefault());
        try {
            Date date = sdf.parse(timeString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
    private void notification() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Alarm Reminders";
            String description = "Hey, Wake Up!!";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel  = new NotificationChannel("Notify", name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}