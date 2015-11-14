package com.purdueplanner.purdueplanner;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by menane on 10/13/15.
 */

public class ScheduleActivity extends FragmentActivity {

    public final static String EXTRA_MESSAGE = "testing this shit";

    @Override
    //provide the onCreate method to apply the Schedule layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        //DEFAULT: Gets the current date to be displayed on the screen.
        //Almost the same stuff inside of the onSetChangeListener... just for the current date
        CalendarView v = (CalendarView)findViewById(R.id.calendarView); //gets the calendar
        Calendar c1 = Calendar.getInstance(); //makes an instance of a calendar
        SimpleDateFormat s1 = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date(); //this class is deprecated technically so we should change it to Calendar class
        String currentDate = s1.format(date); //formats the selected date
        TextView dateText1 = (TextView)this.findViewById(R.id.selected_date);
        dateText1.setText(currentDate);

        //listens to see the selected date from the user
        v.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth){
                view.findViewById(R.id.calendarView);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy"); //makes the date format for the date - month, day, and year
                Calendar c = Calendar.getInstance(); //makes an instance of a calendar object
                c.set(year,month,dayOfMonth); //sets the year, month and date based on what the user selected
                String currdate =  dateFormat.format(c.getTime()); //formats the date based on our date format
                TextView dateText = (TextView)findViewById(R.id.selected_date); //the
                dateText.setText(currdate);
            }
        });
    }

    public void buttonOnClick(View v) {
        Button button = (Button) v;
        Intent intent = new Intent(this, viewClasses.class);
        startActivity(intent);
    }




}
