package com.example.smartdoctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_LogInMain = findViewById(R.id.btnLogInMain);
        btn_LogInMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginBy.class));
            }
        });

        //Action button Register
        Button btn_registerMain = findViewById(R.id.btnRegisterMain);
        btn_registerMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign = new Intent(MainActivity.this, CheckUniqueCode.class);
                startActivity(sign);
            }
        });

        //Action button NFC TEST
        Button buttonNFC = findViewById(R.id.btnNfc);
        buttonNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent nfc = new Intent(MainActivity.this, NFCActivity.class);
//                startActivity(nfc);
            }
        });
    }

    //user log out
    private void HospitalsList(){
        startActivity(new Intent(MainActivity.this, HospitalsList.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_smart_doctor; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (item.getItemId() == R.id.contactUs) {
            HospitalsList();
        }
        return super.onOptionsItemSelected(item);
    }
}