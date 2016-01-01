package com.purdueplanner.purdueplanner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.util.Calendar;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;


/**
 * Created by menane on 10/13/15.
 */

public class ScheduleActivity extends FragmentActivity {

    Date prevDate = new Date();

    @Override
    //provide the onCreate method to apply the Schedule layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        prevDate = new Date();
        Button viewClassesButton = (Button)findViewById(R.id.viewClassesButton);
        viewClassesButton.setBackgroundResource(android.R.drawable.btn_default);

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Intent intent = new Intent(getApplicationContext(), viewClasses.class).putExtra("VisibleDays", 1).putExtra("Day", date);
                startActivity(intent);

            }
        };

        caldroidFragment.setCaldroidListener(listener);

        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendarView, caldroidFragment);
        t.commit();




    }

    public void buttonOnClick(View v) {
        Button button = (Button) v;
        Intent intent = new Intent(this, viewClasses.class).putExtra("VisibleDays", 7);
        startActivity(intent);
    }

    //method for getting the single letter format from the actuday day, i.e. "Monday"
    public String getLetter(String day){
        String letter = "";
        switch(day){
            case "Monday": letter = "M";
                break;
            case "Tuesday": letter = "T";
                break;
            case "Wednesday": letter = "W";
                break;
            case "Thursday": letter = "R";
                break;
            case "Friday": letter = "F";
                break;
            case "Saturday": letter = "S";
                break;
            case "Sunday": letter = "U";
                break;
            //Supposedly there is no classes offered on Sunday's at Purdue, so I won't account for it
        }
        return letter;
    }


}
