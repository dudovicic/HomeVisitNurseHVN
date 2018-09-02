package hr.ferit.dudovicic.homevisitnursehvn;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import hr.ferit.dudovicic.homevisitnursehvn.AccountActivity.LoginActivity;

public class PhotoActivity extends AppCompatActivity {

    private Button mUploadBtn;
    private ImageView mImageView;

    private static final int CAMERA_REQUEST_CODE = 1;

    private StorageReference mStorage;

    private ProgressDialog mProgress;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                  Intent intent = getIntent();
                  String pati_oib = intent.getStringExtra("EXTRA_MESSAGE1");
                  mStorage = FirebaseStorage.getInstance().getReference().child(auth.getCurrentUser().getUid()).child(pati_oib);
            }

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(PhotoActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        mUploadBtn = (Button) findViewById(R.id.btn_take_a_photo1);
        mImageView = (ImageView) findViewById(R.id.imageview_photo);
        mProgress = new ProgressDialog(this);

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });
    }
        //
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                mProgress.setMessage("Uploading photo...");
                mProgress.show();
                Uri uri = data.getData();

                final StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());

                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProgress.dismiss();
                        Toast.makeText(getApplicationContext(), "Uploading finished", Toast.LENGTH_LONG).show();
                    }

                });

                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri urii) {
                                Picasso.get().load(urii.toString()).fit().centerCrop().into(mImageView);
                            }
                        });
                    }
                });

            }


    }

    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                startActivity(new Intent(PhotoActivity.this, LoginActivity.class));
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
