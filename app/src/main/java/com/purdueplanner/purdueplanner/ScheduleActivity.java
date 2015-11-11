package com.purdueplanner.purdueplanner;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by menane on 10/13/15.
 */

public class ScheduleActivity extends FragmentActivity {

    private String[] testArray = {"CS 354", "CS 252", "CS 348", "CS 391", "CS 307", "CS 381", "CS 250"};
    private ArrayList<Classes> studentClasses = new ArrayList<Classes>();
    private GridView weekListView;
    private ArrayAdapter arrayAdapter;

    @Override
    //provide the onCreate method to apply the Schedule layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: not have this crash the code
//        //Implements the action bar
//        android.app.ActionBar ab = getActionBar();
//        getActionBar().setTitle("Schedule");
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_schedule);

        //Sets the week list view for the week
        weekListView = (GridView) findViewById(R.id.weekdayView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, testArray);
        weekListView.setAdapter(arrayAdapter);
        System.out.println("Hello");
    }




}
