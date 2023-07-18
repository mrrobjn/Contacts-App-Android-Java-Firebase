package com.example.votruongdung_4842;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Contacts {
    String userId;
    String name;
    String phoneNumber;
    String dayCreated;
    Firebase fb = new Firebase();
    public Contacts(String name, String phoneNumber, String dayCreated, String userId) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dayCreated = dayCreated;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDayCreated() {
        return dayCreated;
    }

    public void setDayCreated(String dayCreated) {
        this.dayCreated = dayCreated;
    }
    public  void createContact(String userId, String name, String phoneNumber, String dayCreated){
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("name", name);
        data.put("phoneNumber", phoneNumber);
        data.put("dayCreated", dayCreated);

        fb.db.collection("contacts").add(data);

    }
    public void deleteContact(){

    }

}
