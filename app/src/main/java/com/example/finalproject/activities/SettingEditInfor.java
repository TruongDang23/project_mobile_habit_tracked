package com.example.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.model.Account;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SettingEditInfor extends AppCompatActivity {

    EditText edtName, edtPhone, edtDate, edtEmailAddress;
    Button btnSubmit, btnCancel;
    private String ID = null;
    private String Username = null;
    private String Password = null;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private RadioGroup radioGroup;
    String strAvatar;
    private RadioButton maleRadioButton,femaleRadioButton;
    private ImageView imbtnDate;

    ImageView Avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_infor);
        doFormWidget();
        showInfor();
    }
    private void doFormWidget(){
        edtName = findViewById(R.id.editTextText);
        edtDate = findViewById(R.id.editTextDate);
        edtDate.setEnabled(false);
        edtPhone = findViewById(R.id.editTextPhone);
        edtEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        Avatar = findViewById(R.id.image);
        maleRadioButton = findViewById(R.id.radioButton);
        femaleRadioButton = findViewById(R.id.radioButton2);
        imbtnDate = findViewById(R.id.imbtnDate);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                Account account = (Account) bundle.getSerializable("user_account");

                Intent intent = new Intent(SettingEditInfor.this, Setting.class);
                // Tạo Bundle và đặt đối tượng Account vào Bundle
                Bundle b = new Bundle();
                b.putSerializable("user_account",account);
                intent.putExtra("idTaiKhoan", ID);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        imbtnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SettingEditInfor.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // Xử lý khi người dùng chọn ngày
                                // Ví dụ: Đặt giá trị ngày vào văn bản của nút
                                String selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", day, month + 1, year);
                                edtDate.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth); // Thiết lập giá trị mặc định cho DatePickerDialog
                datePickerDialog.show();
            }
        });
    }

    private void showInfor(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Lấy đối tượng Account từ Bundle
            Account account = (Account) bundle.getSerializable("user_account");
            if (account != null) {
                strAvatar = account.getAvatar();
                Username = account.getUsername();
                Password = account.getPassword();
                String sex = account.getSex();
                if (sex.equals("Nam")) {
                    maleRadioButton.setChecked(true);
                } else if (sex.equals("Nữ")) {
                    femaleRadioButton.setChecked(true);
                }
                edtName.setText(account.getName());
                edtDate.setText(account.getBorn());
                edtPhone.setText(account.getPhone());
                edtEmailAddress.setText(account.getGmail());
                Glide.with(this).load(account.getAvatar()).into(Avatar);
            }
        }
    }
    private void connectionFirebase(){
        ID = getIntent().getStringExtra("idTaiKhoan");
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Habit_Tracker").child("Tai_Khoan").child(ID);
    }

    private void getData(){
        connectionFirebase();
        String currentGender = null;
        String phone = edtPhone.getText().toString();
        String born = edtDate.getText().toString();
        if (maleRadioButton.isChecked()) {
            currentGender = "Nam";
        } else if (femaleRadioButton.isChecked()) {
            currentGender = "Nữ";
        }

        Account editAccount = new Account(strAvatar, currentGender, edtEmailAddress.getText().toString(), edtName.getText().toString(), Password, born, phone, Username);
        ref.setValue(editAccount);


        Intent intent = new Intent(this, Setting.class);
        // Tạo Bundle và đặt đối tượng Account vào Bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable("user_account", editAccount);
        intent.putExtra("idTaiKhoan", ID);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}