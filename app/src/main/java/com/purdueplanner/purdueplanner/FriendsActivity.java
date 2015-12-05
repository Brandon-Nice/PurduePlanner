package com.purdueplanner.purdueplanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    String previousText = "";

    ArrayList<HashMap<String, String>> friends = new ArrayList();
    ArrayList<HashMap<String, String>> removedFriends = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        final FriendsAdapter friendAdapter = new FriendsAdapter(friends, FriendsActivity.this);
        final ListView tempFriendsList = (ListView) findViewById(R.id.friendsList);   /* Sets names of friends to view */
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
                            for (int i = 0; i < friendsList.length(); i++) {             /* Loop to go through every friend that the user has and collect their classes */
                                JSONObject friendMap = (JSONObject)friendsList.get(i);String name = friendMap.getString("name");
                                String firstName = name.substring(0, name.indexOf(" "));
                                String lastName = name.substring(name.indexOf(" ") + 1);
                                String id = friendMap.getString("id");
                                HashMap<String, String> friend = new HashMap();
                                friend.put("firstName", firstName);
                                friend.put("lastName", lastName);
                                friend.put("id", id);
                                friends.add(friend);
                            }
                            System.out.println(friends);

                            sortFriends(friends);

                            friendAdapter.notifyDataSetChanged();
                            tempFriendsList.setAdapter(friendAdapter);

                            //Added functionality to open a new activity for each friend clicked
                            tempFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    HashMap<String, String> friend = (HashMap) parent.getItemAtPosition(position);
                                    String friendID = friend.get("id");
                                    //Goes to a new activity once a button is pressed
                                    Intent myIntent = new Intent(FriendsActivity.this, FriendClickedActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("friendID", friendID);
                                    myIntent.putExtras(bundle);
                                    //myIntent.putExtra("name_key", name); //adds the string to a HashMap like object
                                    startActivity(myIntent); //goes to new activity once the button is pressed
                                }

                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException n){ //means that the user is offline
                            //TODO: Implement actions for button
                            Toast.makeText(getApplicationContext(), "Lost connect.", Toast.LENGTH_LONG).show();
                            AlertDialog alertDialog = new AlertDialog.Builder(FriendsActivity.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("Oops! Looks like you aren't connected to Wifi or a mobile network at the moment. Would you like to connect or exit?");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();

                            n.printStackTrace();
                        }
                        //Log.i("OKAY", yo.toString());
                    }

                }
        ).executeAsync();

        final EditText inputSearch = (EditText) findViewById(R.id.inputSearch);

        tempFriendsList.setFocusable(false);
        tempFriendsList.setFocusableInTouchMode(false);
        inputSearch.setFocusable(true);
        inputSearch.setFocusableInTouchMode(true);

        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("hello");
                    hideKeyboard(v);
                    inputSearch.setFocusable(false);
                    inputSearch.setFocusableInTouchMode(false);
                    tempFriendsList.setFocusable(true);
                    tempFriendsList.setFocusableInTouchMode(true);


                    return true;
                }
                return false;
            }
        });

        inputSearch.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tempFriendsList.setFocusable(false);
                tempFriendsList.setFocusableInTouchMode(false);
                inputSearch.setFocusable(true);
                inputSearch.setFocusableInTouchMode(true);
                return false;
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String currentText = cs.toString();
                if (currentText.length() > previousText.length())
                {
                    for (int i = 0; i < friends.size(); i++) {
                        String name = friends.get(i).get("firstName") + " "  + friends.get(i).get("lastName");
                        if (!name.toLowerCase().contains(currentText.toLowerCase())) {
                            removedFriends.add(friends.get(i));
                            friends.remove(i);
                            i--;
                        }
                    }
                }
                else {
                    for (int i = 0; i < removedFriends.size(); i++) {
                        String name = removedFriends.get(i).get("firstName") + " "  + removedFriends.get(i).get("lastName");
                        if (name.toLowerCase().contains(currentText.toLowerCase())) {
                            friends.add(removedFriends.get(i));
                            removedFriends.remove(i);
                            i--;
                        }
                    }
                }
                sortFriends(friends);
                friendAdapter.notifyDataSetChanged();
                previousText = currentText;
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });



    }

    public void sortFriends(ArrayList<HashMap<String, String>> list)
    {
        Comparator<HashMap<String, String>> classSorter = new Comparator<HashMap<String, String>>() {       /* Custom comparator to sort Hashmap of friends*/
            @Override
            public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
                return lhs.get("lastName").compareTo(rhs.get("lastName"));
            }
        };

        Collections.sort(list, classSorter);

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
