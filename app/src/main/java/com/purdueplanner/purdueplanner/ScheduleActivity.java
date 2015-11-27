package com.purdueplanner.purdueplanner;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.facebook.FacebookRequestError;


/**
 * Created by menane on 10/13/15.
 */

public class ScheduleActivity extends FragmentActivity {

    @Override
    //provide the onCreate method to apply the Schedule layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        CalendarView v = (CalendarView)findViewById(R.id.calendarView); //gets the calendar



//        Commented out bc I'm not sure if we want to serialize the list of classes to be able to be access offline or not
//        if(StartActivity.isConnected == false){ //TODO: implement functionality
//            //TODO: Implement actions for button
//            Toast.makeText(getApplicationContext(), "Lost connect.", Toast.LENGTH_LONG).show();
//            AlertDialog alertDialog = new AlertDialog.Builder(StartActivity.this).create();
//            alertDialog.setTitle("Alert");
//            alertDialog.setMessage("Oops! Looks like you aren't connected to Wifi or a mobile network at the moment. Would you like to connect or exit?");
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
//        }
        //DEFAULT: Gets the current date to be displayed on the screen.
        //Almost the same stuff inside of the onSetChangeListener... just for the current date

//        Calendar c1 = Calendar.getInstance(); //makes an instance of a calendar
//        SimpleDateFormat s1 = new SimpleDateFormat("MM-dd-yyyy");
//        Date date = new Date(); //this class is deprecated technically so we should change it to Calendar class
//        String currentDate = s1.format(date); //formats the selected date
//        TextView dateText1 = (TextView)this.findViewById(R.id.selected_date);
//        dateText1.setText(currentDate);


        //listens to see the selected date from the user
        v.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Gets NUMERICAL date
                view.findViewById(R.id.calendarView);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy"); //makes the date format for the date - month, day, and year
                Calendar c = Calendar.getInstance(); //makes an instance of a calendar object
                c.set(year, month, dayOfMonth); //sets the year, month and date based on what the user selected
                String currdate = dateFormat.format(c.getTime()); //formats the date based on our date format
                TextView dateText = (TextView) findViewById(R.id.selected_date); //sets the date text to the one on our xml file
                dateText.setText(currdate);

                //Gets the day from the date
                TextView dayText = (TextView) findViewById(R.id.selected_day); //sets the day to the first letter abbreviation
                SimpleDateFormat newDateFormat = new SimpleDateFormat("EEEEE", Locale.getDefault()); //Formats the day to display the full word.. i.e. "Monday"
                String actualDay = newDateFormat.format(c.getTime()); //gets the time to adhere to the format

                //Since we got the day, let's make it match the databases one letter formatting
                String letter = getLetter(actualDay);
                dayText.setText(letter);


                //in order to pass the 'currdate' (and the day letter) string in using a new Activity, we have to do this
                Intent myIntent = new Intent(ScheduleActivity.this, DayScheduleActivity.class);
                myIntent.putExtra("dayletter_key", letter); //adds the string to a HashMap like object
                startActivity(myIntent); //goes to new activity once the button is pressed
            }




        });

    }

    public void buttonOnClick(View v) {
        Button button = (Button) v;
        Intent intent = new Intent(this, viewClasses.class);
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
