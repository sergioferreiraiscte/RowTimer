package pt.iscte.row_timer.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.row_timer.android.RowTimerApplication;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.model.RowingEvent;
import pt.iscte.row_timer.android.synchronization.DataSynchronizationJob;

public class RaceListActivity extends AppCompatActivity {
    private static final String TAG = "RaceListActivity";
    private RowTimerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_list);

        application = (RowTimerApplication) getApplication();
        List<Race> raceList = application.getCurrentEvent().getEventRaces();

        // Create adapted passing the relevant events
        RaceListArrayAdapter adapter = new RaceListArrayAdapter(this, raceList);

        // Get ListView from activity_events_list.xml
        final ListView listView = (ListView) findViewById(R.id.race_list);

        // setListAdapter
        if (listView != null) {
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Race selectedRace = (Race) listView.getItemAtPosition(position);
                    application.setCurrentRace(selectedRace);
                    callNextActivity();
                }
            });
        }
    }


    private void callNextActivity() {
        Intent callingIntent = getIntent();
        String refereeType = callingIntent.getStringExtra("referee_type");
        Class destActivity = null;
        switch (refereeType) {
            case "START":
                destActivity = StartProcedureActivity.class;
                break;
            case "ARRIVAL":
                destActivity = FinishProcedureActivity.class;
                break;
        }
        Intent gotoEventDetail = new Intent(this, destActivity);
        startActivity(gotoEventDetail);
    }
}
