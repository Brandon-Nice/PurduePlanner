package com.purdueplanner.purdueplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.firebase.client.Firebase;
import java.util.ArrayList;

/**
 * Created by Kenny on 10/17/2015.
 */
public class ClassInfo extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        Firebase ref = new Firebase("https://purduescheduler.firebaseio.com/Classes/CS/CS 307000 : Software Engineering");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView dayListView = (ListView) findViewById(R.id.dayList);
        ArrayList<String> classList = new ArrayList<>();
        //classList.add(ref.getKey(courseNum));
        classList.add("CS 307");
        classList.add("CS 391");
        classList.add("MA 265");
        customAdapter arrayAdapter = new customAdapter(classList, this);
        dayListView.setAdapter(arrayAdapter);
    }
}
