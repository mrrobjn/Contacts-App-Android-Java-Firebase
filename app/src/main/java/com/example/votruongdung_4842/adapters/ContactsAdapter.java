package com.example.votruongdung_4842.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.votruongdung_4842.R;
import com.example.votruongdung_4842.data.Contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ContactsAdapter extends BaseExpandableListAdapter {
    private Context context;
    private Map<String, List<Contacts>> contactsMap;
    private List<String> groupList;

    public ContactsAdapter(Context context, Map<String, List<Contacts>> contactsMap) {
        this.context = context;
        this.contactsMap = contactsMap;
        this.groupList = new ArrayList<>(contactsMap.keySet());
        Collections.sort(groupList);
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String group = groupList.get(groupPosition);
        return contactsMap.get(group).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String group = groupList.get(groupPosition);
        return contactsMap.get(group).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        String group = (String) getGroup(groupPosition);
        textView.setText(group);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.items, parent, false);
        }
        TextView nameTextView = convertView.findViewById(R.id.contact_name);
        TextView phoneTextView = convertView.findViewById(R.id.contact_phone);

        Contacts contacts = (Contacts) getChild(groupPosition, childPosition);

        if (contacts != null) {
            nameTextView.setText(contacts.getName());
            phoneTextView.setText("+84 " + removeFirstZero(contacts.getPhoneNumber()));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void updateData(Map<String, List<Contacts>> contactsMap) {
        this.contactsMap = contactsMap;
        this.groupList = new ArrayList<>(contactsMap.keySet());
        Collections.sort(groupList);
        notifyDataSetChanged();
    }

    private String removeFirstZero(String input) {
        if (input.length() > 0 && input.charAt(0) == '0') {
            return input.substring(1);
        }
        return input;
    }
}




