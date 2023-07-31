package com.example.votruongdung_4842;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.votruongdung_4842.adapters.ContactsAdapter;
import com.example.votruongdung_4842.data.Contacts;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    TextView tvname;
    ImageButton imgBtn;
    Button addBtn;
    private ExpandableListView contactsListView;
    private ContactsAdapter contactsAdapter;
    Firebase fb = new Firebase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("MainActivity2", "Listen failed.", error);
                        return;
                    }

                    Map<String, List<Contacts>> contactsMap1 = new HashMap<>();
                    for (QueryDocumentSnapshot document : value) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) document.get("list");
                        for (Map<String, Object> map : list) {
                            Contacts contacts = new Contacts(map);

                            // Set other fields as needed
                            String firstLetter = contacts.getName().substring(0, 1).toUpperCase();
                            if (!contactsMap1.containsKey(firstLetter)) {
                                contactsMap1.put(firstLetter, new ArrayList<>());
                            }
                            contactsMap1.get(firstLetter).add(contacts);
                        }
                    }
                    for (List<Contacts> contactsList : contactsMap1.values()) {
                        Collections.sort(contactsList, new Comparator<Contacts>() {
                            @Override
                            public int compare(Contacts c1, Contacts c2) {
                                return c1.getName().compareTo(c2.getName());
                            }
                        });
                    }
                    contactsAdapter.updateData(contactsMap1);

                    // Expand all groups
                    int count = contactsAdapter.getGroupCount();
                    for (int i = 0; i < count; i++) {
                        contactsListView.expandGroup(i);
                    }
                });

        imgBtn.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(HomeActivity.this, imgBtn);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.signout_item) {
                    fb.auth.signOut();
                    Toast.makeText(HomeActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    Intent t = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(t);
                }
                return true;
            });
            popup.show();
        });
        contactsListView.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, l) -> {
            Contacts contact = (Contacts) contactsAdapter.getChild(groupPosition, childPosition);

            Intent intent = new Intent(HomeActivity.this, ContactDetailActivity.class);
            intent.putExtra("contactId", contact.getContactId());
            startActivity(intent);
            return true;
        });

        contactsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Get contactId from item at position
            long packedPosition = contactsListView.getExpandableListPosition(position);
            int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
            int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
            Contacts contact = (Contacts) contactsAdapter.getChild(groupPosition, childPosition);
            String contactId = contact.getContactId();

            // Show pop-up menu
            PopupMenu popup = new PopupMenu(HomeActivity.this, view);
            popup.getMenuInflater().inflate(R.menu.popup_menu_2, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete_item) {
                    deleteContact(contactId);
                }
                return true;
            });
            popup.show();
            return true;
        });

        addBtn.setOnClickListener(v -> {
            Intent t = new Intent(HomeActivity.this, AddContactActivity.class);
            startActivity(t);
        });

    }
    protected void deleteContact(String contactId){
        CollectionReference contactsRef = fb.db.collection("contacts");
        contactsRef
                .whereEqualTo("userId", fb.currentUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        List<Map<String, Object>> list = (List<Map<String, Object>>) document.get("list");
                        if (list != null) {
                            Map<String, Object> objectToDelete = null;
                            for (Map<String, Object> item : list) {
                                if (item.get("contactId").equals(contactId)) {
                                    objectToDelete = item;
                                    break;
                                }
                            }
                            if (objectToDelete != null) {
                                document.getReference().update("list", FieldValue.arrayRemove(objectToDelete));
                            }
                        }
                    }
                });
    }
}


