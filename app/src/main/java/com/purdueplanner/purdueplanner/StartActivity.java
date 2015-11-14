package com.purdueplanner.purdueplanner;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.FacebookCallback;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import com.facebook.FacebookSdk;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String[] testArray = {"CS 354", "CS 252", "CS 348", "CS 391"};
    private ListView dayListView;
    private customAdapter arrayAdapter;
    private ArrayList<Classes> studentsClasses;
    private String currDay;
    static LoginButton loginButton;
    static CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Purdue Planner");

        //sets the name in the nav_header
        if(isLoggedIn()) {
            final TextView user_name = (TextView) findViewById(R.id.usertextView);
            final TextView user_email = (TextView) findViewById(R.id.useremailtextView);
            final ImageView user_pic = (ImageView) findViewById(R.id.userimageView);
            //TODO: get the user name, email, and picture of person logged in
            loginButton = LoginActivity.getLoginButton();
            callbackManager = LoginActivity.getCallbackManager();
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            try {
                                                object = response.getJSONObject();
                                                Log.d("LoginActivity", response.toString());
                                                String id = object.getString("id");
                                                String name = object.getString("name");
                                                String email = object.getString("email");
                                                //TODO: get picture

                                                user_name.setText(name);
                                                user_email.setText(email);
                                            }
                                            catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,picture");
                            request.setParameters(parameters);
                            request.executeAsync();
                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onError(FacebookException e) {
                                e.printStackTrace();
                            }
                        });


        }
        // Set the student's schedule up to be displayed
        setStudent();
        //code that implements  the map button
        ImageButton mapButton = (ImageButton) findViewById(R.id.mapsButton);

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MapsActivity.class));
            }
        });

        //code that implements the friends button
        ImageButton friendButton = (ImageButton) findViewById(R.id.friendButton);

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, FriendsActivity.class));
            }
        });

        //code that implements the schedule button
        ImageButton scheduleButton = (ImageButton) findViewById(R.id.scheduleButton);

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, ScheduleActivity.class));
            }
        });

        //Gets the current day
        Date date = new Date();

        currDay = (String) android.text.format.DateFormat.format("EEEE", date);
        System.out.println(currDay);
        TextView myTextView = (TextView) findViewById(R.id.textView);
        myTextView.setText(currDay);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void setStudent() {
        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + Integer.toString(-1));
        final Student currentStudent = new Student();
        final ArrayList<Classes> currentStudentClasses = new ArrayList<Classes>();
        final ArrayList<String> classList = new ArrayList<String>();
        final ArrayList<HashMap<String, String>> databaseAdapterList = new ArrayList<HashMap<String, String>>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot);
                HashMap<String, Object> val = (HashMap) snapshot.getValue();
                ArrayList<HashMap<String, String>> databaseClasses = null;
                for (HashMap.Entry<String, Object> entry : val.entrySet()) {
                    if (entry.getKey().equals("lastName")) {
                        currentStudent.setLastName((String) entry.getValue());
                    } else if (entry.getKey().equals("firstName")) {
                        currentStudent.setFirstName((String) entry.getValue());
                    } else if (entry.getKey().equals("Schedule")) {
                        databaseClasses = (ArrayList<HashMap<String, String>>) entry.getValue();
                    } else if (entry.getKey().equals("id")) {
                        currentStudent.setId((Long) entry.getValue());
                    }
                }

                if (databaseClasses != null) {
                    for (int i = 0; i < databaseClasses.size(); i++) {
                        if ( databaseClasses.get(i) != null) {
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
                                    currentStudentClasses.add(currentClass);
                                    boolean addClassForDay = false;
                                    if (currentClass.getDays().contains("M") && currDay.equals("Monday")) {
                                        addClassForDay = true;
                                    }
                                    if (currentClass.getDays().contains("T") && currDay.equals("Tuesday")) {
                                        addClassForDay = true;
                                    }
                                    if (currentClass.getDays().contains("W") && currDay.equals("Wednesday")) {
                                        addClassForDay = true;
                                    }
                                    if (currentClass.getDays().contains("R") && currDay.equals("Thursday")) {
                                        addClassForDay = true;
                                    }
                                    if (currentClass.getDays().contains("F") && currDay.equals("Friday")) {
                                        addClassForDay = true;
                                    }

                                    if (addClassForDay) {
                                        classList.add(currentClass.getMajor() + " " + currentClass.getCourseNum());
                                        Comparator<String> comparator = new Comparator<String>() {
                                            public int compare(String s1, String s2) {
                                                return s1.compareTo(s2);
                                            }

                                        };
                                        Collections.sort(classList, comparator);
                                        databaseAdapterList.add(currentDBClass);
                                        Comparator<HashMap<String, String>> hashMapComparator = new Comparator<HashMap<String, String>>() {
                                            public int compare(HashMap<String,String> h1, HashMap<String, String> h2) {
                                                return (h1.get("Major") + h1.get("Course")).compareTo(h2.get("Major") + h2.get("Course"));
                                            }
                                        };
                                        Collections.sort(databaseAdapterList, hashMapComparator);
                                        ((customAdapter) dayListView.getAdapter()).notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }
                    }
                }
                System.out.println(currentStudentClasses);
                currentStudent.setSchedule(currentStudentClasses);
                ((MyApplication) getApplication()).setStudent(currentStudent);
                studentsClasses = ((MyApplication) getApplication()).getStudent().getSchedule();

                //Sets the list view for the day
                dayListView = (ListView) findViewById(R.id.dayList);
                //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, testArray);
                //arrayAdapter.set

                customAdapter arrayAdapter = new customAdapter(classList, databaseAdapterList, getApplicationContext());
                dayListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(StartActivity.this, SettingsActivity.class));
        }
        //code that implements the add class button
        else if (id == R.id.add_class) {
            startActivity(new Intent(StartActivity.this, AddClassActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            // Handle the login action
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(StartActivity.this, ContactActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(StartActivity.this, AboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRestart() {
        ArrayList<String> classList = new ArrayList<>();
        ArrayList<HashMap<String, String>> databaseAdapterList = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < studentsClasses.size(); i++) {
            Classes currentClass = studentsClasses.get(i);
            boolean addClassForDay = false;
            if (currentClass.getDays().contains("M") && currDay.equals("Monday"))
            {
                addClassForDay = true;
            }
            if (currentClass.getDays().contains("T") && currDay.equals("Tuesday"))
            {
                addClassForDay = true;
            }
            if (currentClass.getDays().contains("W") && currDay.equals("Wednesday"))
            {
                addClassForDay = true;
            }
            if (currentClass.getDays().contains("R") && currDay.equals("Thursday"))
            {
                addClassForDay = true;
            }
            if (currentClass.getDays().contains("F") && currDay.equals("Friday"))
            {
                addClassForDay = true;
            }
            if (addClassForDay) {
                classList.add(currentClass.getMajor() + " " + currentClass.getCourseNum());
                HashMap<String, String> currentDatabaseClass = new HashMap<String, String>();
                currentDatabaseClass.put("Major", currentClass.getMajor());
                currentDatabaseClass.put("Course", currentClass.getCourseNum());
                currentDatabaseClass.put("Section", currentClass.getSectionNum());
                databaseAdapterList.add(currentDatabaseClass);
            }
        }
        Comparator<String> comparator = new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }

        };
        Collections.sort(classList, comparator);
        Comparator<HashMap<String, String>> hashMapComparator = new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String,String> h1, HashMap<String, String> h2) {
                return (h1.get("Major") + h1.get("Course")).compareTo(h2.get("Major") + h2.get("Course"));
            }
        };
        Collections.sort(databaseAdapterList, hashMapComparator);
        ((customAdapter) dayListView.getAdapter()).notifyDataSetChanged();
        customAdapter arrayAdapter = new customAdapter(classList, databaseAdapterList, this);
        dayListView.setAdapter(arrayAdapter);
        super.onRestart();
    }

}
