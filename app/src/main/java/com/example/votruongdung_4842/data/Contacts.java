package com.example.votruongdung_4842.data;


import java.util.Map;

public class Contacts {
    private String contactId;
    private String name;
    private String phoneNumber;
    private String imgUrl;
    public Contacts() {}

    public Contacts(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.phoneNumber = (String) map.get("phone");
        this.contactId = (String) map.get("contactId");
        this.imgUrl = (String) map.get("photo");
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

