package com.example.votruongdung_4842;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FireBase {
    private FirebaseFirestore db;

    public FireBase() {
        db = FirebaseFirestore.getInstance();
    }

    public void getUsers(final OnUsersReadyCallback callback) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Map<String, Object>> users = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                users.add(document.getData());
                            }
                            callback.onUsersReady(users);
                        } else {
                            Log.w("err", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public interface OnUsersReadyCallback {
        void onUsersReady(List<Map<String, Object>> users);
    }
}



