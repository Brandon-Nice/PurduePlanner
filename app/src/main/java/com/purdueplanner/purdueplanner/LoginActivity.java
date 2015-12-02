package com.purdueplanner.purdueplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by menane on 10/13/15.
 */



public class LoginActivity extends Activity {
    //Declaring variables
    private TextView info;
    public static LoginButton loginButton;
    public static CallbackManager callbackManager;
    public AccessTokenTracker accessTokenTracker;

    @Override
    //provide the onCreate method to apply the Friends layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        setupActionBar();

        if (isLoggedIn())
        {
            boolean fromNavMenu = false;
            if (getIntent().hasExtra("FromNavMenu"))
            {
                fromNavMenu = getIntent().getExtras().getBoolean("FromNavMenu");
            }

            if (fromNavMenu == false)
            {
                loginStudent(AccessToken.getCurrentAccessToken().getUserId());
            }

        }



        callbackManager = CallbackManager.Factory.create();
        //gets the login button from activity_login.xml
        loginButton = (LoginButton) findViewById(R.id.fb_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("user_friends");
        //gets the textview from activity_login.xml
        info = (TextView) findViewById(R.id.fb_info);



        //Creates a callback function to handle the results of the login attempts
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginStudent(loginResult.getAccessToken().getUserId());
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");
            }

        });

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                if (currentAccessToken == null){
                    Intent init = new Intent(LoginActivity.this, LoginActivity.class);
                    init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(init);
                    finish();
                }
            }
        };





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setupActionBar() {
        android.app.ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Friends");
        }
    }

    //LoginButton getter
    static LoginButton getLoginButton() {
        return loginButton;
    }
    //CallbackManager getter
    static CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void loginStudent(final String userID)
    {
        Student currentStudent = currentStudent = new Student(userID, null, null, new ArrayList<Classes>());
        ((MyApplication) getApplication()).setStudent(currentStudent);
        Intent init = new Intent(LoginActivity.this, StartActivity.class);
        init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(init);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (accessTokenTracker != null) {
            accessTokenTracker.stopTracking();
        }
    }

}
