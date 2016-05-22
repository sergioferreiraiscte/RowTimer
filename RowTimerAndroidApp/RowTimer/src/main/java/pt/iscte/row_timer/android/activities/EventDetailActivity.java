package pt.iscte.row_timer.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class EventDetailActivity extends AppCompatActivity {
    private static final String TAG = "EventDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        setTitle(R.string.title_activity_event_detail);
    }

    /**
     * Clicked to choose to go to referee menu.
     * When choosed, starts an Intent to execute/show RefereeMenuActivity
     */
    public void chooseRefereeMenu(View view) {
        Log.d(TAG,"Referee menu choosed");
        Intent gotoRefereeMenu = new Intent(this, RefereeMenuActivity.class);
        startActivity(gotoRefereeMenu);
    }
}
