package com.example.votruongdung_4842;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    TextView tvname;
    ImageView imgv;
    private ListView contactsListView;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Firebase fb = new Firebase();
        String name = fb.currentUser.getDisplayName();
        String url = fb.currentUser.getPhotoUrl().toString();

        tvname = findViewById(R.id.user_name);
        imgv = findViewById(R.id.imageView);

        tvname.setText(name);

        Glide.with(this)
                .load(url)
                .into(imgv);

        contactsListView = findViewById(R.id.contacts_list);
        contactsAdapter = new ContactsAdapter(this, new ArrayList<Contacts>());
        contactsListView.setAdapter(contactsAdapter);

        fb.db.collection("contacts")
                .whereEqualTo("userId", fb.currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Contacts> contactsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<Map<String, Object>> list = (List<Map<String, Object>>) document.get("list");
                                for (Map<String, Object> map : list) {
                                    Contacts contacts = new Contacts();
                                    contacts.setName((String) map.get("name"));

                                    // Set other fields as needed
                                    contactsList.add(contacts);
                                }
                            }
                            Collections.sort(contactsList, new Comparator<Contacts>() {
                                @Override
                                public int compare(Contacts c1, Contacts c2) {
                                    return c1.getName().compareTo(c2.getName());
                                }
                            });
                            contactsAdapter.updateData(contactsList);
                        } else {
                            Log.w("MainActivity2", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}

