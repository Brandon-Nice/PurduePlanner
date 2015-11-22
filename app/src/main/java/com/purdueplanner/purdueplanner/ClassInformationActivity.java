package com.purdueplanner.purdueplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

public class ClassInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // It sets the content to activity_add_class because I want to reuse and its easier than making its own layout
        // when its just going to be the same
        setContentView(R.layout.activity_class_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Class Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        /*//Add buttons and spinners
        Button okbutton = (Button) findViewById(R.id.okbutton);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Submit schedule to the database
            }
        });

        Button cancelbutton = (Button) findViewById(R.id.cancelbutton);
        cancelbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddClassActivity.this, StartActivity.class));
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        else if (id == android.R.id.home)
        {
            System.out.println("Back Pressed");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
