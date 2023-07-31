package com.example.votruongdung_4842.data;

import java.util.Map;

public class ContactDetail {
    private String name;
    private String phone;
    private String email;
    private String imgUrl;

    public ContactDetail(Map<String, Object> map) {
        this.setName((String) map.get("name"));
        this.setPhone((String) map.get("phone"));
        this.setEmail((String) map.get("email"));
        this.setImgUrl((String) map.get("photo"));
    }

    public ContactDetail(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
