package com.jhunjhunu.patusari.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jhunjhunu.patusari.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DeveloperActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_developer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        ImageSlider imageSlider = findViewById(R.id.img_developer_cover);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://c4.wallpaperflare.com/wallpaper/819/205/214/minimalism-programming-code-lyrics-wallpaper-preview.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1461749280684-dccba630e2f6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&w=1000&q=80", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://c4.wallpaperflare.com/wallpaper/244/41/128/quote-inspirational-typography-motivational-wallpaper-preview.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://theknowledgeexchangeblog.files.wordpress.com/2019/07/coding-1-1.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://c4.wallpaperflare.com/wallpaper/527/102/511/programming-code-php-wallpaper-preview.jpg", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        ImageView userImage = findViewById(R.id.img_user);
        String urlUserImage = "https://codebyashish.netlify.app/assets/img/codebyashish.png";
        Picasso.get().load(urlUserImage).placeholder(R.drawable.ic_placeholder).into(userImage);

        ImageView imgComing = findViewById(R.id.img_comingsoon);
        String urlComingimg = "https://www.psdstamps.com/wp-content/uploads/2019/11/grunge-coming-soon-label-png.png";
        Picasso.get().load(urlComingimg).into(imgComing);

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
        if (view.getId() == R.id.ashishemail) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("mailto:ashishdangi96@gmail.com"));
            startActivity(intent);
        }
        else  if (view.getId() == R.id.ashishwebsite) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://codebyashish.netlify.app"));
            startActivity(intent);
        }
        else  if (view.getId() == R.id.ashishtwitter) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://twitter.com/ashishdangi369"));
            startActivity(intent);
        }
        else  if (view.getId() == R.id.ashishfacebook) {
            if (isFbAppInstalled()) {
                String uri = "fb://profile/100014279683344";
                Intent fbintent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(fbintent);
            }
        }
        /*else  if (view.getId() == R.id.ashishlocation) {
            Uri gmmIntentUri = Uri.parse("geo:28.0465406,75.4345811,340m");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }*/

    }
    private boolean isFbAppInstalled() {
        return true;
    }
}