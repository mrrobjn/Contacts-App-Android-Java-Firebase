package com.example.votruongdung_4842.data;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Users {
//    Firebase firebase = new Firebase();

    String userId, name, profile;
    public Users(String userId, String name, String profile) {
        this.userId = userId;
        this.name = name;
        this.profile = profile;
    }
    public Users(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

//    public void createUser(String userId, String name, String photoUrl){
//        Map<String, Object> data = new HashMap<>();
//        data.put("userId", userId);
//        data.put("name", name);
//        data.put("photoUrl", photoUrl);
//        firebase.db.collection("users")
//                .whereEqualTo("userId", userId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if (task.getResult().isEmpty()) {
//                                // Add new document
//                                firebase.db.collection("users").add(data);
//                            } else {
//                                // Document with userId already exists
//                            }
//                        } else {
//                            // Handle error
//                        }
//                    }
//                });
//    }
}



