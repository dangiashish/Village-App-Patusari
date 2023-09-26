package com.jhunjhunu.patusari.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jhunjhunu.patusari.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class AboutAppActivity extends AppCompatActivity {

    private TextView appDesc;
    private FirebaseRemoteConfig firebaseRemoteConfig;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_menu_aboutapp);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        displayFirebaseConfigText();


        TextView terms_cond = findViewById(R.id.terms_conditions);
        terms_cond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tc = "https://patusarijjn.wordpress.com/terms-and-conditions/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tc));
                startActivity(intent);
            }
        });

        TextView privacypolicy = (TextView) findViewById(R.id.privacy_policy);
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String primary = "https://patusarijjn.wordpress.com/privacy-policy/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(primary));
                startActivity(intent);
            }
        });



    }

    private void displayFirebaseConfigText() {
        appDesc = findViewById(R.id.appDescription);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        long cacheExpriration = 0;
        firebaseRemoteConfig.fetch(cacheExpriration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.fetchAndActivate();
                        }
                        appDesc.setText(firebaseRemoteConfig.getString("app_description"));
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}