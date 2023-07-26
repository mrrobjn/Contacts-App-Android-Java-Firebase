package com.example.votruongdung_4842;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {
    Firebase fb = new Firebase();
    TextView emailTv;
    ImageButton returnBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        emailTv = findViewById(R.id.email_textview);
        returnBtn = findViewById(R.id.return_home_btn2);

        emailTv.setText(fb.currentUser.getEmail());
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(AddContactActivity.this, HomeActivity.class);
                startActivity(t);
            }
        });
    }
}
