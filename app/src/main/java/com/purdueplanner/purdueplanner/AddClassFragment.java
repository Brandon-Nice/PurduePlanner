package com.purdueplanner.purdueplanner;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddClassFragment extends Fragment {

    // The view that the fragment will show
    View v;
    // The major of the current class being added to a schedule
    String majorName;
    // The course number of the current class being added to a schedule
    String courseNum;
    // The section name of the current class being added to a schedule
    String sectionName;
    // The current class being added to the schedule
    Classes currentClass;
    // The interface for add the class will be placed here
    LinearLayout rl;
    ArrayList<String> classArrayList;
    ArrayAdapter classAdapter;
    ListView classListView;
    EditText inputSearch;
    ArrayList<String> removedClasses;
    String previousText = "";

    public AddClassFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_class, container, false);
        // Required to connect to firebase database
        Firebase.setAndroidContext(getActivity());
        classArrayList = new ArrayList();
        removedClasses = new ArrayList();
        ArrayList<String> majors = new ArrayList(Arrays.asList("AAE", "AAS", "ABE", "AD", "AFT", "AGEC", "AGR", "AGRY", "AMST", "ANSC", "ANTH", "ARAB", "ASAM", "ASL", "ASM", "ASTR", "AT", "BAND", "BCHM", "BCM", "BIOL", "BME", "BMS", "BTNY", "CAND", "CE", "CEM", "CGT", "CHE", "CHM", "CHNS", "CLCS", "CLPH", "CMPL", "CNIT", "COM", "CPB", "CS", "CSR", "DANC", "EAPS", "ECE", "ECET", "ECON", "EDCI", "EDPS", "EDST", "EEE", "ENE", "ENGL", "ENGR", "ENTM", "ENTR", "EPCS", "EXPL", "FNR", "FR", "FS", "FVS", "GEP", "GER", "GRAD", "GREK", "GS", "HDFS", "HEBR", "HIST", "HK", "HONR", "HORT", "HSCI", "HTM", "IDE", "IDIS", "IE", "IET", "IPPH", "IT", "ITAL", "JPNS", "JWST", "LA", "LALS", "LATN", "LC", "LING", "MA", "MARS", "MCMP", "ME", "MET", "MFET", "MGMT", "MSE", "MSL", "MUS", "NRES", "NS", "NUCL", "NUPH", "NUR", "NUTR", "OBHR", "OLS", "PES", "PHAD", "PHIL", "PHPR", "PHRM", "PHYS", "POL", "PSY", "PTGS", "REL", "RUSS", "SA", "SCI", "SFS", "SLHS", "SOC", "SPAN", "STAT", "TECH", "THTR", "TLI", "VCS", "VM", "WGSS", "YDAE"));
        // Load the buttons to select the course
        for (int i = 0; i < majors.size(); i++) {
            classArrayList.add(majors.get(i));

        }
        if (getActivity() != null) {
            classAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, classArrayList);
        }
        classListView = (ListView) v.findViewById(R.id.classList);
        classListView.setAdapter(classAdapter);
        classListView.setOnItemClickListener(btnClickedMajor);

        inputSearch = (EditText) v.findViewById(R.id.inputSearch);


        classListView.setFocusable(false);
        classListView.setFocusableInTouchMode(false);
        inputSearch.setFocusable(true);
        inputSearch.setFocusableInTouchMode(true);

        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(v);
                    inputSearch.setFocusable(false);
                    inputSearch.setFocusableInTouchMode(false);
                    classListView.setFocusable(true);
                    classListView.setFocusableInTouchMode(true);
                    return true;
                }
                return false;
            }
        });

        inputSearch.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                classListView.setFocusable(false);
                classListView.setFocusableInTouchMode(false);
                inputSearch.setFocusable(true);
                inputSearch.setFocusableInTouchMode(true);
                return false;
            }
        });


        inputSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void onTextChanged (CharSequence cs,int arg1, int arg2, int arg3){
               // When user changed the Text
               String currentText = cs.toString();
               if (currentText.length() > previousText.length()) {
                   for (int i = 0; i < classArrayList.size(); i++) {
                       if (!classArrayList.get(i).toLowerCase().startsWith(currentText.toLowerCase())) {
                           removedClasses.add(classArrayList.get(i));
                           classArrayList.remove(i);
                           i--;
                       }
                   }
               } else {
                   for (int i = 0; i < removedClasses.size(); i++) {
                       if (removedClasses.get(i).toLowerCase().startsWith(currentText.toLowerCase())) {
                           classArrayList.add(removedClasses.get(i));
                           removedClasses.remove(i);
                           i--;
                       }
                   }
               }
               Collections.sort(classArrayList);
               classAdapter.notifyDataSetChanged();
               previousText = currentText;

           }

           @Override
           public void beforeTextChanged (CharSequence arg0,int arg1, int arg2,
                                          int arg3){

           }

           @Override
           public void afterTextChanged (Editable arg0){
           }
        }

        );
        return v;
    }

    public void loadView(String dir, final AdapterView.OnItemClickListener clicked) {
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
                    classArrayList.add(entry.getKey());
                }
                // Sort the button texts in alphabetical order
                Collections.sort(classArrayList);
                classAdapter.notifyDataSetChanged();
                classListView.setOnItemClickListener(clicked);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    AdapterView.OnItemClickListener btnClickedMajor = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Set the major to the text of the button just clicked
            majorName = (String) parent.getItemAtPosition(position);
            // Set the action bar to reflect what the user will be selecting next
            if (getActivity() != null) {
                ((AddClassActivity) getActivity()).setActionBarTitle("Select Course");
            }
            classArrayList.clear();
            removedClasses.clear();
            classAdapter.notifyDataSetChanged();
            inputSearch.setText("");
            inputSearch.setHint("Search courses..");
            // Load the buttons to select the course
            loadView(majorName, btnClickedCourse);
        }
    };

    AdapterView.OnItemClickListener btnClickedCourse = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Set the course number to the text of the button just clicked
            courseNum = (String) parent.getItemAtPosition(position);
            // Set the action bar to reflect what the user will be selecting next
            if (getActivity() != null) {
                ((AddClassActivity) getActivity()).setActionBarTitle("Select Section");
            }
            classArrayList.clear();
            removedClasses.clear();
            classAdapter.notifyDataSetChanged();
            // Load the buttons to select the section
            inputSearch.setText("");
            inputSearch.setHint("Search sections..");
            loadView(majorName + "/" + courseNum, btnClickedSection);
        }
    };

    AdapterView.OnItemClickListener btnClickedSection = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Set the section name to the text of the button just clicked
            sectionName = (String) parent.getItemAtPosition(position);
            // Set the action bar to reflect what the user will see on the screen
            if (getActivity() != null) {
                ((AddClassActivity) getActivity()).setActionBarTitle("Class Information");
            }
            classArrayList.clear();
            removedClasses.clear();
            classAdapter.notifyDataSetChanged();
            // Load the class information on the screen for confirmation to add class
            loadClass(majorName + "/" + courseNum + "/" + sectionName);
        }
    };

    public void loadClass(String dir) {
        hideKeyboard(v);
        ((ViewGroup) classListView.getParent()).removeView(classListView);
        ((ViewGroup) inputSearch.getParent()).removeView(inputSearch);
        ScrollView sv = (ScrollView) v.findViewById(R.id.scrollView1);
        sv.setVisibility(View.VISIBLE);
        rl = (LinearLayout) v.findViewById(R.id.linear);



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
                // Get the latitude
                currentClass.setLatitude(val.get("latitude"));
                // Get the longitude
                currentClass.setLongitude(val.get("longitude"));
                // Create a new text view that will store the class information
                TextView textView = null;
                if (getActivity() != null) {
                    textView = new TextView(getActivity());
                }
                // Put the class information in the text view
                textView.setText(currentClass.toString());
                // Display the class information on the interface
                rl.addView(textView);
                // Create a button so the user can add the class
                Button addClassButton = null;
                if (getActivity() != null) {
                    addClassButton = new Button(getActivity());
                }
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
            Student currentStudent = null;
            if (getActivity() != null) {
                currentStudent = ((MyApplication) getActivity().getApplication()).getStudent();
            }
            // Get the current student's schedule
            ArrayList<Classes> studentClasses = currentStudent.getSchedule();
            // Track if the student already has the class they are trying to add in their schedule
            boolean classAlreadyAdded = false;
            // Loop to check if student already has the class they are trying to add in their schedule
            for (int i = 0; i < studentClasses.size(); i++) {
                // If the CRN's of the class they are trying to add is the same as one in their schedule
                if (currentClass.getCRN().equals(studentClasses.get(i).getCRN())) {
                    Toast toast = null;
                    // Tell the user they already has that class
                    if (getActivity() != null) {
                        toast = Toast.makeText(getActivity(), "Classes already exists in schedule.", Toast.LENGTH_LONG);
                    }
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
                if (getActivity() != null) {
                    ((MyApplication) getActivity().getApplication()).getStudent().setSchedule(studentClasses);
                }
                // This is where the student's classes for the database will be stored
                ArrayList<HashMap<String, String>> databaseClasses = new ArrayList<HashMap<String, String>>();
                // Go through all the student's classes and add them to the above list in the from of hashmaps
                for (int i = 0; i < studentClasses.size(); i++) {
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
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    };

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
