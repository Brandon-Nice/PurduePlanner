package com.purdueplanner.purdueplanner;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Adds the Facebook friends
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new
                GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    //TODO: Have to decode the JSON object
                    object = response.getJSONObject();
                    try {
                        if (object != null) {
                            String user_id = object.getString("id");
                            String user_name = object.getString("name");
                            //TODO: Try to get friendlists
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,friendlists");
        request.setParameters(parameters);
        request.executeAsync();


    }

}
