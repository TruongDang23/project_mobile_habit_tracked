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

public class ProgressActivity extends AppCompatActivity {
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    private Account acc = new Account();

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
        TextInputEditText edt_reminser_message = (TextInputEditText) dialog.findViewById(R.id.edt_reminder_message);
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
                    String ten = snapshot.child("Ten").getValue(String.class);
                    Log.d("Ten", ten);
                    edt_name.setText(ten);
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

}