package hr.ferit.dudovicic.homevisitnursehvn;

import hr.ferit.dudovicic.homevisitnursehvn.AccountActivity.LoginActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button signOut;
    private TextView email;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private DatabaseReference myref;
    private TextView today;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        email = (TextView) findViewById(R.id.useremail);

        String date_n = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        today = (TextView) findViewById(R.id.today);
        today.setText("Your visits for: " + date_n);

        recyclerView = (RecyclerView) findViewById(R.id.rvPatients);
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (auth.getCurrentUser() != null) {
           myref = FirebaseDatabase.getInstance().getReference().child("patients").child(auth.getCurrentUser().getUid());
        }

        updateUI();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        signOut = (Button) findViewById(R.id.sign_out);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    private void updateUI() {
        if (auth.getCurrentUser() != null){
            Log.i("MainActivity", "auth != null");
        } else {
            Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("MainActivity", "auth == null");
        }
    }

    public static class PatientItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;
        TextView visit_date_txt;
        TextView visit_hour_txt;
        TextView patient_name_txt;
        TextView patient_surname_txt;
        TextView patient_oib_txt;
        CardView noteCard;

        public PatientItemViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            visit_date_txt = (TextView) itemView.findViewById(R.id.visit_date_txt);
            visit_hour_txt = (TextView) itemView.findViewById(R.id.visit_hour_txt);
            patient_name_txt = (TextView) itemView.findViewById(R.id.patient_name);
            patient_surname_txt = (TextView) itemView.findViewById(R.id.patient_surname);
            patient_oib_txt = (TextView) itemView.findViewById(R.id.patient_oib);
            noteCard = mView.findViewById(R.id.patient_card);
            noteCard.setOnClickListener(this);
        }

        private void Layout_hide() {
            if(itemView.getVisibility() != View.GONE) itemView.setVisibility(View.GONE);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            layoutParams.setMargins(0, 0 ,0 ,0);
            itemView.setLayoutParams(layoutParams);
        }

        public void setPatientsOrdered_visit(String visit_date) {
            visit_date_txt.setText("Date of visit: " + visit_date);
        }

        public void setPatientsLast_visit(String visit_hour) {visit_hour_txt.setText("Time of visit: " + visit_hour);}

        public void setPatientsName(String patient_name) {
            patient_name_txt.setText("Patients name: " + patient_name);
        }

        public void setPatientsSurname(String patient_surname) {
            patient_surname_txt.setText("Patients surname: " + patient_surname);
        }

        public void setPatientsOib(String patient_oib) {
            patient_oib_txt.setText(patient_oib);
        }

        public String getPatientsOib() {
            String patient_oib = patient_oib_txt.getText().toString();
            return patient_oib;
        }


        @Override
        public void onClick(View v) {

            String patient_oib = getPatientsOib();

            Intent intent = new Intent(mView.getContext(), InfoActivity.class);

            intent.putExtra("EXTRA_MESSAGE", patient_oib);

            mView.getContext().startActivity(intent);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {
        email.setText("Hello, " + user.getEmail());
    }

    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {
                setDataToView(user);
            }
        }
    };

    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

        FirebaseRecyclerAdapter<patients, PatientItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<patients, PatientItemViewHolder>(

                patients.class,
                R.layout.patient_item,
                PatientItemViewHolder.class,
                myref

        ) {
            @Override
            protected void populateViewHolder(final PatientItemViewHolder viewHolder, patients model, int position) {
                String patientId = getRef(position).getKey();

                myref.child(patientId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String visit_date = dataSnapshot.child("visit_date").getValue().toString();
                        String visit_hour = dataSnapshot.child("visit_hour").getValue().toString();
                        String patient_name = dataSnapshot.child("name").getValue().toString();
                        String patient_surname = dataSnapshot.child("surname").getValue().toString();
                        String patient_oib = dataSnapshot.child("oib").getValue().toString();
                        String date_n = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        if (visit_date.equals(date_n)) {
                            viewHolder.setPatientsOrdered_visit(visit_date);
                            viewHolder.setPatientsLast_visit(visit_hour);
                            viewHolder.setPatientsName(patient_name);
                            viewHolder.setPatientsSurname(patient_surname);
                            viewHolder.setPatientsOib(patient_oib);
                        }
                        else{
                            viewHolder.Layout_hide();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
