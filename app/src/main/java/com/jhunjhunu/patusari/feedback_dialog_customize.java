package com.jhunjhunu.patusari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class feedback_dialog_customize extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_dialog_customize);

        getIncomingIntent();

        updateFeedback();
    }

    private void updateFeedback() {
        Button update = findViewById(R.id.btnUpdate);
        update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra("name") && getIntent().hasExtra("phone") && getIntent().hasExtra("feedback")){
            String name = getIntent().getStringExtra("name");
            String phone = getIntent().getStringExtra("phone");
            String feedback = getIntent().getStringExtra("feedback");

            setIntent(name, phone, feedback);
        }
    }

    private void setIntent(String name, String phone, String feedback){
        EditText name1 = findViewById(R.id.editName);
        EditText phone1 = findViewById(R.id.editPhone);
        EditText feedback1 = findViewById(R.id.editFeedback);

        name1.setText(name);
        phone1.setText(phone);
        feedback1.setText(feedback);
    }
}