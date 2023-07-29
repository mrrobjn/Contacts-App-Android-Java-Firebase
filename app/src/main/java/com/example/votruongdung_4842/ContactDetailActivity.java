package com.example.votruongdung_4842;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.votruongdung_4842.data.ContactDetail;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ContactDetailActivity extends AppCompatActivity {
    Firebase fb = new Firebase();
    ContactDetail contactDetail = new ContactDetail();
    TextView name, phone, email;
    ImageButton returnBtn , callBtn, messageBtn, emailBtn;
    ImageView avatar;
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

        avatar = findViewById(R.id.imageView4);

        returnBtn.setOnClickListener(view -> {
            Intent t1 = new Intent(ContactDetailActivity.this, HomeActivity.class);
            startActivity(t1);
        });

        callBtn.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contactDetail.getPhone()));
            startActivity(callIntent);
        });

        messageBtn.setOnClickListener(view -> {
            Intent messageIntent = new Intent(Intent.ACTION_VIEW);
            messageIntent.setData(Uri.parse("sms:" + contactDetail.getPhone()));
            startActivity(messageIntent);
        });

        emailBtn.setOnClickListener(view -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + contactDetail.getEmail()));
            startActivity(emailIntent);
        });

        fb.db.collection("contacts")
                .whereEqualTo("userId", fb.currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<Map<String, Object>> list = (List<Map<String, Object>>) document.get("list");
                            for (Map<String, Object> map : list) {
                                if (Objects.equals(map.get("contactId"), contactId)) {
                                    contactDetail.setName((String) map.get("name"));
                                    contactDetail.setPhone((String) map.get("phone"));
                                    contactDetail.setEmail((String) map.get("email"));
                                    contactDetail.setImgUrl((String) map.get("photo"));

                                    name.setText(contactDetail.getName());
                                    phone.setText(contactDetail.getPhone());
                                    email.setText(contactDetail.getEmail());

                                    Glide.with(ContactDetailActivity.this)
                                            .load(contactDetail.getImgUrl())
                                            .circleCrop().into(avatar);

                                    return;
                                }
                            }
                        }
                    } else {
                        Log.w("ContactDetailActivity", "Error getting documents.", task.getException());
                    }
                });
    }
}
