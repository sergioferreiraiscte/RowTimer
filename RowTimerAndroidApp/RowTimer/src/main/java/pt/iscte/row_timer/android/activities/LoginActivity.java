package pt.iscte.row_timer.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.facebook.FacebookSdk;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.

        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);
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


    Intent intent;

    /** Called when the user clicks the Event List button */
    public void checkLoginCredentials(View view) {

        // LOGIN CHECKING ....
        boolean validated = true;

        if(validated){
            intent = new Intent(this, EventsListActivity.class);
            startActivity(intent);
        }
    }
}
