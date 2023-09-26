package com.jhunjhunu.patusari.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jhunjhunu.patusari.R;
import com.squareup.picasso.Picasso;

public class das_cv_emitra extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imglogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.das_cv_emitra);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imglogo = findViewById(R.id.imagelogo);
        String url = "https://emitra.rajasthan.gov.in/content/dam/emitra/images/img/t-logo.png";
        Picasso.get().load(url).placeholder(R.drawable.ic_placeholder).into(imglogo);

    }


    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
//            case R.id.center_name:
//                break;
//
//            case R.id.center_sso_id:
//                Toast.makeText(this,"Sorry, You cant See Completely",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.kiosk_code:
//                Toast.makeText(this,"kiosk code : K103168864",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.center_contact:
////                intent = new Intent(Intent.ACTION_DIAL);
////                intent.setData(Uri.parse("tel:7726854705"));
//                Toast.makeText(this, "Kiosk Contact Number Not Available", Toast.LENGTH_SHORT).show();
////                if (ActivityCompat.checkSelfPermission(das_cv_emitra.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
////                    startActivity(intent);
////                }
//                break;

        }
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