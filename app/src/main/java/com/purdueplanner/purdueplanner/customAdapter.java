package com.purdueplanner.purdueplanner;

/**
 * Created by Kenny on 10/17/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Kenny on 10/17/2015.
 */
public class customAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<HashMap<String, String>> list = new ArrayList();
    private Context context;
    private View view;
    public customAdapter(ArrayList<HashMap<String, String>> list, Context context) {
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
        return 0;
    }

    public void clearList()
    {
        list.clear();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sample_class_button, null);
        }

        //Handle TextView and display string from your list
        final TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        String listText = list.get(position).get("Major") + " "  + list.get(position).get("Course");
        listItemText.setText(listText);

        //Handle buttons and add onClickListeners
        Button moreInfoBtn = (Button)view.findViewById(R.id.moreInfo_btn);
        moreInfoBtn.setBackgroundResource(android.R.drawable.btn_default);

        moreInfoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent init = new Intent(context, ClassInformationActivity.class);
                init.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                init.putExtra("Database Info", list.get(position));
                context.startActivity(init);
                notifyDataSetChanged();
            }
        });


        return view;
    }
}

