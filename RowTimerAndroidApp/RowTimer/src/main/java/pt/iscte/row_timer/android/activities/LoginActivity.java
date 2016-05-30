package pt.iscte.row_timer.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import pt.iscte.row_timer.android.RowTimerApplication;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    RowTimerApplication rowTimerApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.

        rowTimerApplication = (RowTimerApplication) getApplication();
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);
        facebookLoginSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        //AppEventsLogger.deactivateApp(this);
    }



    /** Called when the user clicks the Event List button */
    public void checkLoginCredentials(View view) {
        Intent intent;

        // LOGIN CHECKING ....
        boolean validated = true;

        if(validated){
            intent = new Intent(this, EventsListActivity.class);
            startActivity(intent);
        }
    }

    public void facebookLoginSetup() {
        Log.d(TAG,"facebookLoginSetup()");
        LoginButton loginButton;
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));

        CallbackManager callbackManager;
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"onSuccess() : " + loginResult.getAccessToken());
                // App code
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"onCancel()" );
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG,"onError()" );
                // App code
            }
        });


    }
}
