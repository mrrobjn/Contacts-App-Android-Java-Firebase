package com.example.votruongdung_4842;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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

        Contacts contact = getItem(position);

        if (contact != null) {
            nameTextView.setText(contact.getName());
        }

        return convertView;
    }

    public void updateData(List<Contacts> contactsList) {
        clear();
        addAll(contactsList);
        notifyDataSetChanged();
    }
}



