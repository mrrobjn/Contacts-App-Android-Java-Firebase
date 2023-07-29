package com.example.votruongdung_4842;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2;

    Firebase fb = new Firebase();
    Validator validator = new Validator();
    TextView emailTv;
    ImageButton returnBtn, uploadAvt;
    Button saveBtn;
    EditText nameEdt, phoneEdt, emailEdt;
    Uri selectedImage;

    ImageView userImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        FirebaseApp.initializeApp( this);
        fb.firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        emailTv = findViewById(R.id.email_textview);
        returnBtn = findViewById(R.id.return_home_btn2);
        uploadAvt = findViewById(R.id.upload_avatar_btn);
        userImage = findViewById(R.id.imageView5);
        saveBtn = findViewById(R.id.save_button);

        nameEdt = findViewById(R.id.editTextTextPersonName);
        phoneEdt = findViewById(R.id.editTextTextPersonName2);
        emailEdt = findViewById(R.id.editTextTextPersonName3);

        emailTv.setText(fb.currentUser.getEmail());
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(AddContactActivity.this, HomeActivity.class);
                startActivity(t);
            }
        });
        String url = "https://img.icons8.com/?size=512&id=98957&format=png";
        String url2 = "https://icon-library.com/images/add-camera-icon/add-camera-icon-13.jpg";
        Glide.with(this)
                .load(url)
                .into(userImage);
        Glide.with(this)
                .load(url2)
                .circleCrop()
                .into(uploadAvt);

        uploadAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReadExternalStoragePermission();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String name = nameEdt.getText().toString();
                    String phone = phoneEdt.getText().toString();
                    String email = emailEdt.getText().toString();
                    // Trien khai ham validate
                    if(validator.validator(selectedImage, name, phone, email)==""){
                        Uri file = selectedImage;
                        StorageReference storageRef = fb.storage.getReference();
                        StorageReference imageRef = storageRef.child("photos/"+fb.currentUser.getUid()+"/"+ file.getLastPathSegment());
                        UploadTask uploadTask = imageRef.putFile(file);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        String contactId = fb.db.collection("contacts").document().getId();
                                        addContact(contactId, name, phone, email, imageUrl);
                                    }
                                });
                            }
                        });
                    }
                    else{
                        Toast.makeText(AddContactActivity.this, validator.validator(
                                selectedImage,
                                name,
                                phone,
                                email), Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            int targetWidth = (int) (100 * getResources().getDisplayMetrics().density);
            int targetHeight = (int) (100 * getResources().getDisplayMetrics().density);

            Glide.with(this)
                    .asBitmap()
                    .load(picturePath)
                    .override(targetWidth, targetHeight)
                    .circleCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            uploadAvt.setImageBitmap(resource);
                        }
                    });
        }
    }

    private void requestReadExternalStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Yêu cầu quyền truy cập")
                    .setMessage("This permission is needed to access photos from your device.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddContactActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
    }
    protected void addContact(String contactId, String name, String phone, String email, String imageUrl){
        Map<String, Object> newContact = new HashMap<>();
        newContact.put("contactId", contactId);
        newContact.put("name", name);
        newContact.put("phone", phone);
        newContact.put("email", email);
        newContact.put("photo", imageUrl);

        fb.db.collection("contacts")
                .whereEqualTo("userId", fb.currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference docRef = document.getReference();
                                docRef.update("list", FieldValue.arrayUnion(newContact));
                                Intent t = new Intent(AddContactActivity.this, HomeActivity.class);
                                startActivity(t);
                                Toast.makeText(AddContactActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
