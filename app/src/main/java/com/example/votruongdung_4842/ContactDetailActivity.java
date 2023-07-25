package com.example.votruongdung_4842;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votruongdung_4842.data.ContactDetail;
import com.example.votruongdung_4842.data.Contacts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactDetailActivity extends AppCompatActivity {
    Firebase fb = new Firebase();
    ContactDetail contactDetail = new ContactDetail();
    TextView name, phone, email;
    ImageButton returnBtn , callBtn, messageBtn, emailBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Intent t = getIntent();
        String contactId = t.getExtras().getString("contactId");

         name = findViewById(R.id.textView);
         phone = findViewById(R.id.textView3);
         email = findViewById(R.id.textView5);

        returnBtn = findViewById(R.id.return_home_btn);
        callBtn = findViewById(R.id.call_btn);
        messageBtn = findViewById(R.id.message_btn);
        emailBtn = findViewById(R.id.email_btn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(ContactDetailActivity.this, HomeActivity.class);
                startActivity(t);
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contactDetail.getPhone()));
                startActivity(callIntent);
            }
        });


        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(Intent.ACTION_VIEW);
                messageIntent.setData(Uri.parse("sms:" + contactDetail.getPhone()));
                startActivity(messageIntent);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + contactDetail.getEmail()));
                startActivity(emailIntent);
            }
        });

        fb.db.collection("contacts")
                .whereEqualTo("userId", fb.currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<Map<String, Object>> list = (List<Map<String, Object>>) document.get("list");
                                for (Map<String, Object> map : list) {
                                    if (map.get("contactId").equals(contactId)) {
                                        contactDetail.setName((String) map.get("name"));
                                        contactDetail.setPhone((String) map.get("phone"));
                                        contactDetail.setEmail((String) map.get("email"));
                                        name.setText(contactDetail.getName());
                                        phone.setText(contactDetail.getPhone());
                                        email.setText(contactDetail.getEmail());
                                        return;
                                    }
                                }
                            }
                        } else {
                            Log.w("MainActivity2", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
