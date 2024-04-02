package com.example.finalproject.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.model.Account;
import com.example.finalproject.model.Habit;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Edit_habit extends AppCompatActivity {
    private FirebaseDatabase database;
    private String isTimeRangeSelected = null;
    private int defaultColor = Color.parseColor("#d0ebff");
    private int selectedColor = Color.parseColor("#187BCE");
    final int[] clickCount = {0};
    private String period = null;

    private boolean isDaySelected = false;
    private boolean isWeekSelected = false;
    private boolean isMonthSelected = false;

    private DatabaseReference ref;
    private Button btnComplete;
    private String idTaiKhoan;
    private String idThoiQuen;
    private EditText editName, editDescription, editIncrease, editReminderMessage, editNumber;
    private Button btnMorning, btnAfternoon, btnEvening, btnAnytime, btntime;
    private Button btnDonVi, btnDay, btnWeek, btnMonth;
    private Button btnBatDau, btnKetThuc;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_habit);
        doFormWidget();
        showInfor();
        handleGoalPeriod();
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHabit();
            }
        });
    }
    private void doFormWidget(){
        idTaiKhoan = getIntent().getStringExtra("idTaiKhoan");
        idThoiQuen=getIntent().getStringExtra("idThoiQuen");
        btnComplete=findViewById(R.id.btnComplete);
        editName = findViewById(R.id.editName);
        editDescription = findViewById(R.id.editDescription);
        editIncrease = findViewById(R.id.editIncrease);
        editReminderMessage = findViewById(R.id.editReminderMessage);
        editNumber = findViewById(R.id.editNumber);
        btntime=findViewById(R.id.btntime);
        btnBatDau=findViewById(R.id.btnBatDau);
        btnKetThuc=findViewById(R.id.btnKetThuc);
        btnDonVi=findViewById(R.id.btnDonVi);
       // showInfor();
        habitTerm();
        handleReminder();
        timeRange();
        handleGoal();
        handleGoalPeriod();

    }
    private void showInfor(){
        Bundle bundlehabit = getIntent().getExtras();
        if (bundlehabit != null) {
            // Lấy đối tượng Habit từ Bundle
                Habit habit = (Habit) bundlehabit.getSerializable("habit");
                    editName.setText(habit.getTen());
                    editDescription.setText(habit.getMoTa());
                    editIncrease.setText(Double.toString(habit.getDonViTang()));
                    editReminderMessage.setText(habit.getLoiNhacNho());
                    editNumber.setText(Double.toString(habit.getMucTieu()));
                    String timerange=habit.getThoiDiem();
                    if(timerange.equals("Morning")){btnMorning.setBackgroundColor(selectedColor);isTimeRangeSelected = "Morning";}
                    else if(timerange.equals("Afternoon")){btnAfternoon.setBackgroundColor(selectedColor);isTimeRangeSelected = "Afternoon";}
                    else if (timerange.equals("Evening")) {btnEvening.setBackgroundColor(selectedColor);isTimeRangeSelected = "Evening";}
                    else {btnAnytime.setBackgroundColor(selectedColor);isTimeRangeSelected = "Anytime";}
                    btntime.setText(habit.getThoiGianNhacNho());
                    btnKetThuc.setText(habit.getThoiGianKetThuc());
                    btnBatDau.setText(habit.getThoiGianBatDau());
                    btnDonVi.setText(habit.getDonVi());
                    String khoangthoigian=habit.getKhoangThoiGian();
                    if(khoangthoigian.equals("Day")){btnDay.setBackgroundColor(selectedColor);isDaySelected = true;}
                    else if(khoangthoigian.equals("Week")){btnWeek.setBackgroundColor(selectedColor);isWeekSelected = true;}
                    else if(khoangthoigian.equals("Month")) {btnMonth.setBackgroundColor(selectedColor);isMonthSelected = true;}

        }
    }

    private void changeHabit( ){
        timeRange();
        handleGoalPeriod();
        idTaiKhoan = getIntent().getStringExtra("idTaiKhoan");
        idThoiQuen=getIntent().getStringExtra("idThoiQuen");
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Habit_Tracker").child("Du_Lieu").child(idTaiKhoan).child(idThoiQuen);
        String name = editName.getText().toString();
        String description = editDescription.getText().toString();
        String increaseStr = editIncrease.getText().toString();
        double increase = Double.parseDouble(increaseStr);
        String timeRange = isTimeRangeSelected;
        String reminder = btntime.getText().toString();
        String reminderMessage = editReminderMessage.getText().toString();
        String start = btnBatDau.getText().toString();
        String end = btnKetThuc.getText().toString();
        double goal = Double.parseDouble(editNumber.getText().toString());
        String unit = btnDonVi.getText().toString();
        period = determinePeriod();

        ref.child("Ten").setValue(name);
        ref.child("DonVi").setValue(unit);
        ref.child("DonViTang").setValue(increase);
        ref.child("KhoangThoiGian").setValue(period);
        ref.child("LoiNhacNho").setValue(reminderMessage);
        ref.child("MoTa").setValue(description);
        ref.child("MucTieu").setValue(goal);
        ref.child("ThoiDiem").setValue(timeRange);
        ref.child("ThoiGianBatDau").setValue(start);
        ref.child("ThoiGianKetThuc").setValue(end);
        ref.child("ThoiGianNhacNho").setValue(reminder);

        Intent intent = new Intent(Edit_habit.this, Home_Activity.class);
        Bundle bundlehabit = new Bundle();
        intent.putExtra("idTaiKhoan",idTaiKhoan);
        intent.putExtras(bundlehabit);
        startActivity(intent);
    }

    private void timeRange() {
        btnMorning = findViewById(R.id.btnMorning);
        btnAfternoon = findViewById(R.id.btnAfternoon);
        btnEvening = findViewById(R.id.btnEvening);
        btnAnytime = findViewById(R.id.btnAnytime);


        // Thiết lập sự kiện click cho từng button
        btnMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đặt màu cho nút Morning và đặt lại màu cho các nút khác
                btnMorning.setBackgroundColor(selectedColor);
                btnAfternoon.setBackgroundColor(defaultColor);
                btnEvening.setBackgroundColor(defaultColor);
                btnAnytime.setBackgroundColor(defaultColor);

                isTimeRangeSelected = "Morning";
            }
        });

        btnAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đặt màu cho nút Afternoon và đặt lại màu cho các nút khác
                btnMorning.setBackgroundColor(defaultColor);
                btnAfternoon.setBackgroundColor(selectedColor);
                btnEvening.setBackgroundColor(defaultColor);
                btnAnytime.setBackgroundColor(defaultColor);

                isTimeRangeSelected = "Afternoon";
            }
        });

        btnEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đặt màu cho nút Evening và đặt lại màu cho các nút khác
                btnMorning.setBackgroundColor(defaultColor);
                btnAfternoon.setBackgroundColor(defaultColor);
                btnEvening.setBackgroundColor(selectedColor);
                btnAnytime.setBackgroundColor(defaultColor);

                isTimeRangeSelected = "Evening";
            }
        });

        btnAnytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đặt màu cho nút Anytime và đặt lại màu cho các nút khác
                btnMorning.setBackgroundColor(defaultColor);
                btnAfternoon.setBackgroundColor(defaultColor);
                btnEvening.setBackgroundColor(defaultColor);
                btnAnytime.setBackgroundColor(selectedColor);

                isTimeRangeSelected = "Anytime";
            }
        });
    }

    private void habitTerm() {
        btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnBatDau = findViewById(R.id.btnBatDau);
        btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Edit_habit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // Xử lý khi người dùng chọn ngày
                                // Ví dụ: Đặt giá trị ngày vào văn bản của nút
                                String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", day, month + 1, year);
                                btnBatDau.setText(selectedDate);
                                handleGoalPeriod();
                            }
                        }, year, month, dayOfMonth); // Thiết lập giá trị mặc định cho DatePickerDialog
                datePickerDialog.show();
            }
        });
        btnKetThuc = findViewById(R.id.btnKetThuc);
        btnKetThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Edit_habit.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // Xử lý khi người dùng chọn ngày
                                // Ví dụ: Đặt giá trị ngày vào văn bản của nút
                                String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", day, month + 1, year);
                                btnKetThuc.setText(selectedDate);
                                handleGoalPeriod();
                            }
                        }, year, month, dayOfMonth); // Thiết lập giá trị mặc định cho DatePickerDialog
                datePickerDialog.show();
            }
        });
    }

    private void handleReminder() {
        btntime = findViewById(R.id.btntime);
        btntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(Edit_habit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        SimpleDateFormat displayFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
                        Date date = null;
                        try {
                            date = new SimpleDateFormat("H:mm", Locale.getDefault()).parse(selectedTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String formattedTime = displayFormat.format(date);
                        btntime.setText(formattedTime.toUpperCase()); // Để chuyển đổi thành chữ hoa

                    }
                }, hour, minute, true); // Thiết lập giá trị mặc định cho TimePickerDialog
                timePickerDialog.show();
            }
        });
    }

    private long calculateDateDifference(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date1 = sdf.parse(startDate);
            Date date2 = sdf.parse(endDate);

            long difference = date2.getTime() - date1.getTime();
            // Chuyển đổi khoảng cách từ milliseconds sang days
            long daysDifference = difference / (1000 * 60 * 60 * 24);
            return daysDifference;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Trả về -1 nếu có lỗi xảy ra
        }
    }

    private void handleGoalPeriod() {
        String startDate = btnBatDau.getText().toString();
        String endDate = btnKetThuc.getText().toString();
        long difference = calculateDateDifference(startDate, endDate);
        btnDay = findViewById(R.id.btnDay);
        btnWeek = findViewById(R.id.btnWeek);
        btnMonth = findViewById(R.id.btnMonth);


        if (difference >= 30) {
            // Khoảng cách lớn hơn hoặc bằng 30 ngày: cho phép chọn tất cả các phương thức
            btnDay.setEnabled(true);
            btnWeek.setEnabled(true);
            btnMonth.setEnabled(true);
        } else if (difference >= 7) {
            btnDay.setEnabled(true);
            btnWeek.setEnabled(true);
            btnMonth.setEnabled(false);
        } else {
            btnDay.setEnabled(true);
            btnWeek.setEnabled(false);
            btnMonth.setEnabled(false);
        }
        btnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDay.setBackgroundColor(selectedColor);
                btnWeek.setBackgroundColor(defaultColor);
                btnMonth.setBackgroundColor(defaultColor);

                isDaySelected = true;
                isWeekSelected = false;
                isMonthSelected = false;

            }
        });
        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDay.setBackgroundColor(defaultColor);
                btnWeek.setBackgroundColor(selectedColor);
                btnMonth.setBackgroundColor(defaultColor);

                isDaySelected = false;
                isWeekSelected = true;
                isMonthSelected = false;
            }
        });
        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDay.setBackgroundColor(defaultColor);
                btnWeek.setBackgroundColor(defaultColor);
                btnMonth.setBackgroundColor(selectedColor);

                isDaySelected = false;
                isWeekSelected = false;
                isMonthSelected = true;
            }
        });
    }
    private String determinePeriod() {
        if (isDaySelected) {
            return "Day";
        } else if (isWeekSelected) {
            return "Week";
        } else  {
            return "Month";
        }
    }
    private void handleGoal(){
        btnDonVi = findViewById(R.id.btnDonVi);

        btnDonVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount[0]++;
                // Dựa vào giá trị của biến đếm, thay đổi văn bản của nút
                switch ( clickCount[0] % 3) {
                    case 0:
                        btnDonVi.setText("km");
                        break;
                    case 1:
                        btnDonVi.setText("pages");
                        break;
                    case 2:
                        btnDonVi.setText("hours");
                        break;
                }
            }
        });
    }
}
