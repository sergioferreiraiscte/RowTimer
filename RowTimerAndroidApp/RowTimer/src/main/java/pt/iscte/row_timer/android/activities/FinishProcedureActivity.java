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
import pt.iscte.row_timer.android.model.Result;
import pt.iscte.row_timer.android.synchronization.RaceFinishJob;
import pt.iscte.row_timer.android.synchronization.RaceStartJob;

/**
 * TODO : Think on Reorder arrivals by lane
 */
public class FinishProcedureActivity extends AppCompatActivity {
    private static final String TAG = "FinishProcedureActivity";
    private RowTimerApplication application;
    Race race;
    List<Result> arrivals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finish_procedure);
        arrivals = new ArrayList<Result>();

        application = (RowTimerApplication) getApplication();
        race = application.getCurrentRace();
        List<Alignment> crewAlignment = race.getCrewAlignment();

        // Get ListView from activity_events_list.xml
        final ListView listView = (ListView) findViewById(R.id.finish_alignment);

        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            Camera2VideoFragment cameraFragment = Camera2VideoFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, cameraFragment)
                    .commit();
        }
    }

    /*
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
    */


    /**
     * A crew has arrived and the referee mark is arrival time
     *
     * @param view
     */
    public void markCrewArrival(View view) {
        Log.d(TAG,"markCrewArrival()");

        Result result = new Result();
        result.setFinishTime(new Date());
        result.setEvent_id(race.getEventId());
        result.setRaceno(race.getSeqno());
        arrivals.add(result);
    }

    /**
     * Edit the order of the arrivals
     * Open a dialog with a list that edit the ResultList
     */
    public void editOrder() {

    }

    /**
     * All arrivals done.
     * Save them on alignment
     * Goto to list of events
     * @param view
     */
    public void done(View view) {
        Log.d(TAG,"done()");

        // TODO : Should update the corresponding team
        List<Alignment> alignment = race.getCrewAlignment();
        for (int i=0 ; i < arrivals.size(); i++ ) {
            Alignment lane = alignment.get(i);
            lane.setEndTime(arrivals.get(i).getFinishTime());
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
