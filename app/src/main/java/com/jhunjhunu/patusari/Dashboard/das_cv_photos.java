package com.jhunjhunu.patusari.Dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhunjhunu.patusari.Activities.SignUpActivity;
import com.jhunjhunu.patusari.Adapters.photosAdapter;
import com.jhunjhunu.patusari.R;
import com.jhunjhunu.patusari.modelClass.photosModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.ViewHolder;
//import com.skydoves.balloon.ArrowConstraints;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;

import java.io.IOException;
import java.util.HashMap;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;


public class das_cv_photos extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private Toolbar toolbar;
    Button btnbrowse, btnupload;
    ImageView imgview;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    int Image_Request_Code = 7;
    ProgressDialog progressDialog;
    TextView uploaderName, swipedownText;
    private RecyclerView recyclerPhotos;
    private photosAdapter photosAdapter;
    private SwipeRefreshLayout refresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.das_cv_photos);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        recyclerPhotos = findViewById(R.id.recycle_photosGallery);
        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedownRefresh);
        btnbrowse = (Button) findViewById(R.id.btnbrowse);
        btnupload = (Button) findViewById(R.id.btnupload);
        imgview = (ImageView) findViewById(R.id.image_view);
        uploaderName = (TextView) findViewById(R.id.uploaderName);
        progressDialog = new ProgressDialog(das_cv_photos.this);


        storageReference = FirebaseStorage.getInstance().getReference("Patusari App Drawables").child("das_photos");
        databaseReference = FirebaseDatabase.getInstance().getReference("das_photos").push();

        btnbrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserImage();
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });

        recyclerPhotosLoading();

        noInternetDialog();
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
                    recyclerPhotosLoading();
                    dialogPlus.dismiss();
                }
            });
            dialogPlus.show();
        }
    }


    private void swipeRefresh() {
        refresh = findViewById(R.id.swipedownRefresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                photosAdapter.stopListening();
                refreshData();
            }
        });
    }

    private void refreshData() {
        noInternetDialog();
        photosAdapter.startListening();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        }, 1000);

    }

    private void recyclerPhotosLoading() {

        recyclerPhotos = findViewById(R.id.recycle_photosGallery);
        recyclerPhotos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPhotos.setNestedScrollingEnabled(false);

        FirebaseRecyclerOptions<photosModel> options =
                new FirebaseRecyclerOptions.Builder<photosModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("das_photos"), photosModel.class)
                        .build();

        photosAdapter = new photosAdapter(options);

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setReverseLayout(true);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerPhotos.setAdapter(photosAdapter);
        recyclerPhotos.setLayoutManager(manager);

        swipeRefresh();

    }

    private void browserImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
        } else  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
        } else  {
            requestCallPermisiion();
        }

    }
    private void requestCallPermisiion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.requestPermissions(this, new String[]{READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void UploadImage() {
        noInternetDialog();

        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();

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

            if (FilePathUri != null) {

                progressDialog.setCanceledOnTouchOutside(false);

                if (firebaseUser != null) {
                    uploaderName.setText(firebaseUser.getDisplayName());
                }

                final String uploaderNameFolder = uploaderName.getText().toString();


                final StorageReference storageReference2 =
                        storageReference.child(uploaderNameFolder)
                                .child(System.currentTimeMillis() + "." + getFileExtension(FilePathUri));

                storageReference2.putFile(FilePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        auth = FirebaseAuth.getInstance();
                        final FirebaseUser firebaseUser = auth.getCurrentUser();

                        if (firebaseUser != null) {
                            uploaderName.setText(firebaseUser.getDisplayName());
                        }

                        final String uploaderNameFolder = uploaderName.getText().toString();

                        storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("das_photos").push();

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("imageURL", String.valueOf(uri));
                                hashMap.put("message", "");
                                hashMap.put("userName", uploaderNameFolder);
                                databaseReference2.setValue(hashMap);

                            }
                        });

                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();

                        progressDialog.setTitle("Hi " + auth.getCurrentUser().getDisplayName());
                        progressDialog.setMessage(" Your Image is Uploading " + (int) percent + " %");
                        progressDialog.show();

                    }
                });
            } else {
                balloonShow();
            }
        }
    }

    private void balloonShow() {
        final Balloon balloon = new Balloon.Builder(this)
                .setArrowSize(10)
                .setArrowOrientation(ArrowOrientation.TOP)
//                .setArrowConstraints(ArrowConstraints.ALIGN_ANCHOR)
                .setArrowPosition(0.5f)
//                .setArrowVisible(true)
                .setArrowColor(ContextCompat.getColor(this, R.color.black))
                .setWidthRatio(0.5f)
                .setHeight(50)
                .setWidth(60)
                .setTextSize(15f)
                .setCornerRadius(4f)
                .setAlpha(0.9f).setElevation(10)
                .setText("Choose Image First")
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.black))
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();

        balloon.showAlignBottom(btnbrowse);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                balloon.dismiss();
            }
        }, 2500);
    }

    public String getFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    public void onStart() {
        super.onStart();
        photosAdapter.startListening();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                imgview.setImageBitmap(bitmap);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
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