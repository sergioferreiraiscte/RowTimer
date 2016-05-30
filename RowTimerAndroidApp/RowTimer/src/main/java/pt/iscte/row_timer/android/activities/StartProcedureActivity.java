package pt.iscte.row_timer.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import pt.iscte.row_timer.android.RowTimerApplication;
import pt.iscte.row_timer.android.model.Alignment;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.model.RowingEvent;
import pt.iscte.row_timer.android.synchronization.DataSynchronizationJob;
import pt.iscte.row_timer.android.synchronization.RaceStartJob;

public class StartProcedureActivity extends AppCompatActivity {
    private static final String TAG = "StartProcedureActivity";
    private RowTimerApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_procedure);
        setTitle("lane alignment");

        application = (RowTimerApplication) getApplication();
        List<Alignment> crewAlignment = application.getCurrentRace().getCrewAlignment();

        // Create adapted passing the relevant events
        StartProcedureArrayAdapter adapter = new StartProcedureArrayAdapter(this, crewAlignment);

        // Get ListView from activity_events_list.xml
        final ListView listView = (ListView) findViewById(R.id.start_alignment);

        if (listView != null) {
            listView.setAdapter(adapter);
        }
    }

    public void markStartOfRace(View view) {
        TextView v = (TextView) findViewById(R.id.tvCountdown);
        v.setText("15''");
        Log.d(TAG,"markStartOfRace()");
        Race race = application.getCurrentRace();
        race.setStartTime(new Date());

        final ProgressDialog ringProgressDialog = ProgressDialog.show(StartProcedureActivity.this,
                "Please wait ...", "Updating race start time...", true);
        RaceStartJob rsj = new RaceStartJob(getApplicationContext(), race,
                new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object o) {
                        Log.d(TAG, "onTaskCompleted()");
                        ringProgressDialog.dismiss();
                    }
                });
        rsj.execute();
    }

}
