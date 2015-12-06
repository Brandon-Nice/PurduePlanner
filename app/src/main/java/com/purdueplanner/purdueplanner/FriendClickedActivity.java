package com.purdueplanner.purdueplanner;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendClickedActivity extends AppCompatActivity {

    private ListView friendView;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_clicked);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //ArrayList<String> test = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("friendID");
        String firstName = extras.getString("first");
        String lastName = extras.getString("last");
        String combinedName = firstName + " " + lastName;
        TextView name = (TextView) findViewById(R.id.friendName);
        name.setText(combinedName);

        //final ArrayList<HashMap<String, String>> classes = new ArrayList<HashMap<String, String>>();
        final ArrayList<HashMap<String, String>> classes = new ArrayList<>(); //array list to store the classes
        final ArrayList<String> courseInfo = new ArrayList<String>(); //stores the course info
        final ArrayList<String> formattedClasses = new ArrayList<String>(); //stores the formatted classes with the classes + times

        Firebase tempRef = new Firebase("https://purduescheduler.firebaseio.com/Students/" + id);
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("Snapshot: ", snapshot.toString());
                HashMap<String, Object> tempHash = (HashMap) snapshot.getValue();        /* Hashmap of a Student's content   */
                Log.i("Hashmap: ", tempHash.toString());
                ArrayList<HashMap<String, String>> testList = (ArrayList<HashMap<String, String>>) tempHash.get("Schedule");     /* ArrayList of Hashmaps containing the classes*/
                String cCourse, cMajor, cSec, actualClass; //local variables to store class info to concatenate into an actual class
                if (testList != null) {
                    Log.i("ArrayList: ", testList.toString());
                    for (int j = 0; j < testList.size(); j++) {
                        HashMap<String, String> courseMap = new HashMap<String, String>();
                        cCourse = testList.get(j).get("Course");       /* Major, Course, Section of each class    */
                        cMajor = testList.get(j).get("Major");
                        cSec = testList.get(j).get("Section");

                        actualClass = cMajor + " " + cCourse + " " + cSec; //formats them nicely
                        formattedClasses.add(j, actualClass); //adds them to the formatted class list

                        courseMap.put("Course", cCourse);
                        courseMap.put("Major", cMajor);
                        courseMap.put("Section", cSec);
                        classes.add(j, courseMap);
                    }
                    for (int i = 0; i < classes.size(); i++) {
                        //Log.i("********************", "1");
                        final String section = classes.get(i).get("Section");
                        Firebase classRef = new Firebase("https://purduescheduler.firebaseio.com/Classes/" + classes.get(i).get("Major") + "/" + classes.get(i).get("Course") + "/");
                        final String innerI = Integer.toString(i); //only way to access "i" inside of the listener = make it to string and convert it back to int
                        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            public void onDataChange(DataSnapshot snapshot) {

                                Log.i("Snapshot: ", snapshot.toString());
                                HashMap<String, Object> tempHash = (HashMap) snapshot.getValue();        /* Hashmap of a Student's content   */
                                Log.i("Hashmap: ", tempHash.toString());

                                HashMap<String, String> testList = (HashMap<String, String>) tempHash.get(section);     /* ArrayList of Hashmaps containing the classes*/
                                String sStart, sEnd, sDays;
                                String formattedInfo, c = "";
                                if (testList != null) { //add the info to the classInfo list
                                    sStart = testList.get("startTime");
                                    sEnd = testList.get("endTime");
                                    sDays = testList.get("days");
                                    formattedInfo = " " + sStart + " - " + sEnd + " " + sDays; //formats the info nicely
                                    c = formattedClasses.get(Integer.parseInt(innerI)).concat(formattedInfo); //FINALLY adds the course info to the class
                                }

                                formattedClasses.remove(Integer.parseInt(innerI)); //remove the old value (that previously had just the classes)
                                formattedClasses.add(Integer.parseInt(innerI), c); //add the new value of the class with the info
                                friendView = (ListView) findViewById(R.id.friendClassView);
                                adapter = new ArrayAdapter(FriendClickedActivity.this, android.R.layout.simple_list_item_1, formattedClasses);
                                friendView.setAdapter(adapter);

                            }


                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }
}