package com.example.rowrowapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        setTitle(R.string.title_activity_main_test);
    }

    Intent intent;

    /** Called when the user clicks the Event List button */
    public void startEventsListActivity(View view) {
        intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Event Detail button */
    public void startEventDetailActivity(View view) {
        intent = new Intent(this, EventDetailActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Race Mode button */
    public void startRaceModeActivity(View view) {
        intent = new Intent(this, RaceModeActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Login button */
    public void startLoginActivity(View view) {
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
