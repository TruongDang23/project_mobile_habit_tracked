package com.example.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        edtPhone = findViewById(R.id.editTextPhone);
        edtEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        Avatar = findViewById(R.id.image);
        maleRadioButton = findViewById(R.id.radioButton);
        femaleRadioButton = findViewById(R.id.radioButton2);

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

                Date born = account.getBorn();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String strBorn = dateFormat.format(born);
                edtDate.setText(strBorn);

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

        if (maleRadioButton.isChecked()) {
            currentGender = "Nam";
        } else if (femaleRadioButton.isChecked()) {
            currentGender = "Nữ";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date ngaySinhDate = null;
        try {
            // Chuyển đổi chuỗi thành đối tượng Date
            ngaySinhDate = dateFormat.parse(edtDate.getText().toString());

            // Cắt bỏ thông tin giờ, phút và giây từ đối tượng Date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ngaySinhDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            ngaySinhDate = calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Account editAccount = new Account(strAvatar, currentGender, edtEmailAddress.getText().toString(), edtName.getText().toString(), Password, ngaySinhDate, phone, Username);
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