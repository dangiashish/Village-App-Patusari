package com.jhunjhunu.patusari.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jhunjhunu.patusari.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class das_cv_about extends AppCompatActivity {

    Toolbar toolbar;
    private TextView aboutMain;
    private FirebaseRemoteConfig remoteConfig;
    private ShimmerFrameLayout shimmerFrameLayout;
    MediaPlayer player;
    private SwipeRefreshLayout refresh;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.das_cv_about);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        shimmerEffect();
        displayFirebaseConfigText();
        noInternetDialog();
        swipeRefresh();
        imageSlider();


    }

    public void shimmerEffect(){
        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    private void imageSlider() {
        final ImageSlider image1 = (ImageSlider) findViewById(R.id.img_Slider);
        final List<SlideModel> slideModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("das_about").child("village_images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (shimmerFrameLayout.isShimmerVisible()) {
//                            shimmerFrameLayout.hideShimmer();
                            for (DataSnapshot data : snapshot.getChildren())
                                slideModels.add(new SlideModel(data.child("url")
                                        .getValue()
                                        .toString(), ScaleTypes.FIT));
                            image1.setImageList(slideModels, ScaleTypes.FIT);
//                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void displayFirebaseConfigText() {
        aboutMain = findViewById(R.id.text_about1);
        remoteConfig = FirebaseRemoteConfig.getInstance();
        long cacheExpriration = 0;
        remoteConfig.fetch(cacheExpriration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (shimmerFrameLayout.isShimmerVisible()) {
                    shimmerFrameLayout.hideShimmer();
                    if (task.isSuccessful()) {
                        remoteConfig.fetchAndActivate();
                    }
                    aboutMain.setText(remoteConfig.getString("about_village_text"));
                }
            }
        });
    }

    private void swipeRefresh() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedownRefresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                refreshData();
            }
        });
    }

    private void refreshData() {
        noInternetDialog();
        displayFirebaseConfigText();
        imageSlider();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 1000);
    }

    private void noInternetDialog() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                    .setContentHolder(new ViewHolder(R.layout.custom_no_internet))
                    .setCancelable(false)
                    .setGravity(Gravity.CENTER)
                    .setBackgroundColorResId(Color.TRANSPARENT)
                    .create();

            View view = dialogPlus.getHolderView();

            Button btnRetry = (Button) view.findViewById(R.id.btnRetry);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayFirebaseConfigText();
                    dialogPlus.dismiss();
                }
            });
            dialogPlus.show();

        }

    }

    public void play(View v) {

            if (player == null) {
                player = MediaPlayer.create(this,
                        Uri.parse("https://firebasestorage.googleapis.com/v0/b/patusari-android-app.appspot.com/o/Patusari%20App%20Drawables%2Fabout(dashboard)%2Fpatusari_about_voice?alt=media&token=7c7238d0-8e76-4444-a3c8-eac6ea1d59df"));
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopPlayer();
                    }
                });
            }
            player.start();

    }

    public void pause(View v) {
        if (player != null) {
            player.pause();
        }
    }

    public void stop(View v) {
        stopPlayer();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
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