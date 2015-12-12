package com.purdueplanner.purdueplanner;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Kenny on 12/11/2015.
 */
public class DatabaseBuilder extends AppCompatActivity {
/*
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Firebase.setAndroidContext(this);
            //setContentView(R.layout.activity_main);
        }

        @Override
        protected void onStart() {

            super.onStart();
            //Firebase nuke = new Firebase("https://purduescheduler.firebaseio.com/Classes");
            //nuke.removeValue();
            //System.out.println("Post nuke");

            try {
                AssetManager am = getAssets();
                InputStream is = am.open("classesForAllOutput.txt");

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                int i = 0;
                int majorNumber = 100;
                Classes c;
                HashMap<String, String> fireMap = new HashMap<>(19);
                //String courseName = "";
                String courseNumber = "";
                String sectionNumber = "";
                String major = "";
                while((line = reader.readLine()) != null) {
                    if(i == 17) {
                        Firebase myFirebaseRef = new Firebase("https://purduescheduler.firebaseio.com/");

                        ClassesPrime fullClass = new ClassesPrime(fireMap.get("Start Time"), fireMap.get("End Time "), fireMap.get("Start Date"), fireMap.get("End Date"),
                                fireMap.get("CRN"), fireMap.get("Major"), fireMap.get("Course Number"), fireMap.get("Section Number"),
                                fireMap.get("Credits"), fireMap.get("Title"), fireMap.get("Days"), fireMap.get("Primary instructor"),
                                fireMap.get("Primary instructor email"), fireMap.get("Location"), fireMap.get("Type"), fireMap.get("Latitude"),
                                fireMap.get("Longitude"));
                        Firebase finalRef = myFirebaseRef.child("Classes/" + major + "/" + courseNumber + "/" + sectionNumber);
                        finalRef.setValue(fullClass);
                        fireMap.clear();
                        i = 0;
                    }
                    if(line.isEmpty()) {
                        continue;
                    }
                    else {
                        fireMap.put(line.substring(0, line.indexOf(':')), line.substring(line.indexOf(':') + 2));
                    /*if(i == 9) {
                       // System.out.println("TITLE FOR ADDRESS: " + fireMap.get(line.substring(0, line.indexOf(':'))));
                        courseName = fireMap.get(line.substring(0, line.indexOf(':')));
                    }*/
                    /*
                        if(i == 7) {
                            // System.out.println("SECTION FOR ADDRESS: " + fireMap.get(line.substring(0, line.indexOf(':'))));
                            sectionNumber = fireMap.get(line.substring(0, line.indexOf(':')));
                        }
                        else if(i == 6) {
                            //System.out.println("SECTION FOR ADDRESS: " + fireMap.get(line.substring(0, line.indexOf(':'))));
                            courseNumber = fireMap.get(line.substring(0, line.indexOf(':')));
                        }
                        else if (i == 5) {
                            major = fireMap.get(line.substring(0, line.indexOf(':')));
                        }
                        i++;
                    }

                    System.out.println(line);

                }
                is.close();
            } catch(IOException ioe){
                ioe.printStackTrace();
                return;
            }
        }*/
}

