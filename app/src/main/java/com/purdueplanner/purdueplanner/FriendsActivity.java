package com.purdueplanner.purdueplanner;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.FacebookGraphResponseException;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class FriendsActivity extends AppCompatActivity {


    static LoginButton loginButton;
    static CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                               Log.i("INFO", response.toString()); //DISPLAYS ON LOGCAT

                        JSONObject test = response.getJSONObject();
                        try {
                            JSONArray friendsList = test.getJSONArray("data");
                            ArrayList<String> friendsArrayList = new ArrayList<String>();
                            ArrayList<HashMap<String, String>> classesPreSortList = new ArrayList<HashMap<String, String>>();
                            for(int i = 0; i < friendsList.length(); i++) {             /* Loop to go through every friend that the user has and collect their classes */
                                Log.i("User:  " + i, friendsList.get(i).toString());
                                JSONObject friendMap = (JSONObject)friendsList.get(i);
                                friendsArrayList.add(i, friendMap.getString("name"));       /* ArrayList to store the name of every friend      */
                                JSONObject a = friendsList.getJSONObject(i);

                                final Firebase tempRef = new Firebase("https://purduescheduler.firebaseio.com/Students/" + a.get("id").toString());     /* Firebase reference to a user's friend    */
                                tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Log.i("Snapshot: ", snapshot.toString());
                                        HashMap<String, Object> tempHash = (HashMap) snapshot.getValue();        /* Hashmap of a Student's content   */
                                        Log.i("Hashmap: ", tempHash.toString());
                                        ArrayList<HashMap<String, String>> testList = (ArrayList<HashMap<String, String>>) tempHash.get("Schedule");     /* ArrayList of Hashmaps containing the */
                                        if (testList != null) {
                                            Log.i("ArrayList: ", testList.toString());

                                            for (int j = 0; j < testList.size(); j++) {
                                                testList.get(j).get("Course");       /* Major, Course, Section of each class    */
                                                testList.get(j).get("Major");
                                                testList.get(j).get("Section");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }
                            Comparator<HashMap<String, String>> classSorter = new Comparator<HashMap<String, String>>() {       /* Custom comparator to sort Hashmap of friends*/
                                @Override
                                public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
                                    return lhs.get("lastName").compareTo(rhs.get("lastName"));
                                }
                            };

                            for(int h = 0; h < friendsArrayList.size(); h++) {
                                HashMap<String, String> classesPreSort = new HashMap<String, String>();
                                String name = friendsArrayList.get(h);
                                String firstName = name.substring(0, name.indexOf(" "));
                                String lastName = name.substring(name.indexOf(" "));
                                classesPreSort.put("firstName", firstName);
                                classesPreSort.put("lastName", lastName);
                                classesPreSortList.add(h, classesPreSort);
                            }
                            Log.i("Unsorted hashmap: ", classesPreSortList.toString());
                            Log.i("Friends Arraylist: ", friendsArrayList.toString());
                            Collections.sort(classesPreSortList, classSorter);          /* Sorts hashmap of classes */
                            Log.i("Sorted hashmap: ", classesPreSortList.toString());

                            friendsArrayList.clear();                                   /* Clears old ArrayList and adds the sorted entries from Hashmap */
                            for(int k = 0; k < classesPreSortList.size(); k++) {
                                friendsArrayList.add(k, classesPreSortList.get(k).get("firstName") + classesPreSortList.get(k).get("lastName"));
                            }

                            ListView tempFriendsList = (ListView) findViewById(R.id.friendsList);   /* Sets names of friends to view */
                            ArrayAdapter friendAdapter;
                            friendAdapter = new ArrayAdapter(FriendsActivity.this, android.R.layout.simple_list_item_1, friendsArrayList);

                            tempFriendsList.setAdapter(friendAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.i("OKAY", yo.toString());
                    }

                }
        ).executeAsync();

    }

}
