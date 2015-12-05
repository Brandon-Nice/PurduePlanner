package com.purdueplanner.purdueplanner;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

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


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressInput.setText("");
                zipInput.setText("");
                countryInput.setText("");
                startActivity(new Intent(HomeLocationActivity.this, StartActivity.class));
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Inside onClick");
                if (addressInput.toString().matches("") || zipInput.toString().matches("") || countryInput.toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter all information", Toast.LENGTH_LONG).show();
                    System.out.println("Inside if empty");
                }
                else {
                    System.out.println("Inside else");
                    //TODO: Fix this
                    Geocoder coder = new Geocoder(getApplicationContext());
                    try {
                        ArrayList<Address> addresses = (ArrayList<Address>) coder.getFromLocationName(addressInput.toString()
                                + " " + zipInput.toString() + " " + countryInput.toString(), 1);
                        for (Address add : addresses) {
                           if (addresses == null) {
                                Toast.makeText(getApplicationContext(), "Location Not Found \n Please Try Again", Toast.LENGTH_LONG).show();
                            } else {
                                double lat = add.getLatitude();
                                double lon = add.getLongitude();
                                Toast.makeText(getApplicationContext(), "Lat" + lat + "\n" + "Long" + lon, Toast.LENGTH_LONG).show();
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
