package com.example.smartdoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientPage extends AppCompatActivity {

    //Declare variables
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView textViewWelcomePatient;

    private Button buttonSeeMedRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_page);

        //initialise the variables
        textViewWelcomePatient = findViewById(R.id.tvWelcomePatient);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //retrieve data from database into text views
        databaseReference = FirebaseDatabase.getInstance().getReference("Patients");

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                //retrieve data from database
//                for (DataSnapshot dsPat : dataSnapshot.getChildren()) {
//                    FirebaseUser user_Pat = firebaseAuth.getCurrentUser();
//
//                    final Patients pat = dsPat.getValue(Patients.class);
//
//                    if (user_Pat.getEmail().equalsIgnoreCase(pat.patEmail_Address)){
//                        textViewWelcomePatient.setText("Welcome "+pat.getPatFirst_Name()+" "+pat.getPatLast_Name());
//
//                        buttonSeeMedRecord = (Button)findViewById(R.id.btnSeeMedRecord);
//                        buttonSeeMedRecord.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent rec_Pat = new Intent(PatientPage.this, MedicalRecordPatient.class);
//                                rec_Pat.putExtra("PATID", pat.getPatFirst_Name()+" "+pat.getPatLast_Name()+" "+pat.getPatUnique_Code());
//                                rec_Pat.putExtra("DOCID", pat.getPatDoc_ID());
//                                startActivity(rec_Pat);
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(PatientPage.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void changePassword(){
        finish();
        startActivity(new Intent(PatientPage.this,PatientChangePassword.class));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient_page, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.patient_logOut) {
            firebaseAuth.signOut();
            startActivity(new Intent(PatientPage.this, MainActivity.class));
            finish();
        }

        if(item.getItemId() == R.id.patient_changePassword) {
            startActivity(new Intent(PatientPage.this, PatientChangePassword.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}