package com.jhunjhunu.patusari.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jhunjhunu.patusari.Activities.SignUpActivity;
import com.jhunjhunu.patusari.Adapters.complaintsAdapter;
import com.jhunjhunu.patusari.R;
import com.jhunjhunu.patusari.complaints_userdata;
import com.jhunjhunu.patusari.modelClass.complaintsModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
//import com.skydoves.balloon.ArrowConstraints;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class das_cv_complaints extends AppCompatActivity {

    private AppCompatEditText writefeedback, writephone, writeemail;
    private AppCompatButton submit;
    private AppCompatTextView writename;
    private FirebaseDatabase rootNode;
    private DatabaseReference setDatabase;
    private FirebaseAuth firebaseAuth;
    private complaintsAdapter complaintsAdapter;
    private SwipeRefreshLayout refresh;
    private complaints_userdata userdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.das_cv_complaints);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        refresh = findViewById(R.id.swipedownRefresh);
        writename =  findViewById(R.id.etName);
        writefeedback = findViewById(R.id.etFeedback);
        writephone = findViewById(R.id.etPhone);
        writeemail = findViewById(R.id.etUserEmail_feedback);

        submit = findViewById(R.id.btnSubmit);

        warnDialog();

        submitFeedback();

        fetchNameEmail();

        recyclerComplaintsLoading();

        noInternetDialog();

        balloonShow();

    }

    private void balloonShow() {
        writename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Balloon balloon = new Balloon.Builder(getApplicationContext())
                        .setArrowSize(10)
                        .setArrowOrientation(ArrowOrientation.TOP)
//                        .setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
                        .setArrowPosition(0.1f)
//                        .setArrowVisible(true)
                        .setArrowColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
                        .setWidthRatio(0.8f)
                        .setHeight(50)
                        .setWidth(160)
                        .setTextSize(15f)
                        .setTextTypeface(Typeface.BOLD)
                        .setCornerRadius(4f)
                        .setAlpha(0.9f)
                        .setElevation(5)
                        .setText("Google Account Name, You Can't Edit")
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white))
                        .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
                        .setBalloonAnimation(BalloonAnimation.FADE)
                        .build();

                balloon.showAlignBottom(writename);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        balloon.dismiss();
                    }
                }, 2500);
            }
        });

    }

    private void fetchNameEmail() {
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            writename.setText(firebaseUser.getDisplayName());
            writeemail.setText(firebaseUser.getEmail());
        }
    }


    public void warnDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please write your information correctly, You can't edit feedbacks after submit\nकृपया अपना विवरण सही लिखे, क्योंकि भेजने के बाद आप सम्पादित नहीं कर पाएंगे।");
        builder.setTitle("Warning !!");
        builder.setCancelable(true);
        builder.setNegativeButton("OK", null);
        builder.show();
    }

    private void submitFeedback() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                noInternetDialog();
                firebaseAuth = FirebaseAuth.getInstance();
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Sign In First", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                            startActivity(intent);
                        }
                    }, 1500);
                } else {
                    rootNode = FirebaseDatabase.getInstance();
                    setDatabase = rootNode.getReference("das_complaints");

                    if (TextUtils.isEmpty(writename.getText())) {
                        writename.setError("Enter name");
                        writename.requestFocus();
                    } else {
                       /* if (writephone.length() < 1) {
                            writephone.setError("Enter your number correctly");
                            writephone.requestFocus();
                        } else {*/
                            if (writefeedback.length() < 5) {
                                writefeedback.setError("Please write atleast 50 characters");
                                writefeedback.requestFocus();
                            } else {

                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
                                String time = currentTime.format(calendar.getTime());

                                SimpleDateFormat currentDate = new SimpleDateFormat("dd, MMM, yyyy, ");
                                String date = "Posted on " + currentDate.format(calendar.getTime());

                                String name = writename.getText().toString();
                                String phone = writephone.getEditableText().toString();
                                String feedback = writefeedback.getEditableText().toString();
                                String email = writeemail.getEditableText().toString();

                                userdata = new complaints_userdata(name, phone, email, feedback, date + time);
                                setDatabase.push().setValue(userdata);


                                writephone.getText().clear();
                                writefeedback.getText().clear();

//                                successfulDialog();
                                Toast.makeText(das_cv_complaints.this, "Added successfully", Toast.LENGTH_SHORT).show();

//                            }
                        }
                    }
                }

            }
        });
    }

    private void successfulDialog() {
        DialogPlus dialogPlus = DialogPlus.newDialog(das_cv_complaints.this)
                .setContentHolder(new ViewHolder(R.layout.custom_successful_dialog))
                .setCancelable(true)
                .setGravity(Gravity.CENTER)
                .setExpanded(true, 700)
                .setBackgroundColorResId(Color.TRANSPARENT)
                .create();

        dialogPlus.show();
    }


    private void recyclerComplaintsLoading() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);

        FirebaseRecyclerOptions<complaintsModel> options =
                new FirebaseRecyclerOptions.Builder<complaintsModel>()
                        .setQuery(FirebaseDatabase.getInstance().
                                getReference("das_complaints"), complaintsModel.class)
                        .build();
        complaintsAdapter = new complaintsAdapter(options);
        recyclerView.setAdapter(complaintsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefresh();
    }

    private void swipeRefresh() {
        refresh = findViewById(R.id.swipedownRefresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                complaintsAdapter.stopListening();
                refreshData();
            }
        });
    }

    private void refreshData() {
        noInternetDialog();
        complaintsAdapter.startListening();
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

            Button btnRetry = view.findViewById(R.id.btnRetry);
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerComplaintsLoading();
                    dialogPlus.dismiss();
                }
            });
            dialogPlus.show();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        complaintsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        complaintsAdapter.stopListening();
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
    }
}