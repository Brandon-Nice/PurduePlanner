package com.purdueplanner.purdueplanner;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home Location");

        Button setButton = (Button) findViewById(R.id.setButton);
        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        final EditText addressInput = (EditText) findViewById(R.id.address);
        final EditText zipInput = (EditText) findViewById(R.id.zip);
        final EditText countryInput = (EditText) findViewById(R.id.country);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
                    System.out.println(fullAddress);
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
                                System.out.println("done");
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

}
