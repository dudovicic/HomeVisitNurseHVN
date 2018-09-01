package hr.ferit.dudovicic.homevisitnursehvn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class WriteActivity extends AppCompatActivity {

    private EditText visit_date_edit, visit_hour_edit, visit_therapy_edit, visit_notes_edit;
    private Button btn_save;
    private FirebaseAuth auth;
    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        visit_date_edit = (EditText) findViewById(R.id.date_edt);
        visit_hour_edit = (EditText) findViewById(R.id.hour_edt);
        visit_therapy_edit = (EditText) findViewById(R.id.therapy_edt);
        visit_notes_edit = (EditText) findViewById(R.id.notes_edt);

        btn_save = (Button) findViewById(R.id.btn_save);
        Intent intent = getIntent();
        String pat_oib = intent.getStringExtra("EXTRA_MESSAGE");

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            myref = FirebaseDatabase.getInstance().getReference().child("patients").child(auth.getCurrentUser().getUid()).child(pat_oib);
        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            try {
                String date_edited = visit_date_edit.getText().toString();
                String hour_edited = visit_hour_edit.getText().toString();
                String therapy_edited = visit_therapy_edit.getText().toString();
                String notes_edited = visit_notes_edit.getText().toString();

                myref.child("visit_date").setValue(String.valueOf(date_edited));
                myref.child("visit_hour").setValue(String.valueOf(hour_edited));
                myref.child("visit_therapy").setValue(String.valueOf(therapy_edited));
                myref.child("visit_notes").setValue(String.valueOf(notes_edited));

                Toast.makeText(getApplicationContext(), "Data inserted!", Toast.LENGTH_LONG).show();
                } catch (Exception e)
                {
                Toast.makeText(getApplicationContext(), "Data can't be insert!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

