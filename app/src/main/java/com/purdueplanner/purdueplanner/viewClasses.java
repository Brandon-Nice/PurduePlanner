package com.purdueplanner.purdueplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class viewClasses extends AppCompatActivity {

    private String[] testArray = {"A", "B", "C", "D"};
    private ListView classListView;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_classes);


        ArrayList<Classes> classList = new ArrayList<>();
        ArrayList<String> stringList = new ArrayList<>();

        classList = ((MyApplication) getApplication()).getStudent().getSchedule();
        Classes currentClass = new Classes();
        //classList.add(currentClass);
        int size = classList.size();
        for (int i = 0; i < size; i++) {
            stringList.add(classList.get(i).getMajor() + " " + classList.get(i).getCourseNum() + "\n" +
                    classList.get(i).getDays() + "\t" +
                    classList.get(i).getStartTime()
                     + " - " + classList.get(i).getEndTime() + "\n" +
                    classList.get(i).getLocation());
        }

        // 3 lines below work!
        classListView = (ListView) findViewById(R.id.classView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringList);
        classListView.setAdapter(adapter);


    }


}
