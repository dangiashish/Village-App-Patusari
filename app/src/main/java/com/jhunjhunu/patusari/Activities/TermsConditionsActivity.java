package com.jhunjhunu.patusari.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jhunjhunu.patusari.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class TermsConditionsActivity extends AppCompatActivity {

    ImageButton btn_close;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private TextView termscondistions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_popup_terms_conditions);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);


        btn_close = (ImageButton) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        termscondistions = findViewById(R.id.terms_conditions);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        long cacheExpriration = 0;
        firebaseRemoteConfig.fetch(cacheExpriration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.fetchAndActivate();
                        }
                        displayFirebaseConfigText();
                    }
                });

    }

    private void displayFirebaseConfigText() {
        termscondistions.setText(firebaseRemoteConfig.getString("terms_and_conditions"));
    }
}