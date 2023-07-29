package com.example.votruongdung_4842;

import android.net.Uri;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private boolean isValidName(String name) {
        String regex = "[a-zA-Z]+\\.?";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();
    }
    private boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return Patterns.PHONE.matcher(phone).matches();
    }
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public String validator(Uri selectedImage , String name, String phone, String email) {
        String error = "";
        switch (0) {
            case 0:
                if (selectedImage == null) {
                    error = "Vui lòng up ảnh";
                    break;
                }
            case 1:
                if (!isValidName(name)) {
                    error = "Họ tên không hợp lệ";
                    break;
                }
            case 2:
                if (!isValidPhone(phone)) {
                    error = "Số điện thoại không hợp lệ";
                    break;
                }
            case 3:
                if (!isValidEmail(email)) {
                    error = "Email không hợp lệ";
                    break;
                }
            default:
                error = "";
        }
        return error;
    }

}
