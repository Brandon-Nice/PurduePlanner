package com.purdueplanner.purdueplanner;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_location);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home Location");

        final Button setButton = (Button) findViewById(R.id.setButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        setButton.setBackgroundResource(android.R.drawable.btn_default);
        cancelButton.setBackgroundResource(android.R.drawable.btn_default);
        final EditText addressInput = (EditText) findViewById(R.id.address);
        final EditText zipInput = (EditText) findViewById(R.id.zip);
        final EditText countryInput = (EditText) findViewById(R.id.country);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        final View focusTheif = findViewById(R.id.focus_thief);

        addressInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    addressInput.clearFocus();
                    zipInput.requestFocus(zipInput.FOCUS_DOWN);
                    return true;
                }
                return false;
            }
        });

        addressInput.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() != null)
                {
                    getCurrentFocus().clearFocus();
                }
                addressInput.requestFocus(addressInput.FOCUS_DOWN);
                return false;
            }
        });

        zipInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    zipInput.clearFocus();
                    countryInput.requestFocus(addressInput.FOCUS_DOWN);
                    return true;
                }
                return false;
            }
        });

        zipInput.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() != null)
                {
                    getCurrentFocus().clearFocus();
                }
                zipInput.requestFocus(zipInput.FOCUS_DOWN);
                return false;
            }
        });

        countryInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(v);
                    countryInput.clearFocus();
                    focusTheif.requestFocus(focusTheif.FOCUS_DOWN);
                    return true;
                }
                return false;
            }
        });

        countryInput.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                countryInput.clearFocus();
                focusTheif.requestFocus(focusTheif.FOCUS_DOWN);
                return false;
            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressInput.setText("");
                zipInput.setText("");
                countryInput.setText("");
                //Finish activity
                finish();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressInput.toString().matches("") || zipInput.toString().matches("") || countryInput.toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all information", Toast.LENGTH_LONG).show();
                }
                else {
                    String fullAddress = addressInput.getText().toString()
                            + " " + zipInput.getText().toString() + " " + countryInput.getText().toString();
                    Geocoder coder = new Geocoder(getApplicationContext());
                    try {
                        ArrayList<Address> addresses = (ArrayList<Address>) coder.getFromLocationName(fullAddress, 1);
                        for (Address add : addresses) {
                           if (addresses == null) {
                                Toast.makeText(getApplicationContext(), "Location Not Found \n Please Try Again", Toast.LENGTH_LONG).show();
                            } else {
                                double lat = add.getLatitude();
                                double lon = add.getLongitude();
                                Toast.makeText(getApplicationContext(), "Lat " + lat + "\n" + "Long " + lon, Toast.LENGTH_LONG).show();

                               //Add the lat and long to the student in database
                               Firebase.setAndroidContext(getApplicationContext());
                               Student currentStudent = null;
                               if (getApplication() != null) {
                                   currentStudent = ((MyApplication) getApplication()).getStudent();
                               }
                               // Go to the current student in the firebase databse
                               Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + currentStudent.getId());
                               // Go to the current student's home location in the firebase data
                               Firebase scheduleRef = ref.child("HomeLocation");
                               HashMap<String, Double> homelocation = new HashMap<String, Double>();
                               homelocation.put("Latitude", lat);
                               homelocation.put("Longitude", lon);
                               // Put all of the classes in the database for the students
                               scheduleRef.setValue(homelocation);

                               currentStudent.setLatitude(lat);
                               currentStudent.setLongitude(lon);
                               //Finish activity
                               finish();
                           }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
