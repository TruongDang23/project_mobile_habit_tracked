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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private String idUser;
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

        idUser = getIntent().getStringExtra("idTaiKhoan");


        Calendar calendar = Calendar.getInstance();
        mPicker = findViewById(R.id.day_date_picker);
        mPicker.setStartDate(1, 3, 2024);
        mPicker.setEndDate(1, 3, 2025);
        currentDay = findViewById(R.id.txtCurrentDay);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        currentDay.setText(LocalDateTime.now().format(formatter));

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
                intent.putExtra("idTaiKhoan", idUser);
                Log.d("HabitID", arrayListHome.get(position).getHabitId());
                Log.d("Username", acc.getUsername());
                Log.d("idTaiKhoan", idUser);
                startActivity(intent);
            }
        });

        btnNew = findViewById(R.id.btnNew);
        btnNew.setOnClickListener(v -> {
            Intent intent = new Intent(Home_Activity.this, Create_habit.class);
            // Tạo Bundle và đặt đối tượng Account vào Bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_account", acc);
            intent.putExtra("idTaiKhoan", idUser);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        ibGraph = findViewById(R.id.ib_graph);
        ibGraph.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Progress_total.class);
            // Tạo Bundle và đặt đối tượng Account vào Bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_account", acc);
            i.putExtra("idTaiKhoan", idUser);
            i.putExtras(bundle);
            startActivity(i);
        });

        ibMusic = findViewById(R.id.ib_music);
        ibMusic.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, SongsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_account", acc);
            i.putExtra("idTaiKhoan", idUser);
            i.putExtras(bundle);
            startActivity(i);
        });

        ibClock = findViewById(R.id.ib_clock);
        ibClock.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Pomorodo.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_account", acc);
            i.putExtra("idTaiKhoan", idUser);
            i.putExtras(bundle);
            startActivity(i);
        });
        
        ibSettings = findViewById(R.id.ib_settings);
        ibSettings.setOnClickListener(v -> {
            Intent i = new Intent(Home_Activity.this, Setting.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_account", acc);
            i.putExtra("idTaiKhoan", idUser);
            i.putExtras(bundle);
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
        getConnection(idUser);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot habitSnapshot : snapshot.getChildren())
                    {
                        String status = habitSnapshot.child("TrangThai").getValue(String.class);

                        if(!status.equals("Đã xóa"))
                        {
                            String habitId = habitSnapshot.getKey();
                            int indexItem = checkHabitNotInList(habitId);

                            String nameHabit = habitSnapshot.child("Ten").getValue(String.class);
                            String section = habitSnapshot.child("ThoiDiem").getValue(String.class);
                            String time = habitSnapshot.child("ThoiGianNhacNho").getValue(String.class);
                            String donVi = habitSnapshot.child("DonVi").getValue(String.class);

                            double target = habitSnapshot.child("MucTieu").getValue(Double.class);
                            double maxVol = target; //Được sử dụng để check trạng thái Đã hoàn thành
                            double doing = getHistoryData(habitSnapshot.child("ThoiGianThucHien"));
                            String period = habitSnapshot.child("KhoangThoiGian").getValue(String.class);
                            target = calculateTarget(target, period);
                            double donViTang = habitSnapshot.child("DonViTang").getValue(Double.class);
                            String doingStr = String.format("%.1f", doing);
                            String targetStr = String.format("%.1f",target);
                            String done;
                            String dateEnd = habitSnapshot.child("ThoiGianKetThuc").getValue(String.class);

                            if(status.equals("Đã hoàn thành"))
                            {
                                doing = target; //Vì đã hoàn thành nên tiến độ thực hiện luôn luôn bằng target
                                done = targetStr + "/" + targetStr + " " + donVi; //Dữ liệu thay vì doingStr thì dùng targetStr cho tiện
                            }
                            else
                            {
                                done = doingStr + "/" + targetStr + " " + donVi;
                            }

                            if(todayIsExpire(dateEnd) && isMaxVol(habitSnapshot.child("ThoiGianThucHien"),maxVol))
                            {
                                ref.child(habitId).child("TrangThai").setValue("Đã hoàn thành");
                            }
                            else ref.child(habitId).child("TrangThai").setValue("Đang thực hiện");

                            if(indexItem == -1)
                            {
                                arrayListHome.add(new ListviewHomeTest(habitId,nameHabit,section,time,done,(int) Math.ceil(doing * 100.0 / target),donViTang,status,doing));
                                adapterHome.notifyDataSetChanged();
                            }
                            else
                            {
                                arrayListHome.set(indexItem, new ListviewHomeTest(habitId,nameHabit,section,time,done,(int) Math.ceil(doing * 100.0 / target),donViTang,status,doing));
                                adapterHome.notifyDataSetChanged();
                            }
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
    public double calculateTarget(double target, String period)
    {
        double result = 0.0;
        BigDecimal format;
        if(period.equals("Day"))
            result = target;
        else if(period.equals("Week"))
            result = (target/ 7);
        else if (period.equals("Month"))
            result = (target/ 30);
        format = new BigDecimal(result).setScale(1, RoundingMode.HALF_UP);
        result = format.doubleValue();
        return result;
    }
    public double getHistoryData(DataSnapshot snapshot)
    {
        double result = 0.0f;
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
    public boolean todayIsExpire(String time)
    {
        boolean result = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
            result = dateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }
        return result;
    }
    public boolean isMaxVol(DataSnapshot snapshot, Double maxVol)
    {
        double result = 0;
        for(DataSnapshot data : snapshot.getChildren())
            result += data.getValue(Double.class);
        return result >= maxVol;
    }
    public void getReminder() {
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