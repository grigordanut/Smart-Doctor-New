package com.example.smartdoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_RegisterMain = findViewById(R.id.btnRegisterMain);
        btn_RegisterMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_Reg = new Intent(MainActivity.this, CheckUniqueCode.class);
                startActivity(intent_Reg);
            }
        });
    }
}