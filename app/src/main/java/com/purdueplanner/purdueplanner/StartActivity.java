package com.purdueplanner.purdueplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
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
        implements NavigationView.OnNavigationItemSelectedListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private ListView dayListView;
    private ArrayList<Classes> studentsClasses;
    private String currDay;
    static LoginButton loginButton;
    static CallbackManager callbackManager;
    private NetworkStateReceiver networkStateReceiver;
    public static Boolean isConnected; //boolean that can be accessed via any class to see if connection is lost or not

    @Override
    public void networkAvailable() { //Function that is called when the network is available == aka the onCreate stuff
        isConnected = true;

        Toast.makeText(getApplicationContext(), "Connected.", Toast.LENGTH_LONG).show();
        //sets the name in the nav_header
        if (isLoggedIn()) {
            final TextView user_name = (TextView) findViewById(R.id.usertextView);
            loginButton = LoginActivity.getLoginButton();
            callbackManager = LoginActivity.getCallbackManager();

            //Gets the name of the user
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null,
                    HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    //handle the response
                    final JSONObject jsonObject = response.getJSONObject();
                    String name = "";
                    try {
                        name = jsonObject.getString("name");
                        String firstName = name.substring(0, name.indexOf(" "));
                        String lastName = name.substring(name.indexOf(" ") + 1);
                        ((MyApplication) getApplication()).getStudent().setFirstName(firstName);
                        ((MyApplication) getApplication()).getStudent().setLastName(lastName);
                        user_name.setText(name);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();

            //Gets the picture of the user
            final ProfilePictureView profilePictureView;
            profilePictureView = (ProfilePictureView) findViewById(R.id.userimageView);
            profilePictureView.setCropped(true);
            profilePictureView.setProfileId(AccessToken.getCurrentAccessToken().getUserId());
        }
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
        TextView myTextView = (TextView) findViewById(R.id.textView);
        myTextView.setText(currDay);

        // Set the student's schedule up to be displayed
        setStudent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //added from oncreate
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);




        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void networkUnavailable() { //Function that is called when user loses connection. TODO: Implement more functionality maybe?
        isConnected = false;
        //TODO: Implement actions for button
        Toast.makeText(getApplicationContext(), "Lost connection.", Toast.LENGTH_LONG).show();
        AlertDialog alertDialog = new AlertDialog.Builder(StartActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Oops! Looks like you aren't connected to Wifi or a mobile network at the moment. Would you like to connect or exit?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        //TODO: Which buttons should still be accessible to users when they're immediately offline (ie opening the app first and not having a connection)?
        // Copy & paste them below..
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Purdue Planner");

        //Registers our Connectivity receiver and adds a listener to it
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void setStudent() {
        Firebase.setAndroidContext(this);
        final Student currentStudent = ((MyApplication) getApplication()).getStudent();
        final Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + currentStudent.getId());
        final ArrayList<Classes> currentStudentClasses = new ArrayList<Classes>();
        final ArrayList<HashMap<String, String>> databaseAdapterList = new ArrayList();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot);
                HashMap<String, Object> val = (HashMap) snapshot.getValue();
                if (val == null) {
                    HashMap<String, String> dataBaseStudent = new HashMap();
                    dataBaseStudent.put("id", currentStudent.getId());
                    ref.setValue(dataBaseStudent);
                }
                else {

                    ArrayList<HashMap<String, String>> databaseClasses = null;
                    HashMap<String, Double> homeLocation = null;
                    currentStudent.setId((String) val.get("id"));
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    MenuItem hide = navigationView.getMenu().getItem(2);
                    if (val.get("isScheduleHidden") != null) {
                        currentStudent.setScheduleHidden((boolean) val.get("isScheduleHidden"));
                        if (currentStudent.getScheduleHidden()) {
                            hide.setIcon(getResources().getDrawable(R.drawable.check_mark));
                        } else {
                            hide.setIcon(getResources().getDrawable(R.drawable.x_icon));
                        }
                    }
                    else
                    {
                        hide.setIcon(getResources().getDrawable(R.drawable.x_icon));
                    }
                    databaseClasses = (ArrayList<HashMap<String, String>>) val.get("Schedule");
                    homeLocation = (HashMap<String, Double>) val.get("HomeLocation");
                    if(homeLocation != null) {
                        currentStudent.setLongitude(homeLocation.get("Longitude"));
                        currentStudent.setLatitude(homeLocation.get("Latitude"));
                    }
                    if (databaseClasses != null) {
                        for (int i = 0; i < databaseClasses.size(); i++) {
                            if (databaseClasses.get(i) != null) {
                                final HashMap<String, String> currentDBClass = databaseClasses.get(i);
                                String major = currentDBClass.get("Major");
                                String course = currentDBClass.get("Course");
                                String section = currentDBClass.get("Section");

                                Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Classes/" + major + "/" +
                                        course + "/" + section);
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
                                            currentDBClass.put("StartTime", currentClass.getStartTime());
                                            databaseAdapterList.add(currentDBClass);
                                            sortClasses(databaseAdapterList);
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
                }
                currentStudent.setSchedule(currentStudentClasses);
                ((MyApplication) getApplication()).setStudent(currentStudent);
                studentsClasses = ((MyApplication) getApplication()).getStudent().getSchedule();

                //Sets the list view for the day
                dayListView = (ListView) findViewById(R.id.dayList);
                //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, testArray);
                //arrayAdapter.set

                customAdapter arrayAdapter = new customAdapter(databaseAdapterList, getApplicationContext());
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
        final Student currentStudent = ((MyApplication) getApplication()).getStudent();
        Firebase.setAndroidContext(this);

        if (id == R.id.nav_login) {
            // Handle the login action
            Intent init = new Intent(StartActivity.this, LoginActivity.class);
            init.putExtra("FromNavMenu", true);
            startActivity(init);
        } else if (id == R.id.nav_home) {
            // Handle the Home Location
            startActivity(new Intent(StartActivity.this, HomeLocationActivity.class));

        }
        else if(id == R.id.nav_del) {
            //Sets an alertdialog message to confirm delete
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(StartActivity.this);
            deleteDialog.setTitle("Confirm Delete");
            deleteDialog.setMessage("Are you sure you want to delete all classes?");
            deleteDialog.setIcon(R.drawable.delete_icon);
            deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ArrayList<Classes> emptyClasses = new ArrayList<>();
                    currentStudent.setSchedule(emptyClasses);
                    ((customAdapter) dayListView.getAdapter()).clearList();
                    ((customAdapter) dayListView.getAdapter()).notifyDataSetChanged();
                    Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + currentStudent.getId() + "/Schedule");
                    ref.removeValue();
                }
            });
            deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            deleteDialog.show();

        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(StartActivity.this, ContactActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(StartActivity.this, AboutActivity.class));
        } else if (id == R.id.nav_hide)
        {
            if (currentStudent.getScheduleHidden())
            {
                item.setIcon(getResources().getDrawable(R.drawable.x_icon));
                currentStudent.setScheduleHidden(false);
                Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + currentStudent.getId());
                // Go to the current student's schedule in the firebase data
                Firebase isScheduleHiddenRef = ref.child("isScheduleHidden");
                isScheduleHiddenRef.setValue(false);;



            }
            else
            {
                item.setIcon(getResources().getDrawable(R.drawable.check_mark));
                currentStudent.setScheduleHidden(true);
                Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + currentStudent.getId());
                // Go to the current student's schedule in the firebase data
                Firebase isScheduleHiddenRef = ref.child("isScheduleHidden");
                isScheduleHiddenRef.setValue(true);

            }
        }
        return true;
    }

    @Override
    public void onRestart() {
        try {
            ArrayList<HashMap<String, String>> databaseAdapterList = new ArrayList();
            for (int i = 0; i < studentsClasses.size(); i++) {
                Classes currentClass = studentsClasses.get(i);
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
                    HashMap<String, String> currentDBClass = new HashMap();
                    currentDBClass.put("Major", currentClass.getMajor());
                    currentDBClass.put("Course", currentClass.getCourseNum());
                    currentDBClass.put("Section", currentClass.getSectionNum());
                    currentDBClass.put("StartTime", currentClass.getStartTime());
                    databaseAdapterList.add(currentDBClass);
                }
            }
            sortClasses(databaseAdapterList);
            customAdapter arrayAdapter = new customAdapter(databaseAdapterList, this);
            dayListView.setAdapter(arrayAdapter);
            super.onRestart();

        } catch (NullPointerException n){ //means that the user is offline
            //TODO: Implement actions for button
            Toast.makeText(getApplicationContext(), "Lost connection.", Toast.LENGTH_LONG).show();
            AlertDialog alertDialog = new AlertDialog.Builder(StartActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Oops! Looks like you aren't connected to Wifi or a mobile network at the moment. Would you like to connect or exit?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            super.onRestart();
        }
    }

    private void sortClasses(ArrayList<HashMap<String, String>> classes)
    {
        Comparator<HashMap<String, String>> byTime = new Comparator<HashMap<String, String>>() {
            public int compare(HashMap<String, String> h1, HashMap<String, String> h2) {
                int firstColon = h1.get("StartTime").indexOf(":");
                int firstSpace = h1.get("StartTime").indexOf(" ");
                int firstStartTime = Integer.parseInt(h1.get("StartTime").substring(0, firstColon) +
                        h1.get("StartTime").substring(firstColon + 1, firstSpace));
                if (h1.get("StartTime").contains("pm") && firstStartTime < 1200)
                {
                    firstStartTime += 1200;
                }
                int secondColon = h2.get("StartTime").indexOf(":");
                int secondSpace = h2.get("StartTime").indexOf(" ");
                int secondStartTime = Integer.parseInt(h2.get("StartTime").substring(0, secondColon) +
                        h2.get("StartTime").substring(secondColon + 1, secondSpace));
                if (h2.get("StartTime").contains("pm") && secondStartTime < 1200)
                {
                    secondStartTime += 1200;
                }
                return (firstStartTime - secondStartTime);
            }
        };
        Collections.sort(classes, byTime);
    }

}
