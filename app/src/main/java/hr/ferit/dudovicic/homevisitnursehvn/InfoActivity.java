package hr.ferit.dudovicic.homevisitnursehvn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import hr.ferit.dudovicic.homevisitnursehvn.AccountActivity.LoginActivity;
import android.annotation.SuppressLint;


public class InfoActivity extends AppCompatActivity {

    TextView patient_name_txt, patient_surname_txt, patient_oib_txt, patient_blood_type_txt, patient_history_txt, patient_address_txt, visit_date_txt, visit_hour_txt, visit_therapy_txt, visit_notes_txt;
    private Button btn_find, btn_write, btn_gallery, btn_take_a_photo;
    private FirebaseAuth auth;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        patient_name_txt = (TextView) findViewById(R.id.patient_name);
        patient_surname_txt = (TextView) findViewById(R.id.patient_surname);
        patient_oib_txt = (TextView) findViewById(R.id.patient_oib);
        patient_blood_type_txt = (TextView) findViewById(R.id.patient_blood_type);
        patient_history_txt = (TextView) findViewById(R.id.patient_history);
        patient_address_txt = (TextView) findViewById(R.id.patient_address);
        visit_date_txt = (TextView) findViewById(R.id.visit_date_txt);
        visit_hour_txt = (TextView) findViewById(R.id.visit_hour_txt);
        visit_therapy_txt = (TextView) findViewById(R.id.visit_therapy);
        visit_notes_txt = (TextView) findViewById(R.id.visit_notes);

        Intent intent = getIntent();
        String oib = intent.getStringExtra("EXTRA_MESSAGE");

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            myref = FirebaseDatabase.getInstance().getReference().child("patients").child(auth.getCurrentUser().getUid());
        }

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        myref.child(oib).addValueEventListener(new ValueEventListener(){

            @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String visit_date = dataSnapshot.child("visit_date").getValue().toString();
                String visit_hour = dataSnapshot.child("visit_hour").getValue().toString();
                String patient_name = dataSnapshot.child("name").getValue().toString();
                String patient_surname = dataSnapshot.child("surname").getValue().toString();
                String patient_address = dataSnapshot.child("address").getValue().toString();
                String patient_blood_type = dataSnapshot.child("blood_type").getValue().toString();
                String patient_therapy = dataSnapshot.child("visit_therapy").getValue().toString();
                String patient_notes = dataSnapshot.child("visit_notes").getValue().toString();
                String patient_history = dataSnapshot.child("history_of_illness").getValue().toString();
                String patient_oib = dataSnapshot.child("oib").getValue().toString();

                    visit_date_txt.setText("For:" + visit_date);
                    visit_hour_txt.setText("in:" + visit_hour);
                    patient_name_txt.setText(patient_name);
                    patient_surname_txt.setText(patient_surname);
                    patient_oib_txt.setText("OIB:" + patient_oib);
                    patient_address_txt.setText(patient_address);
                    patient_blood_type_txt.setText("Blood type:" + patient_blood_type);
                    visit_therapy_txt.setText("Therapy:" + patient_therapy);
                    visit_notes_txt.setText("Notes from last therapy:" + patient_notes);
                    patient_history_txt.setText("History:" + patient_history);

                }
            @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                finish();
            }
        }
    };

    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
