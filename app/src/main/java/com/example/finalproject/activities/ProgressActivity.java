package com.example.finalproject.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.Account;
import com.example.finalproject.ui.MyPagerProgressAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ProgressActivity extends AppCompatActivity {
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    private Account acc = new Account();
    private int[] perfectArr = new int[32];
    TextView txtDIM, txtTTD, txtVTT, txtCS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            acc = (Account) b.getSerializable("user_account");
        }

        String idHabit = getIntent().getStringExtra("idThoiQuen");
        String idTaiKhoan = getIntent().getStringExtra("idTaiKhoan");

        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        Button btnInfor = (Button) findViewById(R.id.btnInfor);

        // Lấy thông tin thói quen
        getDetailHabit(idHabit, idTaiKhoan);

        TabLayout tabLayout = findViewById(R.id.tabLayoutProgress);
        TabItem tabDay = findViewById(R.id.tabDay);
        TabItem tabWeek = findViewById(R.id.tabWeek);
        TabItem tabMonth = findViewById(R.id.tabMonth);

        ViewPager viewPagerProgress = findViewById(R.id.viewpagerProgress);

        MyPagerProgressAdapter paperProgressAdapter = new MyPagerProgressAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPagerProgress.setAdapter(paperProgressAdapter);

        // Dung cho viec chuyen tab
        tabLayout.setupWithViewPager(viewPagerProgress);

        //Xóa thói quen
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteHabit();
            }
        });
        btnInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inforHabit(idHabit, idTaiKhoan);
            }
        });
    }

    public void getDetailHabit(String idHabit, String idTaiKhoan) {
        TextView tvDetail = (TextView) findViewById(R.id.tvDetail);
        txtDIM = (TextView) findViewById(R.id.tvDoneinMonth);
        txtTTD = (TextView) findViewById(R.id.tvTotalDone);
        txtVTT = (TextView) findViewById(R.id.tvVolTotal);
        txtCS = (TextView) findViewById(R.id.tvCurrentStreak);

        getConnection(idTaiKhoan, idHabit);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    try
                    {
                        //Reset array
                        Arrays.fill(perfectArr,0);

                        //Lấy tên thói quen
                        String ten = snapshot.child("Ten").getValue(String.class);
                        tvDetail.setText(ten);

                        //Lấy Total Done
                        int totalDone = getTotalDone(snapshot.child("ThoiGianThucHien"));
                        txtTTD.setText(totalDone + " Days");

                        //Lấy Vol Total
                        Double vol = getVolTotal(snapshot.child("ThoiGianThucHien"));
                        String fomatNumber = String.format("%.1f",vol);
                        String unit = snapshot.child("DonVi").getValue(String.class);
                        txtVTT.setText(fomatNumber + " " + unit);

                        //Lấy Done in month
                        int doneInMonth = getDoneInMonth(snapshot.child("ThoiGianThucHien"));
                        txtDIM.setText(doneInMonth + " Days");

                        //Lấy Current Streak
                        int streak = getCurrentSteak();
                        txtCS.setText(streak + " Days");
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(ProgressActivity.this, e.toString() + "\n", Toast.LENGTH_LONG).show();
                        Log.d("Error", e.toString());
                    }
                }
                else
                {
                    Toast.makeText(ProgressActivity.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProgressActivity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void inforHabit(String idHabit, String idTaiKhoan) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_infor);

        Log.d("idHabit", idHabit);
        Log.d("idUser", acc.getUsername());
        getConnection(idTaiKhoan, idHabit);


        EditText edt_name = (EditText) dialog.findViewById(R.id.edt_name);
        TextInputEditText edt_decription = (TextInputEditText) dialog.findViewById(R.id.edt_decription);
        EditText edt_unit_increase = (EditText) dialog.findViewById(R.id.edt_unit_increase);
        EditText edt_time_range = (EditText) dialog.findViewById(R.id.edt_time_range);
        EditText edt_reminder = (EditText) dialog.findViewById(R.id.edt_reminder);
        TextInputEditText edt_reminder_message = (TextInputEditText) dialog.findViewById(R.id.edt_reminder_message);
        EditText edt_start_term = (EditText) dialog.findViewById(R.id.edt_start_term);
        EditText edt_end_term = (EditText) dialog.findViewById(R.id.edt_end_term);
        EditText edt_goal = (EditText) dialog.findViewById(R.id.edt_goal);
        EditText edt_unit = (EditText) dialog.findViewById(R.id.edt_unit);
        EditText edt_period = (EditText) dialog.findViewById(R.id.edt_period);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);

        // Set data
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        Toast.makeText(ProgressActivity.this, "Đọc dữ liệu thành công", Toast.LENGTH_SHORT).show();
                        String ten = snapshot.child("Ten").getValue(String.class);
                        edt_name.setText(ten);
                        String moTa = snapshot.child("MoTa").getValue(String.class);
                        edt_decription.setText(moTa);
                        double donViTang = snapshot.child("DonViTang").getValue(Double.class);
                        String donViTangString = String.valueOf(donViTang);
                        edt_unit_increase.setText(donViTangString);
                        String thoiDiem = snapshot.child("ThoiDiem").getValue(String.class);
                        edt_time_range.setText(thoiDiem );
                        String nhacNho = snapshot.child("ThoiGianNhacNho").getValue(String.class);
                        edt_reminder.setText(nhacNho);
                        String loiNhacNho = snapshot.child("LoiNhacNho").getValue(String.class);
                        edt_reminder_message.setText(loiNhacNho);
                        String ngayBatDau = snapshot.child("ThoiGianBatDau").getValue(String.class);
                        edt_start_term.setText(ngayBatDau);
                        String ngayKetThuc = snapshot.child("ThoiGianKetThuc").getValue(String.class);
                        edt_end_term.setText(ngayKetThuc);
                        Long mucTieu = snapshot.child("MucTieu").getValue(Long.class);
                        edt_goal.setText(mucTieu.toString());
                        String donVi = snapshot.child("DonVi").getValue(String.class);
                        edt_unit.setText(donVi);
                        String khoangThoiGian = snapshot.child("KhoangThoiGian").getValue(String.class);
                        edt_period.setText(khoangThoiGian);

                    } catch (Exception e) {
                        Toast.makeText(ProgressActivity.this, e.toString() + "\n", Toast.LENGTH_LONG).show();
                        Log.d("Error", e.toString());
                    }

                }
                else {
                    Toast.makeText(ProgressActivity.this, "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProgressActivity.this, "Lỗi khi đọc dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });


        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    public void deleteHabit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProgressActivity.this);
        LayoutInflater inflater = getLayoutInflater();

        @SuppressLint("ResourceType")
        View dialogView = inflater.inflate(R.drawable.dialog_delete, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.dialogTitle);
        title.setText("Delete Habit?");

        TextView message = dialogView.findViewById(R.id.dialogMessage);
        message.setText("Do you want to remove this habit?" +
                "\nThis action cannot be undone");
        message.setGravity(Gravity.CENTER);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Xóa thối quen
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void getConnection(String idUser, String idHabit) {
        dataBase = FirebaseDatabase.getInstance();
        ref = dataBase.getReference("Habit_Tracker").child("Du_Lieu").child(idUser).child(idHabit);
    }
    public Double getVolTotal(DataSnapshot snapshot)
    {
        double result = 0;
        for(DataSnapshot timeSnapshot : snapshot.getChildren())
        {
            double vol = timeSnapshot.getValue(Double.class);
            result += vol;
        }
        return result;
    }
    public int getTotalDone(DataSnapshot snapshot)
    {
        int day = 0;
        String current = "";
        String previous = "";
        for(DataSnapshot timeSnapshot : snapshot.getChildren())
        {
            current = timeSnapshot.getKey();
            if(previous.isEmpty() || !isSameDay(current,previous))
            {
                day++;
                previous = current;
            }
        }
        return day;
    }
    public boolean isSameDay(String curr, String pre)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");

        LocalDateTime parsedCurr = LocalDateTime.parse(curr, formatter);
        LocalDateTime parsedPre = LocalDateTime.parse(pre, formatter);
        return parsedCurr.toLocalDate().equals(parsedPre.toLocalDate());
    }
    public int getDoneInMonth(DataSnapshot snapshot)
    {
        for(DataSnapshot timeSnapshot : snapshot.getChildren())
        {
            String time = timeSnapshot.getKey();
            if(isInCurrentMonth(time))
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
                LocalDateTime parsedTime = LocalDateTime.parse(time, formatter);

                perfectArr[parsedTime.getDayOfMonth()] = 1;
            }
        }
        return countDay();
    }
    public boolean isInCurrentMonth(String time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy h:mm:ssa");
        LocalDateTime parsedTime = LocalDateTime.parse(time, formatter);

        return (parsedTime.getMonth().equals(LocalDateTime.now().getMonth())) && (parsedTime.getYear() == LocalDateTime.now().getYear());
    }
    public int countDay()
    {
        int day = 0;
        for (int i = 0; i < perfectArr.length; i++)
        {
            if(perfectArr[i] == 1)
                day++;
        }
        return day;
    }
    public int getCurrentSteak()
    {
        int maxStreak = 0;
        int streak = 1;
        for(int i = 0; i < perfectArr.length - 1; i++)
        {
            if(perfectArr[i] == 1 && perfectArr[i+1] == 1)
                streak++;
            else if (perfectArr[i] == 0)
            {
                if(maxStreak <= streak)
                {
                    maxStreak = streak;
                    streak = 1;
                }
            }
        }
        return (maxStreak == 1) ? 0 : maxStreak;
    }
}