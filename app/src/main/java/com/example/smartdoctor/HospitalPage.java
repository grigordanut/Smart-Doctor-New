package com.example.smartdoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class HospitalPage extends AppCompatActivity {

    //Access Hospitals database
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    //Declare variables
    private TextView tVWelcomeHospital;

    private Button btn_HospDocList, btn_HospPatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_page);

        Objects.requireNonNull(getSupportActionBar()).setTitle("HOSPITALS: main page");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //retrieve data from Hospitals database
        databaseReference = FirebaseDatabase.getInstance().getReference("Hospitals");

        //initialise the variables
        tVWelcomeHospital = findViewById(R.id.tvWelcomeHospital);

        btn_HospDocList = findViewById(R.id.btnHospDocList);
        btn_HospPatList = findViewById(R.id.btnHospPatList);

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve data from database
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Hospitals user_Hosp = postSnapshot.getValue(Hospitals.class);

                    assert user_Hosp != null;
                    assert firebaseUser != null;
                    if (firebaseUser.getUid().equals(postSnapshot.getKey())) {
                        tVWelcomeHospital.setText("Welcome to: " + user_Hosp.getHosp_Name() + " hospital");

                        btn_HospDocList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HospitalPage.this, HospitalDoctorsList.class);
                                intent.putExtra("HOSPKey", firebaseUser.getUid());
                                startActivity(intent);
                            }
                        });

                        btn_HospPatList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HospitalPage.this, HospitalPatientsList.class);
                                intent.putExtra("HOSPKey", firebaseUser.getUid());
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HospitalPage.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hospital_page, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.hospital_logOut) {
            firebaseAuth.signOut();
            startActivity(new Intent(HospitalPage.this, MainActivity.class));
            finish();
        }

        if (item.getItemId() == R.id.hospital_editProfile) {
            startActivity(new Intent(HospitalPage.this, HospitalEditProfile.class));
            finish();
        }

        if (item.getItemId() == R.id.hospital_changeEmail) {
            startActivity(new Intent(HospitalPage.this, HospitalChangeEmail.class));
            finish();
        }

        if (item.getItemId() == R.id.hospital_changePassword) {
            startActivity(new Intent(HospitalPage.this, HospitalChangePassword.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}