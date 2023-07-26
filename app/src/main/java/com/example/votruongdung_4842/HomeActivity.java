package com.example.votruongdung_4842;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.votruongdung_4842.adapters.ContactsAdapter;
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

public class HomeActivity extends AppCompatActivity {
    TextView tvname;
    ImageView imgv;
    Button addBtn;
    private ExpandableListView contactsListView;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Firebase fb = new Firebase();
        String name = fb.currentUser.getDisplayName();
        String url = fb.currentUser.getPhotoUrl().toString();

        tvname = findViewById(R.id.user_name);
        imgv = findViewById(R.id.imageView);
        contactsListView = findViewById(R.id.contacts_list);
        addBtn = findViewById(R.id.add_contact_btn);

        tvname.setText(name);

        Glide.with(this)
                .load(url)
                .into(imgv);

        Map<String, List<Contacts>> contactsMap = new HashMap<>();
        contactsAdapter = new ContactsAdapter(this, contactsMap);
        contactsListView.setAdapter(contactsAdapter);

        fb.db.collection("contacts")
                .whereEqualTo("userId", fb.currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, List<Contacts>> contactsMap = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<Map<String, Object>> list = (List<Map<String, Object>>) document.get("list");
                                for (Map<String, Object> map : list) {
                                    Contacts contacts = new Contacts();
                                    contacts.setName((String) map.get("name"));
                                    contacts.setPhoneNumber((String) map.get("phone"));
                                    contacts.setContactId((String) map.get("contactId"));

                                    // Set other fields as needed
                                    String firstLetter = contacts.getName().substring(0, 1).toUpperCase();
                                    if (!contactsMap.containsKey(firstLetter)) {
                                        contactsMap.put(firstLetter, new ArrayList<>());
                                    }
                                    contactsMap.get(firstLetter).add(contacts);
                                }
                            }
                            for (List<Contacts> contactsList : contactsMap.values()) {
                                Collections.sort(contactsList, new Comparator<Contacts>() {
                                    @Override
                                    public int compare(Contacts c1, Contacts c2) {
                                        return c1.getName().compareTo(c2.getName());
                                    }
                                });
                            }
                            contactsAdapter.updateData(contactsMap);

                            // Expand all groups
                            int count = contactsAdapter.getGroupCount();
                            for (int i = 0; i < count; i++) {
                                contactsListView.expandGroup(i);
                            }
                        } else {
                            Log.w("MainActivity2", "Error getting documents.", task.getException());
                        }
                    }
                });
        contactsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Contacts contact = (Contacts) contactsAdapter.getChild(groupPosition, childPosition);

                Intent intent = new Intent(HomeActivity.this, ContactDetailActivity.class);
                intent.putExtra("contactId", contact.getContactId());
                startActivity(intent);
                return true;
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(HomeActivity.this, AddContactActivity.class);
                startActivity(t);
            }
        });
    }}


