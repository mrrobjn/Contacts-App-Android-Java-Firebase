package com.example.votruongdung_4842;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contacts> {

    public ContactsAdapter(Context context, List<Contacts> contactsList) {
        super(context, 0, contactsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items, parent, false);

        }

        TextView nameTextView = convertView.findViewById(R.id.contact_name);
//        ImageView imgv = convertView.findViewById(R.id.imageView);

        Contacts contact = getItem(position);

        if (contact != null) {
            nameTextView.setText(contact.getName());
//            contact.setImgUrl("https://th.bing.com/th/id/OIP.1yoSL-WO0YU5mQKROudvswHaHa?w=200&h=200&c=7&r=0&o=5&pid=1.7");
//            Glide.with(getContext())
//                    .load(contact.getImgUrl())
//                    .into(imgv);
        }
        return convertView;
    }

    public void updateData(List<Contacts> contactsList) {
        clear();
        addAll(contactsList);
        notifyDataSetChanged();
    }
}



