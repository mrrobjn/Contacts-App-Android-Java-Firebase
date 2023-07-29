package com.example.votruongdung_4842;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.votruongdung_4842.adapters.ContactsAdapter;
import com.example.votruongdung_4842.data.ContactDetail;
import com.example.votruongdung_4842.data.Contacts;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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
import android.Manifest;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    TextView tvname;
    ImageButton imgBtn;
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
        imgBtn = findViewById(R.id.imageButton);
        contactsListView = findViewById(R.id.contacts_list);
        addBtn = findViewById(R.id.add_contact_btn);

        tvname.setText(name);

        int targetWidth = (int) (50 * getResources().getDisplayMetrics().density);
        int targetHeight = (int) (50 * getResources().getDisplayMetrics().density);

        Glide.with(this)
                .load(url)
                .override(targetWidth, targetHeight)
                .circleCrop()
                .into(imgBtn);

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
                                    contacts.setImgUrl((String) map.get("photo"));

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
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomeActivity.this, imgBtn);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.signout_item) {
                            fb.auth.signOut();
                            Toast.makeText(HomeActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                            Intent t = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(t);
                        }
                        return true;
                    }
                });
                popup.show();
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
    }
}


