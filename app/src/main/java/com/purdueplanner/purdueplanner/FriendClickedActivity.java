package com.purdueplanner.purdueplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.Firebase;

import org.json.JSONObject;

import java.util.ArrayList;

public class FriendClickedActivity extends AppCompatActivity {

    private ListView friendView;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_clicked);

        ArrayList<String> test = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("friendID");
        test.add(0, id);

        friendView = (ListView) findViewById(R.id.friendClassView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, test);
        friendView.setAdapter(adapter);
    }
    //TO-DO: Format friends layout
    //How to get users schedule from database?


}
