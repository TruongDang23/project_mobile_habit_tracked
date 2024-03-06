package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
Button test,test2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test=findViewById(R.id.button);
        test2=findViewById(R.id.button2);
        test.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        setContentView(R.layout.activity_setting);
    }
});
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.edit_infor);
            }
        });
        // This is a comment
    }
}