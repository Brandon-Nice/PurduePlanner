package com.purdueplanner.purdueplanner;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

public class StartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String[] testArray = {"CS 354", "CS 252", "CS 348", "CS 391"};
    private ListView dayListView;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Purdue Planner");
        //Firebase library initilization
        Firebase.setAndroidContext(this);


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


        //Sets the list view for the day
        dayListView = (ListView) findViewById(R.id.dayList);
        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, testArray);
        //arrayAdapter.set
        ArrayList<String> classList = new ArrayList<>();
        classList.add("CS 354");
        classList.add("CS 307");
        classList.add("CS 391");
        classList.add("MA 265");

        customAdapter arrayAdapter = new customAdapter(classList, this);
        dayListView.setAdapter(arrayAdapter);

        //Gets the current day
        Date date = new Date();

        CharSequence currDay = android.text.format.DateFormat.format("EEEE", date);
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
            startActivity(new Intent(StartActivity.this, FacebookFragment.class));
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
}
