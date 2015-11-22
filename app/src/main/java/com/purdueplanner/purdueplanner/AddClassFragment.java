package com.purdueplanner.purdueplanner;

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
public class AddClassFragment extends Fragment {

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

    public AddClassFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_class, container, false);
        // Required to connect to firebase database
        Firebase.setAndroidContext(getActivity());
        // Find the layout that the interface will be displayed in
        rl = (LinearLayout) v.findViewById(R.id.linear);
        // Load the buttons to select the course
        loadView("", btnClickedMajor);
        return v;
    }

    public void loadView(String dir, final View.OnClickListener clicked)
    {
        // Connect to the dir extension of the classes database
        Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Classes/" + dir);
        // This is where the text that go in the buttons will be stored
        final ArrayList<String> btnWord = new ArrayList<String>();
        // Get the information for the firebase database that is being accessed
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Get the values from the database, whether it be the majors, course numbers, or sections
                HashMap<String, HashMap> val = (HashMap) snapshot.getValue();
                // Add all of the names of the majors, course numbers, or sections to the buttons text
                for (HashMap.Entry<String, HashMap> entry : val.entrySet()) {
                    btnWord.add(entry.getKey());
                }

                // Sort comparator to sort the button texts
                Comparator<String> comparator = new Comparator<String>() {
                    public int compare(String s1, String s2) {
                        return s1.compareTo(s2);
                    }

                };
                // Sort the button texts in alphabetical order
                Collections.sort(btnWord);
                // Add all the buttons to the screen in this loop
                for (int i = 0; i < btnWord.size(); i++) {
                    // Create a button
                    Button currentButton = new Button(getActivity());
                    // Set the button's text
                    currentButton.setText(btnWord.get(i));
                    // Set it's onclickListener
                    currentButton.setOnClickListener(clicked);
                    // Set it's style
                    currentButton.setBackgroundResource(R.drawable.buttonstyle);
                    // Add it to the interface
                    rl.addView(currentButton);
                }

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



    View.OnClickListener btnClickedMajor = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get the button just clicked from view v
            Button b = (Button) v;
            // Set the major to the text of the button just clicked
            majorName = b.getText().toString();
            // Remove all previous buttons
            rl.removeAllViews();
            // Set the action bar to reflect what the user will be selecting next
            ((AddClassActivity) getActivity()).setActionBarTitle("Select Course");
            // Load the buttons to select the course
            loadView(majorName, btnClickedCourse);
        }
    };

    View.OnClickListener btnClickedCourse = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get the button just clicked from view v
            Button b = (Button) v;
            // Set the course number to the text of the button just clicked
            courseNum = b.getText().toString();
            // Remove all previous buttons
            rl.removeAllViews();
            // Set the action bar to reflect what the user will be selecting next
            ((AddClassActivity) getActivity()).setActionBarTitle("Select Section");
            // Load the buttons to select the section
            loadView(majorName + "/" + courseNum, btnClickedSection);

        }
    };

    View.OnClickListener btnClickedSection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get the button just clicked from view v
            Button b = (Button) v;
            // Set the section name to the text of the button just clicked
            sectionName = b.getText().toString();
            // Remove all previous buttons
            rl.removeAllViews();
            // Set the action bar to reflect what the user will see on the screen
            ((AddClassActivity) getActivity()).setActionBarTitle("Class Information");
            // Load the class information on the screen for confirmation to add class
            loadClass(majorName + "/" + courseNum + "/" + sectionName);
        }
    };

    public void loadClass(String dir)
    {
        // This should direct to the class the user selected in the previous methods
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
                // Create a button so the user can add the class
                Button addClassButton = new Button(getActivity());
                // Set the text of the button
                addClassButton.setText("Add Class");
                // Set the on click listener
                addClassButton.setOnClickListener(addClass);
                // Set the style
                addClassButton.setBackgroundResource(R.drawable.buttonstyle);
                // Add the button to the interface
                rl.addView(addClassButton);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    View.OnClickListener addClass = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Student currentStudent = ((MyApplication) getActivity().getApplication()).getStudent();
            // Get the current student's schedule
            ArrayList<Classes> studentClasses = currentStudent.getSchedule();
            // Track if the student already has the class they are trying to add in their schedule
            boolean classAlreadyAdded = false;
            // Loop to check if student already has the class they are trying to add in their schedule
            for (int i = 0; i < studentClasses.size(); i++)
            {
                // If the CRN's of the class they are trying to add is the same as one in their schedule
                if (currentClass.getCRN().equals(studentClasses.get(i).getCRN()))
                {
                    // Tell the user they already has that class
                    Toast toast = Toast.makeText(getActivity(), "Classes already exists in schedule.", Toast.LENGTH_LONG);
                    toast.show();
                    // Track that they already have that class in their schedule
                    classAlreadyAdded = true;
                    // Break out of the loop since we found they already have the class
                    break;
                }
            }
            // If the class already has not been added to the schedule
            if (classAlreadyAdded == false) {
                // Add the class to the student's schedule
                studentClasses.add(currentClass);
                ((MyApplication) getActivity().getApplication()).getStudent().setSchedule(studentClasses);
                // This is where the student's classes for the database will be stored
                ArrayList<HashMap<String, String>> databaseClasses = new ArrayList<HashMap<String, String>>();
                // Go through all the student's classes and add them to the above list in the from of hashmaps
                for (int i = 0; i < studentClasses.size(); i++)
                {
                    // This is where the current database class will be stored
                    HashMap<String, String> directory = new HashMap<String, String>();
                    // Get the major of the class
                    String major = studentClasses.get(i).getMajor();
                    // Get the course number of the class
                    String course = studentClasses.get(i).getCourseNum();
                    // Get the section of the class
                    String section = studentClasses.get(i).getSectionNum();
                    // Put the major in the hashmap
                    directory.put("Major", major);
                    // Put the course number in the hashmap
                    directory.put("Course", course);
                    // Put the section in the hashmap
                    directory.put("Section", section);
                    // Put the hashmap in the list of database classes
                    databaseClasses.add(directory);
                }
                // Go to the current student in the firebase databse
                Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Students/" + currentStudent.getId());
                // Go to the current student's schedule in the firebase data
                Firebase scheduleRef = ref.child("Schedule");
                // Put all of the classes in the database for the students
                scheduleRef.setValue(databaseClasses);
            }
            // Go back to screen that displays the schedule of classes for the student
            getActivity().onBackPressed();
        }
    };
}
