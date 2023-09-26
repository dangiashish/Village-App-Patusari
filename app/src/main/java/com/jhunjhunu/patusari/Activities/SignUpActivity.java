package com.jhunjhunu.patusari.Activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.jhunjhunu.patusari.utility.KeyConstants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jhunjhunu.patusari.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jhunjhunu.patusari.databinding.ActivitySignUpBinding;
import com.jhunjhunu.patusari.utility.KeyConstants;
import com.jhunjhunu.patusari.utility.SharedPrefUtil;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;
    String userId, fcmToken;
    ProgressDialog dialog;
    DatabaseReference userDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    SignUpActivity context;

    ActivitySignUpBinding binding;

    private static final int PERMISSION_REQUEST_CODE2 = 100;
    private static final int PERMISSION_REQUEST_CODE = 99;
    private static final int RC_SIGN_IN = 200;
    private Integer type = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = SignUpActivity.this;

        getFcmToken();

        userId = SharedPrefUtil.getString(USER_ID, "", context);
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Log.d("ASHISHJI userID", userId);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("214386117182-5i0frq2518fnptbcrg3muacpcjnlr8cr.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        initViews();


        spannableText();


    }

    private void spannableText() {
        String text = binding.text.getText().toString().trim();
        SpannableString spannableString = new SpannableString(text);

        ClickableSpan termsClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                String tc = "https://patusarijjn.wordpress.com/terms-and-conditions/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tc));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
//                ds.setColor(Color.BLUE);
            }
        };

        ClickableSpan policyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                String primary = "https://patusarijjn.wordpress.com/privacy-policy/";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(primary));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
//                ds.setColor(Color.BLUE);
            }
        };

        int termsStart = text.indexOf("Terms and Conditions");
        int termsEnd = termsStart + "Terms and Conditions".length();
        spannableString.setSpan(termsClickableSpan, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int policyStart = text.indexOf("Privacy Policy");
        int policyEnd = policyStart + "Privacy Policy".length();
        spannableString.setSpan(policyClickableSpan, policyStart, policyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.text.setText(spannableString);
        binding.text.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void initViews() {

        binding.ivGoogle.setOnClickListener(view -> {
            type = 1;
            checkRuntimePermission();
        });


        binding.ivSkip.setOnClickListener(c -> {
            type = 0;
            checkRuntimePermission();

        });

    }


    private void checkRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
            ) {
               if (type == 0){
                   SharedPrefUtil.setString(KeyConstants.isLoginSkipped, "true", SignUpActivity.this);
                   startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                   finish();
               } else if (type == 1){
                   SharedPrefUtil.setString(KeyConstants.isLoginSkipped, "false", SignUpActivity.this);
                   Intent signInIntent = googleSignInClient.getSignInIntent();
                   startActivityForResult(signInIntent, RC_SIGN_IN);

               }


            } else {

                showBottomDialog();

            }
        } else {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                if (type == 0){
                    SharedPrefUtil.setString(KeyConstants.isLoginSkipped, "true", SignUpActivity.this);
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                } else if (type == 1){
                    SharedPrefUtil.setString(KeyConstants.isLoginSkipped, "false", SignUpActivity.this);
                    Intent signInIntent = googleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            } else {
                showBottomDialog();
            }
        }
    }


    public void showBottomDialog() {
        final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.custom_layout_permission_guide))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setBackgroundColorResId(Color.TRANSPARENT)
                .create();
        View view = dialogPlus.getHolderView();
        AppCompatButton btnTc = view.findViewById(R.id.btnTC);
        AppCompatButton btnNext = view.findViewById(R.id.btnNext);
        AppCompatButton btnCancel = view.findViewById(R.id.btnCancel);

        btnNext.setOnClickListener(click -> {
            permissionDialogShow();
            dialogPlus.dismiss();
        });
        btnCancel.setOnClickListener(click ->
                dialogPlus.dismiss());
        btnTc.setOnClickListener(click -> {
            String tc = "https://patusarijjn.wordpress.com/terms-and-conditions/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tc));
            startActivity(intent);
        });

        dialogPlus.show();
    }


    private void permissionDialogShow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestAllPermission2();
        } else
            requestAllPermission();
    }

    private void requestAllPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestAllPermission2() {
        ActivityCompat.requestPermissions(this, new String[]{POST_NOTIFICATIONS, READ_MEDIA_IMAGES, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (type == 0){
                    SharedPrefUtil.setString(KeyConstants.isLoginSkipped, "true", SignUpActivity.this);
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                } else if ( type == 1){
                    SharedPrefUtil.setString(KeyConstants.isLoginSkipped, "false", SignUpActivity.this);
                    Intent intent = googleSignInClient.getSignInIntent();
                    startActivityForResult(intent, RC_SIGN_IN);
                }

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE2) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                if (type == 0){
                    SharedPrefUtil.setString(KeyConstants.isLoginSkipped, "true", SignUpActivity.this);
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                } else if ( type == 1){
                    SharedPrefUtil.setString(KeyConstants.isLoginSkipped, "false", SignUpActivity.this);
                    Intent intent = googleSignInClient.getSignInIntent();
                    startActivityForResult(intent, RC_SIGN_IN);
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
//            ProgressUtils.showSimpleProgressDialog(context, true);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else {
            Log.e("SONU", "request code does not match");
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        if (task.isSuccessful()) {
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, task1 -> {
                        if (task1.isSuccessful()) {
                            String userName = account.getDisplayName();
                            String userEmail = account.getEmail();
                            String userPicOld = Objects.requireNonNull(account.getPhotoUrl()).toString();
                            String userPic = null;
                            String userId = null;
                            if (userPicOld.contains("s96")) {
                                userPic = userPicOld.replace("s96", "s180");
                            }
                            String authId = firebaseAuth.getUid();
                            Log.d("ashishji auth token", account.getIdToken());
                            if (userEmail != null) {
                                if (userEmail.contains("@gmail.com")) {
                                    userId = userEmail.replace("@gmail.com", "");
                                } else if (userEmail.contains(".")) {
                                    userId = userEmail.replace(".", "");
                                }

                                String accessToken = account.getIdToken();
                                String finalUserPic = userPic;
                                String finalUserId = userId;
                                userDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild(finalUserId)) {
                                            userDatabase.child(finalUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String userName = snapshot.child("profile").child("userName").getValue(String.class);
                                                    String userId = snapshot.child("profile").child("userId").getValue(String.class);
                                                    String userPhone = snapshot.child("profile").child("userPhone").getValue(String.class);

                                                    SharedPrefUtil.setString(USER_ID, userId, context);
                                                    SharedPrefUtil.setString(KeyConstants.USER_FULL_NAME, userName, context);
                                                    SharedPrefUtil.setString(KeyConstants.USER_PHONE, userPhone, context);

                                                    updateUserDatabase(authId, finalUserId, userName, finalUserPic, fcmToken, accessToken);

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

//
                                        } else {
                                            setUserDatabase(authId, finalUserId, userName, userEmail, finalUserPic, fcmToken,accessToken);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ashishji", e.getMessage());
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (ApiException e) {
                e.printStackTrace();
                Log.e("ashishji", "auth error : " + e.getStatus().toString());
            }
        }
    }


    private void setUserDatabase(String authId, String userId, String userName, String userEmail, String userPic,
                                 String fcmToken, String accessToken) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", userId);
        hashMap.put("userName", userName);
        hashMap.put("userEmail", userEmail);
        hashMap.put("userPic", userPic);
        hashMap.put("userPhone", "");
        hashMap.put("authId", authId);
        hashMap.put("fcmToken", fcmToken);
        hashMap.put("lat", "");
        hashMap.put("lng", "");
        hashMap.put("version", "1.0.0");
        hashMap.put("verified", String.valueOf(false));

        userDatabase.child(userId).child("profile").setValue(hashMap);


        SharedPrefUtil.setString(KeyConstants.USER_FULL_NAME, userName, context);
        SharedPrefUtil.setString(KeyConstants.USER_EMAIL, userEmail, context);
        SharedPrefUtil.setString(KeyConstants.USER_PIC, userPic, context);
        SharedPrefUtil.setString(KeyConstants.KEY_ACCESS_TOKEN, accessToken, context);
        SharedPrefUtil.setString(KeyConstants.VERIFIED, "false", context);

//        ProgressUtils.removeSimpleProgressDialog();
        startActivity(new Intent(context, MainActivity.class));
        finishAffinity();

    }


    private void updateUserDatabase(String authId, String userId, String userName, String userPic, String fcmToken, String accessToken) {
        Log.d("hello onUpdate", "update called");
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> map = new HashMap<>();
                map.put("fcmToken", fcmToken);
                map.put("lat", "");
                map.put("lng", "");
                map.put("version", "1.0.0");
                map.put("authId", authId);
                userDatabase.child(userId).child("profile").updateChildren(map, (error, ref) -> {
                        }
                );

                SharedPrefUtil.setString(KeyConstants.KEY_ACCESS_TOKEN, accessToken, context);
                SharedPrefUtil.setString(KeyConstants.USER_PIC, userPic, context);
//                ProgressUtils.removeSimpleProgressDialog();

                startActivity(new Intent(context, MainActivity.class));
                finishAffinity();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("ASHISHJI", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    fcmToken = task.getResult();

                    Log.d("ASHISHJI fcmToken", fcmToken);

                    SharedPrefUtil.setString(KeyConstants.FCMTOKEN, fcmToken, context);

                });
    }


  /*  public void onClick(View view) {
        if (view.getId() == R.id.text_EULA) {
            Intent i = new Intent(getApplicationContext(), TermsConditionsActivity.class);
            startActivity(i);
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm = null;
    }
}