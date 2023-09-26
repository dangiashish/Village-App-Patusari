package com.jhunjhunu.patusari.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jhunjhunu.patusari.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jhunjhunu.patusari.utility.KeyConstants;
import com.jhunjhunu.patusari.utility.SharedPrefUtil;


@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    ImageView gif;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_splash_screen);


        gif = findViewById(R.id.gifImage);
        FirebaseDatabase.getInstance().getReference().child("splashscreen").child("gif").child("event")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String path = snapshot.getValue(String.class);
                        Glide.with(getApplicationContext()).load(path).into(gif);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String isSkipped = SharedPrefUtil.getString(KeyConstants.isLoginSkipped, "", SplashScreenActivity.this);

        if (isSkipped.equals("")) {
            startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
        } else {
            new Handler().postDelayed(() -> {
                if (isSkipped.equals("true")) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                } else if (isSkipped.equals("false")) {
                    if (firebaseUser != null) {
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, SignUpActivity.class));
                    }
                }
            }, 2000);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }


}