package com.jhunjhunu.patusari.Activities;

import static com.jhunjhunu.patusari.utility.Utils.getVersionCode;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jhunjhunu.patusari.Dashboard.das_cv_about;
import com.jhunjhunu.patusari.Dashboard.das_cv_bank;
import com.jhunjhunu.patusari.Dashboard.das_cv_complaints;
import com.jhunjhunu.patusari.Dashboard.das_cv_emitra;
import com.jhunjhunu.patusari.Dashboard.das_cv_hospital;
import com.jhunjhunu.patusari.Dashboard.das_cv_photos;
import com.jhunjhunu.patusari.Dashboard.das_cv_roads;
import com.jhunjhunu.patusari.Dashboard.das_cv_school;
import com.jhunjhunu.patusari.Dashboard.das_cv_vehicle_book;
import com.jhunjhunu.patusari.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import de.cketti.library.changelog.ChangeLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    CardView Hospital, School, Road, About, Emitra, Bank, Car, Panchayat, VillageMap, Complaints, photos;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private TextView appFooterText;
    private ImageView lights, eventimg1, eventimg2;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private Button btnlogout;

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnlogout = findViewById(R.id.btn_logout);
        eventImages();
        dasboardItems();
        toolbarShow();
        navigationDrawer();
        googleSignOutButton();
        footerTextFetch();
        userNameLogoFetch();
        firebasePushNotification();
//        changeLogShow();
        remoteConfigInit();


        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);


    }

    private void remoteConfigInit() {

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                final String new_version_code = firebaseRemoteConfig.getString("version_code");
                final String update_key_feature = firebaseRemoteConfig.getString("update_key");
                final String saas_force_update = firebaseRemoteConfig.getString("force_update");

                Log.d("jjn", new_version_code + " " + getVersionCode(MainActivity.this));

                    if (Integer.parseInt(new_version_code) > getVersionCode(MainActivity.this)) {
                        showTheDialog(update_key_feature, false);
                    }
                    if (Integer.parseInt(saas_force_update) > getVersionCode(MainActivity.this)) {
                        showTheDialog(update_key_feature, true);
                    }


            } else
                Log.e("MYLOG", "mFirebaseRemoteConfig.fetchAndActivate() NOT Successful");
        });
    }

    private void showTheDialog(String updateKey, boolean isForce) {
        final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.custom_update_dialog))
                .setCancelable(false)
                .setGravity(Gravity.BOTTOM)
                .setBackgroundColorResId(Color.TRANSPARENT)
                .create();
        View view = dialogPlus.getHolderView();

        TextView tvUpdate = view.findViewById(R.id.tvUpdateKey);
        TextView tvKey = view.findViewById(R.id.tvKey);
        AppCompatButton btnUpdate = view.findViewById(R.id.btnUpdate);
        AppCompatButton btnExit = view.findViewById(R.id.btnExit);
        if (isForce) {
            btnExit.setVisibility(View.GONE);
        } else {
            btnExit.setVisibility(View.VISIBLE);
        }
        if (updateKey.equalsIgnoreCase("")) {
            tvUpdate.setVisibility(View.GONE);
            tvKey.setVisibility(View.GONE);
        } else {
            tvUpdate.setText(updateKey);
        }
        btnUpdate.setOnClickListener(update -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + "com.jhunjhunu.patusari")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + "com.jhunjhunu.patusari")));
            }
        });

        btnExit.setOnClickListener(exit -> dialogPlus.dismiss());
        dialogPlus.show();
    }

    private void eventImages() {
        lights = findViewById(R.id.lightImage);
        FirebaseDatabase.getInstance().getReference("mainActivity")
                .child("gif").child("light")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String path = snapshot.getValue(String.class);
                        Glide.with(getApplicationContext()).load(path).into(lights);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        eventimg1 = findViewById(R.id.eventImage1);
        FirebaseDatabase.getInstance().getReference("mainActivity")
                .child("gif").child("event").child("img1")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String path = snapshot.getValue(String.class);
                        Glide.with(getApplicationContext()).load(path).into(eventimg1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        eventimg2 = findViewById(R.id.eventImage2);
        FirebaseDatabase.getInstance().getReference("mainActivity")
                .child("gif").child("event").child("img2")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String path = snapshot.getValue(String.class);
                        Glide.with(getApplicationContext()).load(path).into(eventimg2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }

    private void dasboardItems() {

        About = findViewById(R.id.das_about);
        Hospital = findViewById(R.id.das_hospital);
        School = findViewById(R.id.das_school);
        Emitra = findViewById(R.id.das_emitra);
        Bank = findViewById(R.id.das_bank);
        Road = findViewById(R.id.das_roads);
        Car = findViewById(R.id.das_car);
        Panchayat = findViewById(R.id.das_panchayat);
        VillageMap = findViewById(R.id.das_village_map);
        Complaints = findViewById(R.id.das_complaints);
        photos = findViewById(R.id.das_photos);

        About.setOnClickListener(this);
        Hospital.setOnClickListener(this);
        School.setOnClickListener(this);
        Emitra.setOnClickListener(this);
        Bank.setOnClickListener(this);
        Road.setOnClickListener(this);
        Car.setOnClickListener(this);
        Panchayat.setOnClickListener(this);
        VillageMap.setOnClickListener(this);
        Complaints.setOnClickListener(this);
        photos.setOnClickListener(this);
    }

    private void navigationDrawer() {
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }


    private void toolbarShow() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }


    private void changeLogShow() {
        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }
    }

    private void footerTextFetch() {

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        appFooterText = findViewById(R.id.app_footer_text);
        appFooterText.setSelected(true);
        long cacheExpriration = 100;
        firebaseRemoteConfig.fetch(cacheExpriration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.fetchAndActivate();
                        }
                        appFooterText.setText(firebaseRemoteConfig.getString("app_footer_text"));
                    }
                });
    }

    private void userNameLogoFetch() {
        ImageView userlogo = findViewById(R.id.user_logo);
        TextView username = findViewById(R.id.user_name);
        TextView useremail = findViewById(R.id.user_email);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            TextView toolbar_title = findViewById(R.id.toolbar_main_title);
            toolbar_title.setText("Hi " + firebaseUser.getDisplayName());
            btnlogout.setText("Sign Out");
        } else {
            btnlogout.setText("Sign In");
        }


        if (firebaseUser != null) {

            Glide.with(MainActivity.this).load(firebaseUser.getPhotoUrl())
                    .placeholder(R.drawable.ic_placeholder).into(userlogo);

            username.setText(firebaseUser.getDisplayName());
            useremail.setText(firebaseUser.getEmail());
        }
    }

    private void firebasePushNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("signedin")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        task.isSuccessful();
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("install")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        task.isSuccessful();
                    }
                });
    }

    private void googleSignOutButton() {
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnlogout.getText().equals("Sign In")){
                    startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                    finish();
                } else if (btnlogout.getText().equals("Sign Out")) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth.signOut();
                                Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finishAffinity();
                                btnlogout.setText("Sign In");
                            }
                        }
                    });
                }

            }
        });
    }

    public boolean isFbAppInstalled() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tool_menu_about) {
            Intent intent = new Intent(MainActivity.this, AboutAppActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        if (v.getId() == R.id.das_about) {
            intent = new Intent(this, das_cv_about.class);
            startActivity(intent);
        } else if (v.getId() == R.id.das_hospital) {
            intent = new Intent(this, das_cv_hospital.class);
            startActivity(intent);
        } else if (v.getId() == R.id.das_emitra) {
            intent = new Intent(this, das_cv_emitra.class);
            startActivity(intent);
        } else if (v.getId() == R.id.das_school) {
            intent = new Intent(this, das_cv_school.class);
            startActivity(intent);
        } else if (v.getId() == R.id.das_bank) {
            intent = new Intent(this, das_cv_bank.class);
            startActivity(intent);
        } else if (v.getId() == R.id.das_roads) {
            intent = new Intent(this, das_cv_roads.class);
            startActivity(intent);
        } else if (v.getId() == R.id.das_car) {
            intent = new Intent(this, das_cv_vehicle_book.class);
            startActivity(intent);
        } else if (v.getId() == R.id.das_complaints) {
            intent = new Intent(this, das_cv_complaints.class);
            startActivity(intent);
        } else if (v.getId() == R.id.das_village_map) {
            Uri gmmIntentUri = Uri.parse("geo:28.0496604,75.454654,540m");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } else if (v.getId() == R.id.das_photos) {
            intent = new Intent(this, das_cv_photos.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_signin) {
            intent = new Intent(getApplicationContext(), SignUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_policy) {
            String primary = "https://patusarijjn.wordpress.com/privacy-policy/";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(primary));
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_share_text)); // Note: Use getString() to get the string resource.
            startActivity(Intent.createChooser(intent, "Share App"));
        } else if (id == R.id.nav_facebook) {

                String uri = "fb://page/235618219963709";
                Intent fbintent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(fbintent);

        } else if (id == R.id.nav_youtube) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://youtube.com/patusarijhunjhunu"));
            intent.setPackage("com.google.android.youtube");
            startActivity(intent);
        } else if (id == R.id.nav_whatspp_join) {

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/919785928939?text=Add%20me%20in%20Patusari%20group"));

                startActivity(intent);

//
        }
        /*else if (id == R.id.nav_rate) {
            Toast.makeText(getApplicationContext(), "App is not on Play Store", Toast.LENGTH_SHORT).show();
        }*/
        else if (id == R.id.nav_developer) {
            intent = new Intent(MainActivity.this, DeveloperActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setIcon(R.mipmap.ic_launcher_round);
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(R.string.exit_confirm);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton(R.string.no, null);
        alertDialog.show();
*/
    }

    @Override
    protected void onStart() {

        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun) {

            new Handler().postDelayed(() -> {
                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.welcome_sound);
                mediaPlayer.start();
            }, 1500);


            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .apply();
        }
        super.onStart();
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

            Button btnRetry = view.findViewById(R.id.btnRetry);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogPlus.dismiss();
                }
            });
            dialogPlus.show();

        }
    }

}