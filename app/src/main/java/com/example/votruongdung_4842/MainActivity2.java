package com.example.votruongdung_4842;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
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

        tvname = findViewById(R.id.textView4);
        imgv = findViewById(R.id.imageView);

        tvname.setText(name);

        Glide.with(this)
                .load(url)
                .into(imgv);

        contactsListView = findViewById(R.id.contacts_list);
        contactsAdapter = new ContactsAdapter(this, new ArrayList<Contacts>());
        contactsListView.setAdapter(contactsAdapter);

        fb.db.collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Contacts> contactsList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contacts contact = document.toObject(Contacts.class);
                                contactsList.add(contact);
                            }
                            contactsAdapter.updateData(contactsList);
                        } else {
                            Log.w("MainActivity2", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}

