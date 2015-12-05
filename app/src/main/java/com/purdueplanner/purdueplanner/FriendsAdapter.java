package com.purdueplanner.purdueplanner;

/**
 * Created by Kenny on 10/17/2015.
 */

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.*;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<HashMap<String, String>> list = new ArrayList();
    private Context context;
    private View view;
    public FriendsAdapter(ArrayList<HashMap<String, String>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).;
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView) view.findViewById(android.R.id.text1);
        String listText = list.get(position).get("firstName") + " "  + list.get(position).get("lastName");
        listItemText.setText(listText);


        return view;
    }
}

