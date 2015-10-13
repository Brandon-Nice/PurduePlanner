package com.purdueplanner.purdueplanner;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by menane on 10/13/15.
 */
public class ScheduleActivity extends FragmentActivity{
    @Override
    //provide the onCreate method to apply the Schedule layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
    }
}
