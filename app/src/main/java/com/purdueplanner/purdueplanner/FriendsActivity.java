package com.purdueplanner.purdueplanner;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;

/**
 * Created by menane on 10/13/15.
 */
// Comment
public class FriendsActivity extends FragmentActivity {
    @Override
    //provide the onCreate method to apply the Friends layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        setupActionBar();
    }

    private void setupActionBar() {
        android.app.ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Example");
        }
    }
}
