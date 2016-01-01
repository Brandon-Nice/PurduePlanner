package com.purdueplanner.purdueplanner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class viewClasses extends AppCompatActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener {

   // private String[] testArray = {"A", "B", "C", "D"};
    private ListView classListView;
    private ArrayAdapter adapter;
    private WeekView mWeekView;
    ArrayList<Classes> classList = new ArrayList<>();
    int visibleDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        visibleDays = (int) intent.getExtras().get("VisibleDays");


        classList = ((MyApplication) getApplication()).getStudent().getSchedule();





        final String currDay = (String) android.text.format.DateFormat.format("EEEE", new Date());

        int minTime = 100;
        if (visibleDays == 7)
        {
            setContentView(R.layout.view_classes_7);
            mWeekView = (WeekView) findViewById(R.id.weekView);
            mWeekView.setHourHeight(mWeekView.getHourHeight() * 2);
            mWeekView.setFirstDayOfWeek(Calendar.SUNDAY);
            GregorianCalendar day = new GregorianCalendar();
            day.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            mWeekView.goToDate(day);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            for (int i = 0; i < classList.size(); i++) {
                if (!classList.get(i).getStartTime().equals("TBA")) {
                    HashMap<String, Integer> times = interpretTime(classList.get(i).getStartTime());
                    if (minTime > times.get("Hour")) {
                        minTime = times.get("Hour");
                    }
                }
            }
            mWeekView.goToHour(minTime);
        }
        else
        {
            setContentView(R.layout.view_classes_1);
            mWeekView = (WeekView) findViewById(R.id.weekView);
            mWeekView.setHourHeight(mWeekView.getHourHeight() * 4);
            Calendar day = Calendar.getInstance();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            day.set(Calendar.DAY_OF_WEEK, ((Date) intent.getExtras().get("Day")).getDay() + 1);
            mWeekView.goToDate(day);

            String letter = null;
            if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            {
                letter = "U";
            }
            else if (day.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
            {
                letter = "M";
            }
            else if (day.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
            {
                letter = "T";
            }
            else if (day.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
            {
                letter = "W";
            }
            else if (day.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
            {
                letter = "R";
            }
            else if (day.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
            {
                letter = "F";
            }
            else if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            {
                letter = "S";
            }
            for (int i = 0; i < classList.size(); i++)
            {
                if (classList.get(i).getDays().contains(letter))
                {
                    if (!classList.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(classList.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            mWeekView.goToHour(minTime);
        }

        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);
        mWeekView.setXScrollingSpeed(0);
        mWeekView.setNumberOfVisibleDays(visibleDays);
        setupDateTimeInterpreter(true);








    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
                String weekday = sdf.format(date.getTime());
                return weekday;
            }

            @Override
            public String interpretTime(int hour, int minutes) {
                String strMinutes = String.format("%02d", minutes);
                if (hour == 12)
                {
                    return "12:" + strMinutes + " PM";
                }
                else if (hour > 11) {
                    return (hour - 12) + ":" + strMinutes + " PM";
                } else {
                    if (hour == 0) {
                        return "12:" + strMinutes + " AM";
                    } else {
                        return hour + ":" + strMinutes + " AM";
                    }
                }
            }
        });
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Intent init = new Intent(viewClasses.this, ClassInformationActivity.class);
        init.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        HashMap<String, String> databaseClassInfo = new HashMap<>();
        databaseClassInfo.put("Major", classList.get((int) event.getId()).getMajor());
        databaseClassInfo.put("Course", classList.get((int) event.getId()).getCourseNum());
        databaseClassInfo.put("Section", classList.get((int) event.getId()).getSectionNum());
        databaseClassInfo.put("Start Time", classList.get((int) event.getId()).getStartTime());
        databaseClassInfo.put("End Time", classList.get((int) event.getId()).getEndTime());
        init.putExtra("Database Info", databaseClassInfo);
        init.putExtra("VisibleDays", (int) getIntent().getExtras().get("VisibleDays"));
        init.putExtra("Day", (Date) getIntent().getExtras().get("Day"));
        startActivity(init);
        finish();

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (int i = 0; i < classList.size(); i++)
        {
            if (!classList.get(i).getStartTime().equals("TBA")) {
                HashMap<String, Integer> interpretedStartTime = interpretTime(classList.get(i).getStartTime());
                HashMap<String, Integer> interpretedEndTime = interpretTime(classList.get(i).getEndTime());
                ArrayList<Integer> classDays = getDay(classList.get(i).getDays());
                for (int j = 0; j < classDays.size(); j++) {
                    Calendar startTime = Calendar.getInstance();
                    startTime.set(Calendar.HOUR_OF_DAY, interpretedStartTime.get("Hour"));
                    startTime.set(Calendar.MINUTE, interpretedStartTime.get("Minutes"));
                    startTime.set(Calendar.MONTH, newMonth - 1);
                    startTime.set(Calendar.YEAR, newYear);
                    startTime.set(Calendar.DAY_OF_WEEK, classDays.get(j));
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(Calendar.HOUR_OF_DAY, interpretedEndTime.get("Hour"));
                    endTime.set(Calendar.MINUTE, interpretedEndTime.get("Minutes"));
                    endTime.set(Calendar.MONTH, newMonth - 1);
                    endTime.set(Calendar.YEAR, newYear);
                    endTime.set(Calendar.DAY_OF_WEEK, classDays.get(j));
                    WeekViewEvent event = null;
                    if (visibleDays == 7) {
                        event = new WeekViewEvent(i, classList.get(i).getMajor() + " " + classList.get(i).getCourseNum(), startTime, endTime);
                    } else {
                        event = new WeekViewEvent(i, classList.get(i).getMajor() + " " + classList.get(i).getCourseNum()
                                + "\n" + classList.get(i).getLocation() + "\n" + classList.get(i).getStartTime() + " - "
                                + classList.get(i).getEndTime(), startTime, endTime);
                    }
                    event.setColor(getResources().getColor(R.color.caldroid_black));
                    events.add(event);
                }
            }

        }

        return events;
    }

    public HashMap<String, Integer> interpretTime(String time)
    {
        int firstColon = time.indexOf(":");
        int firstSpace = time.indexOf(" ");
        int hour = Integer.parseInt(time.substring(0, firstColon));
        int minutes = Integer.parseInt(time.substring(firstColon + 1, firstSpace));
        if (time.contains("pm") && hour < 12)
        {
            hour += 12;
        }
        HashMap<String, Integer> interpretedTime = new HashMap<>();
        interpretedTime.put("Hour", hour);
        interpretedTime.put("Minutes", minutes);
        return interpretedTime;
    }

    public ArrayList<Integer> getDay(String days)
    {
        ArrayList<Integer> classDays = new ArrayList<>();
        if (days.contains("M"))
        {
            classDays.add(Calendar.MONDAY);
        }
        if (days.contains("T"))
        {
            classDays.add(Calendar.TUESDAY);
        }
        if (days.contains("W"))
        {
            classDays.add(Calendar.WEDNESDAY);
        }
        if (days.contains("R"))
        {
            classDays.add(Calendar.THURSDAY);
        }
        if (days.contains("F"))
        {
            classDays.add(Calendar.FRIDAY);
        }
        if (days.contains("S"))
        {
            classDays.add(Calendar.SATURDAY);
        }
        if (days.contains("U"))
        {
            classDays.add(Calendar.SUNDAY);
        }
        return classDays;
    }
}
