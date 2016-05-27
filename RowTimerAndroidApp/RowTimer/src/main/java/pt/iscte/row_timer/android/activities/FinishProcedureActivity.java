package pt.iscte.row_timer.android.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.iscte.row_timer.android.RowTimerApplication;
import pt.iscte.row_timer.android.model.Alignment;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.synchronization.RaceFinishJob;
import pt.iscte.row_timer.android.synchronization.RaceStartJob;

/**
 * TODO : Think on Reorder arrivals by lane
 */
public class FinishProcedureActivity extends AppCompatActivity {
    private static final String TAG = "FinishProcedureActivity";
    private RowTimerApplication application;
    Race race;
    List<Date> arrivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_procedure);
        arrivals = new ArrayList<Date>();

        application = (RowTimerApplication) getApplication();
        race = application.getCurrentRace();
        List<Alignment> crewAlignment = race.getCrewAlignment();

        // Create adapted passing the relevant events
        StartProcedureArrayAdapter adapter = new StartProcedureArrayAdapter(this, crewAlignment);

        // Get ListView from activity_events_list.xml
        final ListView listView = (ListView) findViewById(R.id.finish_alignment);

        if (listView != null) {
            listView.setAdapter(adapter);
        }

    }


    public void markCrewArrival(View view) {
        Log.d(TAG,"marCreArrival()");

        race.setStartTime(new Date());
        arrivals.add(new Date());
    }

    /**
     * All arrivals done.
     * Save them on alignment
     * Goto to list of events
     * @param view
     */
    public void done(View view) {
        Log.d(TAG,"done()");
        List<Alignment> alignment = race.getCrewAlignment();
        for (int i=0 ; i < arrivals.size(); i++ ) {
            Alignment lane = alignment.get(i);
            lane.setEndTime(arrivals.get(i));
        }

        final ProgressDialog ringProgressDialog = ProgressDialog.show(FinishProcedureActivity.this,
                "Please wait ...", "Updating local race results...", true);
        RaceFinishJob rsj = new RaceFinishJob(getApplicationContext(), race,
                new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object o) {
                        ringProgressDialog.dismiss();
                    }
                });
        rsj.execute();

    }
}
