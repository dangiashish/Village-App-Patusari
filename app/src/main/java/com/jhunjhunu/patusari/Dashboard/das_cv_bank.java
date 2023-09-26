package com.jhunjhunu.patusari.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jhunjhunu.patusari.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;


import java.util.ArrayList;
import java.util.List;

public class das_cv_bank extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.das_cv_bank);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageSlider imageSlider = findViewById(R.id.imageSlider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://brkgb.com/webdata/img/logos-brkgb.png", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://brkgb.com/webdata/img/logos-brkgb.png", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://brkgb.com/webdata/img/logos-brkgb.png", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.branch_name:
//                Toast.makeText(this,"Branch Name",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.branch_id:
//                Toast.makeText(this,"Branch ID : 4705",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.branch_code:
//                Toast.makeText(this,"Branch Name : PTUJJN",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.branch_contact:
//                intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:7726854705"));
//                Toast.makeText(this, "Branch Contact Number", Toast.LENGTH_SHORT).show();
//                if (ActivityCompat.checkSelfPermission(das_cv_bank.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    startActivity(intent);
//                }
//                break;

        }
    }
}