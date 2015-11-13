package com.purdueplanner.purdueplanner;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class ClassInformationFragment extends Fragment {

    // The view that the fragment will show
    View v;
    // The major name of the current class being added to a schedule
    String majorName;
    // The course number of the current class being added to a schedule
    String courseNum;
    // The section name of the current class being added to a schedule
    String sectionName;
    // The current class being added to the schedule
    Classes currentClass;
    // The interface for add the class will be placed here
    LinearLayout rl;

    public ClassInformationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("in Fragment");
        v = inflater.inflate(R.layout.fragment_class_information, container, false);

        rl = (LinearLayout) v.findViewById(R.id.linear);

        Intent intent = getActivity().getIntent();
        HashMap<String, String> databaseClass = null;
        if (intent != null) {
            Bundle val = intent.getExtras();
            databaseClass = (HashMap<String, String>) val.get("Database Info");
        }
        final HashMap<String, String> databaseClassForFirebase = databaseClass;
        String dir = databaseClass.get("Major") + "/" + databaseClass.get("Course") + "/" + databaseClass.get("Section");
        // Required to connect to firebase database
        Firebase.setAndroidContext(getActivity());
        // This should direct to the class the user selected
        Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Classes/" + dir);
        // Get the class information for the firebase database that is being accessed
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Where all the class information from the database will be stored
                currentClass = new Classes();
                // Get all the class information in a hashmap
                HashMap<String, String> val = (HashMap) snapshot.getValue();
                // Get the course number
                currentClass.setCourseNum(val.get("courseNum"));
                // Get the credits
                currentClass.setCredits(val.get("credits"));
                // Get the crn
                currentClass.setCRN(val.get("crn"));
                // Get the days
                currentClass.setDays(val.get("days"));
                // Get the end date
                currentClass.setEndDate(val.get("endDate"));
                // Get the end time
                currentClass.setEndTime(val.get("endTime"));
                // Get the instructor
                currentClass.setInstructor(val.get("instructor"));
                // Get the instructor's email
                currentClass.setInstructorEmail(val.get("instructorEmail"));
                // Get the location
                currentClass.setLocation(val.get("location"));
                // Get the major
                currentClass.setMajor(val.get("major"));
                // Get the section number
                currentClass.setSectionNum(val.get("sectionNum"));
                // Get the start date
                currentClass.setStartDate(val.get("startDate"));
                // Get the start time
                currentClass.setStartTime(val.get("startTime"));
                // Get the title
                currentClass.setTitle(val.get("title"));
                // Get the type
                currentClass.setType(val.get("type"));
                // Create a new text view that will store the class information
                TextView textView = new TextView(getActivity());
                // Put the class information in the text view
                textView.setText(currentClass.toString());
                // Display the class information on the interface
                rl.addView(textView);
                // Create a button so the user can remove the class
                Button removeClassButton = new Button(getActivity());
                // Set the text of the button
                removeClassButton.setText("Remove Class");
                // Set the on click listener
                removeClassButton.setOnClickListener(removeClass);
                // Set the style
                removeClassButton.setBackgroundResource(R.drawable.buttonstyle);
                // Set the button's on click listener
                removeClassButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Student currentStudent = ((MyApplication) getActivity().getApplication()).getStudent();
                        String dir = currentStudent.getId() + "/Schedule/";
                        final Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + dir);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                System.out.println(snapshot);
                                ArrayList<HashMap<String, String>> val = (ArrayList) snapshot.getValue();
                                for (int i = 0; i < val.size(); i++) {
                                    if (val.get(i).get("Major").equals(databaseClassForFirebase.get("Major")) &&
                                            val.get(i).get("Course").equals(databaseClassForFirebase.get("Course")) &&
                                            val.get(i).get("Section").equals(databaseClassForFirebase.get("Section"))) {
                                        ArrayList<Classes> currentSchedule = ((MyApplication) getActivity().getApplication()).getStudent().getSchedule();
                                        ArrayList<HashMap<String, String>> databaseClasses = new ArrayList<HashMap<String, String>>();
                                        for (int j = 0; j < currentSchedule.size(); j++) {
                                            if (val.get(i).get("Major").equals(currentSchedule.get(j).getMajor()) &&
                                                    val.get(i).get("Course").equals(currentSchedule.get(j).getCourseNum()) &&
                                                    val.get(i).get("Section").equals(currentSchedule.get(j).getSectionNum()))
                                            {
                                                currentSchedule.remove(j);
                                                j--;
                                            }
                                            else
                                            {
                                                HashMap<String, String> currentDatabaseClass = new HashMap<String, String>();
                                                currentDatabaseClass.put("Major", currentSchedule.get(j).getMajor());
                                                currentDatabaseClass.put("Course", currentSchedule.get(j).getCourseNum());
                                                currentDatabaseClass.put("Section", currentSchedule.get(j).getSectionNum());
                                                databaseClasses.add(currentDatabaseClass);
                                            }
                                        }
                                        ref.setValue(databaseClasses);

                                        break;
                                    }
                                }
                                getActivity().onBackPressed();


                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });

                    }
                });
                // Add the button to the interface
                rl.addView(removeClassButton);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return v;
    }

    View.OnClickListener removeClass = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
