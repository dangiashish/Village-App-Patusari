package com.jhunjhunu.patusari.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jhunjhunu.patusari.R;
import com.jhunjhunu.patusari.Adapters.vehicleAdapter;
import com.jhunjhunu.patusari.modelClass.vehicleModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.List;

public class das_cv_vehicle_book extends AppCompatActivity {

    private Toolbar toolbar;
    RecyclerView recyclerView;
    vehicleAdapter vehicleAdapterList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.das_cv_vehicle_book);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);


        noInternetDialog();
        loadVehicleList();
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
                    loadVehicleList();
                    dialogPlus.dismiss();
                }
            });
            dialogPlus.show();

        }
    }

    private void loadVehicleList() {
        recyclerView = findViewById(R.id.recyclerView_vehicle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<vehicleModel> options =
                new FirebaseRecyclerOptions.Builder<vehicleModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("vehiclebook"), vehicleModel.class)
                        .build();

        vehicleAdapterList = new vehicleAdapter(options);
        recyclerView.setAdapter(vehicleAdapterList);
        vehicleAdapterList.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        vehicleAdapterList.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vehicleAdapterList.stopListening();
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