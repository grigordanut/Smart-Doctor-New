package com.example.smartdoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DoctorEditProfile extends AppCompatActivity {

    //declare variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseRefUp;
    private ValueEventListener eventListener;

    private EditText newDocUniqueCode, newDocFirstName, newDocLastName, newDocPhone;
    private TextView tVUpDoctorProfile, tVUpDoctorHospName, tVNewDocEmail;
    private Button buttonNewDocSave;

    private String newDoc_UniqueCode, newDoc_FirstName, newDoc_LastName, newDoc_Phone;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Doctor details");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        newDocUniqueCode = findViewById(R.id.etNewDocUCode);
        newDocFirstName = findViewById(R.id.etNewDocFName);
        newDocLastName = findViewById(R.id.etNewDocLName);
        newDocPhone = findViewById(R.id.etNewDocFNumber);
        tVNewDocEmail = findViewById(R.id.tvNewDocEmail);

        tVNewDocEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDoctorEmailChangePlace();
            }
        });

        tVUpDoctorProfile = findViewById(R.id.tvUpDoctorProfile);
        tVUpDoctorHospName = findViewById(R.id.tvUpDoctorHospName);

        databaseReference = FirebaseDatabase.getInstance().getReference("Doctors");
        databaseRefUp = FirebaseDatabase.getInstance().getReference("Doctors");

        buttonNewDocSave = findViewById(R.id.btnNewDocSave);
        buttonNewDocSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDoctorDetails();
            }
        });
    }

    public void updateDoctorDetails() {

        progressDialog.setTitle("Updating doctor details!!");
        progressDialog.show();

        if (validateNewDoctorDetails()) {

            newDoc_UniqueCode = newDocUniqueCode.getText().toString().trim();
            newDoc_FirstName = newDocFirstName.getText().toString().trim();
            newDoc_LastName = newDocLastName.getText().toString().trim();
            newDoc_Phone = newDocPhone.getText().toString().trim();

            databaseRefUp.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        if (firebaseUser.getUid().equals(postSnapshot.getKey())) {
                            postSnapshot.getRef().child("doctor_UniqueCode").setValue(newDoc_UniqueCode);
                            postSnapshot.getRef().child("doctor_FirstName").setValue(newDoc_FirstName);
                            postSnapshot.getRef().child("doctor_LastName").setValue(newDoc_LastName);
                            postSnapshot.getRef().child("doctor_PhoneNumber").setValue(newDoc_Phone);
                        }
                    }

                    progressDialog.dismiss();

                    Toast.makeText(DoctorEditProfile.this, "The Doctor's details has been changed successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DoctorEditProfile.this, DoctorPage.class));
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DoctorEditProfile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public Boolean validateNewDoctorDetails() {

        boolean result = false;

        newDoc_UniqueCode = newDocUniqueCode.getText().toString().trim();
        newDoc_FirstName = newDocFirstName.getText().toString().trim();
        newDoc_LastName = newDocLastName.getText().toString().trim();
        newDoc_Phone = newDocPhone.getText().toString().trim();

        if (TextUtils.isEmpty(newDoc_UniqueCode)) {
            newDocUniqueCode.setError("Enter Doctor's unique code");
            newDocUniqueCode.requestFocus();
        }

        else if (TextUtils.isEmpty(newDoc_FirstName)) {
            newDocFirstName.setError("Enter Doctor's first name");
            newDocFirstName.requestFocus();
        }

        else if (TextUtils.isEmpty(newDoc_LastName)) {
            newDocLastName.setError("Enter Doctor's last name");
            newDocLastName.requestFocus();
        }

        else if (TextUtils.isEmpty(newDoc_Phone)) {
            newDocPhone.setError("Enter Doctor's phone number");
            newDocPhone.requestFocus();
        }

        else {
            result = true;
        }

        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDoctorData();
    }

    public void loadDoctorData() {
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //retrieve data from firebase database
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Doctors doc_Data = postSnapshot.getValue(Doctors.class);

                    if (firebaseUser.getUid().equals(postSnapshot.getKey())) {
                        assert doc_Data != null;
                        newDocUniqueCode.setText(doc_Data.getDoctor_UniqueCode());
                        newDocFirstName.setText(doc_Data.getDoctor_FirstName());
                        newDocLastName.setText(doc_Data.getDoctor_LastName());
                        newDocPhone.setText(doc_Data.getDoctor_PhoneNumber());
                        tVNewDocEmail.setText(doc_Data.getDoctor_Email());
                        tVUpDoctorProfile.setText("Edit doctor: " + doc_Data.getDoctor_FirstName() + " " + doc_Data.getDoctor_LastName());
                        tVUpDoctorHospName.setText(doc_Data.getDoctor_HospName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DoctorEditProfile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void alertDoctorEmailChangePlace() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("The Email Address cannot be change here.\nPlease use Change Email option!")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doctor_edit_profile, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        if (item.getItemId() == R.id.doctorEditProfile_goBack) {
            startActivity(new Intent(DoctorEditProfile.this, DoctorPage.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}