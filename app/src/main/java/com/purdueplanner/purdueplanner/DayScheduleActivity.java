package com.purdueplanner.purdueplanner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;


/**
 * Created by menane on 11/17/15.
 */

//What to do when a day is clicked on the calendar view
public class DayScheduleActivity extends AppCompatActivity {
    private ListView classesForDayListView;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_schedule);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent myIntent = getIntent(); // gets the previously created intent
        String dayLetter = myIntent.getStringExtra("dayletter_key"); // will return the value associated with this key, i.e. the day letter string

        //Get the actual classes for each day
        ArrayList<Classes> classList = ((MyApplication) getApplication()).getStudent().getSchedule(); //total classes for the student
        ArrayList<String> classPerDayList = new ArrayList<>(); //classes per each day for the student
        int size = classList.size();

        int classLength;
        int lengthBuffer = 11;
        String spaces = "";

        for (int i = 0; i < size; i++) { //iterates through the classes
            classLength = (classList.get(i).getMajor() + " " + classList.get(i).getCourseNum()).length();
            spaces = "";
            for (int s = classLength; s < lengthBuffer; s++) {
                spaces += " ";
            }
                if(classList.get(i).getDays().contains(dayLetter)){ //match by the day
                    classPerDayList.add(classList.get(i).getMajor() + " " + classList.get(i).getCourseNum() + spaces + classList.get(i).getStartTime()
                            + " - " + classList.get(i).getEndTime());
                }
            classLength = 0;
        }

        //now add the list to the xml file
        classesForDayListView = (ListView) findViewById(R.id.classesForDayView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, classPerDayList);
        classesForDayListView.setAdapter(adapter);

    }
}
