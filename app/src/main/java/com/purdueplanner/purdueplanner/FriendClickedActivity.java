package com.purdueplanner.purdueplanner;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FriendClickedActivity extends AppCompatActivity implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener {

    private ListView friendView;
    private ArrayAdapter adapter;
    private WeekView mWeekView;
    private ArrayList<Classes> currentStudentClasses;
    private ArrayList<Classes> friendsClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_clicked);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //ArrayList<String> test = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("friendID");
        String firstName = extras.getString("first");
        String lastName = extras.getString("last");
        final String combinedName = firstName + " " + lastName;
        final String currentStudentName = ((MyApplication) getApplication()).getStudent().getFirstName();
        final TextView friendName = (TextView) findViewById(R.id.friendName);
        final TextView studentName = (TextView) findViewById(R.id.studentName);
        friendName.setText(firstName);
        friendName.setTextColor(getResources().getColor(R.color.ScheduleColor));
        studentName.setText(currentStudentName);
        studentName.setTextColor(getResources().getColor(R.color.caldroid_black));
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(FriendClickedActivity.this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(FriendClickedActivity.this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(FriendClickedActivity.this);
        mWeekView.setXScrollingSpeed(0);
        mWeekView.setNumberOfVisibleDays(1);
        mWeekView.setHourHeight(mWeekView.getHourHeight() * 4);
        setupDateTimeInterpreter(true);
        currentStudentClasses = ((MyApplication) getApplication()).getStudent().getSchedule();
        friendsClasses = new ArrayList();



        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> days = new ArrayList<>();
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        String currDay = (String) android.text.format.DateFormat.format("EEEE", new Date());
        if (currDay.equals("Sunday"))
        {
            spinner.setSelection(0);
        }
        else if (currDay.equals("Monday"))
        {
            spinner.setSelection(1);
        }
        else if (currDay.equals("Tuesday"))
        {
            spinner.setSelection(2);
        }
        else if (currDay.equals("Wednesday"))
        {
            spinner.setSelection(3);
        }
        else if (currDay.equals("Thursday"))
        {
            spinner.setSelection(4);
        }
        else if (currDay.equals("Friday"))
        {
            spinner.setSelection(5);
        }
        else if (currDay.equals("Saturday"))
        {
            spinner.setSelection(6);
        }




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(parent.getItemAtPosition(position));
                if (parent.getItemAtPosition(position).equals("Sunday")) {
                    Calendar day = Calendar.getInstance();
                    day.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    mWeekView.goToDate(day);

                    setMinTime("Sunday");
                } else if (parent.getItemAtPosition(position).equals("Monday")) {
                    Calendar day = Calendar.getInstance();
                    day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    mWeekView.goToDate(day);

                    setMinTime("Monday");
                } else if (parent.getItemAtPosition(position).equals("Tuesday")) {
                    Calendar day = Calendar.getInstance();
                    day.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    mWeekView.goToDate(day);

                    setMinTime("Tuesday");
                } else if (parent.getItemAtPosition(position).equals("Wednesday")) {
                    Calendar day = Calendar.getInstance();
                    day.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    mWeekView.goToDate(day);

                    setMinTime("Wednesday");
                } else if (parent.getItemAtPosition(position).equals("Thursday")) {
                    Calendar day = Calendar.getInstance();
                    day.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    mWeekView.goToDate(day);

                    setMinTime("Thursday");
                } else if (parent.getItemAtPosition(position).equals("Friday")) {
                    Calendar day = Calendar.getInstance();
                    day.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    mWeekView.goToDate(day);

                    setMinTime("Friday");
                } else if (parent.getItemAtPosition(position).equals("Saturday")) {
                    Calendar day = Calendar.getInstance();
                    day.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    mWeekView.goToDate(day);

                    setMinTime("Saturday");
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        final Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot);
                HashMap<String, Object> val = (HashMap) snapshot.getValue();
                boolean isScheduleHidden = false;
                if (val.get("isScheduleHidden") != null)
                {
                    isScheduleHidden = (boolean) val.get("isScheduleHidden");
                }
                if (isScheduleHidden == true) {
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.totalLayout);
                    rl.removeAllViews();
                    TextView hiddenFriend = new TextView(FriendClickedActivity.this);
                    hiddenFriend.setText(combinedName + " has decided not to share their schedule with you.");
                    rl.addView(hiddenFriend);
                }
                else {
                    studentName.setVisibility(View.VISIBLE);
                    friendName.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    mWeekView.setVisibility(View.VISIBLE);
                    findViewById(R.id.versus).setVisibility(View.VISIBLE);

                    ArrayList<HashMap<String, String>> databaseClasses = null;
                    databaseClasses = (ArrayList<HashMap<String, String>>) val.get("Schedule");

                    if (databaseClasses != null) {
                        for (int i = 0; i < databaseClasses.size(); i++) {
                            if (databaseClasses.get(i) != null) {
                                final HashMap<String, String> currentDBClass = databaseClasses.get(i);
                                String major = currentDBClass.get("Major");
                                String course = currentDBClass.get("Course");
                                String section = currentDBClass.get("Section");

                                //System.out.println(major + " " + course + " " + section);
                                Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Classes/" + major + "/" +
                                        course + "/" + section);
                                //System.out.println(ref.getRoot());
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        //System.out.println(snapshot);
                                        Classes currentClass = new Classes();
                                        HashMap<String, String> val = (HashMap) snapshot.getValue();
                                        currentClass.setCourseNum(val.get("courseNum"));
                                        currentClass.setCredits(val.get("credits"));
                                        currentClass.setCRN(val.get("crn"));
                                        currentClass.setDays(val.get("days"));
                                        currentClass.setEndDate(val.get("endDate"));
                                        currentClass.setEndTime(val.get("endTime"));
                                        currentClass.setInstructor(val.get("instructor"));
                                        currentClass.setInstructorEmail(val.get("instructorEmail"));
                                        currentClass.setLocation(val.get("location"));
                                        currentClass.setMajor(val.get("major"));
                                        currentClass.setSectionNum(val.get("sectionNum"));
                                        currentClass.setStartDate(val.get("startDate"));
                                        currentClass.setStartTime(val.get("startTime"));
                                        currentClass.setTitle(val.get("title"));
                                        currentClass.setType(val.get("type"));
                                        currentClass.setLatitude(val.get("latitude"));
                                        currentClass.setLongitude(val.get("longitude"));
                                        friendsClasses.add(currentClass);
                                        setMinTime((String) spinner.getSelectedItem());
                                        mWeekView.notifyDatasetChanged();
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }

                        }
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (int i = 0; i < currentStudentClasses.size(); i++) {
            if (!currentStudentClasses.get(i).getStartTime().equals("TBA")) {
                HashMap<String, Integer> interpretedStartTime = interpretTime(currentStudentClasses.get(i).getStartTime());
                HashMap<String, Integer> interpretedEndTime = interpretTime(currentStudentClasses.get(i).getEndTime());
                ArrayList<Integer> classDays = getDay(currentStudentClasses.get(i).getDays());
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
                    WeekViewEvent event = new WeekViewEvent(i, currentStudentClasses.get(i).getMajor()
                            + " " + currentStudentClasses.get(i).getCourseNum() + "\n" +
                            currentStudentClasses.get(i).getStartTime() + " - " +
                            currentStudentClasses.get(i).getEndTime(), startTime, endTime);
                    event.setColor(getResources().getColor(R.color.caldroid_black));
                    events.add(event);
                }
            }
        }

        for (int i = 0; i < friendsClasses.size(); i++) {
            if (!friendsClasses.get(i).getStartTime().equals("TBA")) {
                HashMap<String, Integer> interpretedStartTime = interpretTime(friendsClasses.get(i).getStartTime());
                HashMap<String, Integer> interpretedEndTime = interpretTime(friendsClasses.get(i).getEndTime());
                ArrayList<Integer> classDays = getDay(friendsClasses.get(i).getDays());
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
                    WeekViewEvent event = new WeekViewEvent(i, friendsClasses.get(i).getMajor()
                            + " " + friendsClasses.get(i).getCourseNum() + "\n" +
                            friendsClasses.get(i).getStartTime() + " - " +
                            friendsClasses.get(i).getEndTime(), startTime, endTime);
                    event.setColor(getResources().getColor(R.color.ScheduleColor));
                    events.add(event);
                }
            }
        }



        return events;
    }

    public HashMap<String, Integer> interpretTime(String time)
    {
        //System.out.println("Time: " + time);
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

    public void setMinTime(String daySelected)
    {
        if (daySelected.equals("Sunday")) {
            int minTime = 100;
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                if (currentStudentClasses.get(i).getDays().contains("U")) {
                    if (!currentStudentClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(currentStudentClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            for (int i = 0; i < friendsClasses.size(); i++) {
                if (friendsClasses.get(i).getDays().contains("U")) {
                    if (!friendsClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(friendsClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            mWeekView.goToHour(minTime);
        }
        else if (daySelected.equals("Monday")) {
            Calendar day = Calendar.getInstance();
            day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            mWeekView.goToDate(day);

            int minTime = 100;
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                if (currentStudentClasses.get(i).getDays().contains("M")) {
                    if (!currentStudentClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(currentStudentClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            for (int i = 0; i < friendsClasses.size(); i++) {
                if (friendsClasses.get(i).getDays().contains("M")) {
                    if (!friendsClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(friendsClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            mWeekView.goToHour(minTime);
            System.out.println("MinTime: " + minTime);
        }
        else if (daySelected.equals("Tuesday")) {
            Calendar day = Calendar.getInstance();
            day.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            mWeekView.goToDate(day);

            int minTime = 100;
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                if (currentStudentClasses.get(i).getDays().contains("T")) {
                    if (!currentStudentClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(currentStudentClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            for (int i = 0; i < friendsClasses.size(); i++) {
                if (friendsClasses.get(i).getDays().contains("T")) {
                    if (!friendsClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(friendsClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            mWeekView.goToHour(minTime);
        }
        else if (daySelected.equals("Wednesday")) {
            Calendar day = Calendar.getInstance();
            day.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            mWeekView.goToDate(day);

            int minTime = 100;
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                if (currentStudentClasses.get(i).getDays().contains("W")) {
                    if (!currentStudentClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(currentStudentClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            for (int i = 0; i < friendsClasses.size(); i++) {
                if (friendsClasses.get(i).getDays().contains("W")) {
                    if (!friendsClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(friendsClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            mWeekView.goToHour(minTime);
        }
        else if (daySelected.equals("Thursday")) {
            Calendar day = Calendar.getInstance();
            day.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            mWeekView.goToDate(day);

            int minTime = 100;
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                if (currentStudentClasses.get(i).getDays().contains("R")) {
                    if (!currentStudentClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(currentStudentClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            for (int i = 0; i < friendsClasses.size(); i++) {
                if (friendsClasses.get(i).getDays().contains("R")) {
                    if (!friendsClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(friendsClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            mWeekView.goToHour(minTime);
        }
        else if (daySelected.equals("Friday")) {
            Calendar day = Calendar.getInstance();
            day.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            mWeekView.goToDate(day);

            int minTime = 100;
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                if (currentStudentClasses.get(i).getDays().contains("F")) {
                    if (!currentStudentClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(currentStudentClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            for (int i = 0; i < friendsClasses.size(); i++) {
                if (friendsClasses.get(i).getDays().contains("F")) {
                    if (!friendsClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(friendsClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            mWeekView.goToHour(minTime);
        }
        else if (daySelected.equals("Saturday")) {
            Calendar day = Calendar.getInstance();
            day.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            mWeekView.goToDate(day);

            int minTime = 100;
            for (int i = 0; i < currentStudentClasses.size(); i++) {
                if (currentStudentClasses.get(i).getDays().contains("S")) {
                    if (!currentStudentClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(currentStudentClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            for (int i = 0; i < friendsClasses.size(); i++) {
                if (friendsClasses.get(i).getDays().contains("S")) {
                    if (!friendsClasses.get(i).getStartTime().equals("TBA")) {
                        HashMap<String, Integer> times = interpretTime(friendsClasses.get(i).getStartTime());
                        if (minTime > times.get("Hour")) {
                            minTime = times.get("Hour");
                        }
                    }
                }
            }
            mWeekView.goToHour(minTime);
            System.out.println("MinTime: " + minTime);
        }
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

}