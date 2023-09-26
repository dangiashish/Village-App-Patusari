package com.jhunjhunu.patusari.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhunjhunu.patusari.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class das_cv_hospital extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private Toolbar toolbar;
    private ImageButton whatsappbtn;
    TextView drName, footerText;
    private ImageSlider imageSlider;
    private static final int REQUEST_CALL = 1;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.das_cv_hospital);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageSlider();

        doctorImage();

        doctorContact();

        displayFirebaseConfigText();


    }

    private void doctorContact() {

        whatsappbtn = findViewById(R.id.iButton_wp);

        FirebaseDatabase.getInstance().getReference("das_hospital").child("doctor_contact").child("phone")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String phonenumber = snapshot.getValue(String.class);
                        final String call = "tel:" + phonenumber.trim();

                        whatsappbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=91" + phonenumber + "&text=Hello"));
                                startActivity(intent);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void doctorImage() {
        final ImageView doctorImage = (ImageView) findViewById(R.id.img_doctor);
        FirebaseDatabase.getInstance().getReference().child("das_hospital").child("doctor_img").child("url")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String urldoctor = snapshot.getValue(String.class);
                        if (urldoctor != null){
                        Picasso.get().load(urldoctor).placeholder(R.drawable.ic_placeholder).into(doctorImage);
                    }}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void imageSlider() {
        imageSlider = (ImageSlider) findViewById(R.id.imgSlider_hospital);
        final List<SlideModel> slideModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("das_hospital").child("hospital_slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren())
                            slideModels.add(new SlideModel(data.child("url")
                                    .getValue()
                                    .toString()
                                    , data.child("title").getValue().toString()
                                    , ScaleTypes.FIT));
                        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void displayFirebaseConfigText() {
        drName = findViewById(R.id.drName);
        footerText = findViewById(R.id.bottom_text_stay_safe);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        long cacheExpriration = 0;
        firebaseRemoteConfig.fetch(cacheExpriration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.fetchAndActivate();
                        }
                        drName.setText(firebaseRemoteConfig.getString("doctor_name"));
                        footerText.setText(firebaseRemoteConfig.getString("hint_text"));
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}