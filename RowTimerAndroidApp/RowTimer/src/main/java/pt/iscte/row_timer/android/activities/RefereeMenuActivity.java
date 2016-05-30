package pt.iscte.row_timer.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import pt.iscte.row_timer.android.RowTimerApplication;
import pt.iscte.row_timer.android.model.RowingEvent;
import pt.iscte.row_timer.android.synchronization.DataSynchronizationJob;
import pt.iscte.row_timer.android.synchronization.UpdateResultsJob;
import pt.iscte.row_timer.android.synchronization.UpdateStartTimesJob;

public class RefereeMenuActivity extends AppCompatActivity {
    private static final String TAG = "RefereeMenuActivity";
    private RowTimerApplication application;
    private RowingEvent currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referee_menu);
        application = (RowTimerApplication) getApplication();
        currentEvent = application.getCurrentEvent();
    }

    /**
     * Clicked to choose to go to start race procedure menu.
     * When choosed, starts an Intent to execute/show StartProcedureActivity
     */
    public void chooseStartProcedure(View view) {
        Log.d(TAG,"Start Procedure choosed");
        Intent gotoStartProcedure = new Intent(this, RaceListActivity.class);
        gotoStartProcedure.putExtra("referee_type","START");
        startActivity(gotoStartProcedure);
    }

    /**
     * Clicked to choose to go to finish race procedure menu.
     * When choosed, starts an Intent to execute/show FinishProcedureActivity
     */
    public void chooseFinishProcedure(View view) {
        Log.d(TAG,"Finish Procedure choosed");
        Intent gotoFinishProcedure = new Intent(this, RaceListActivity.class);
        gotoFinishProcedure.putExtra("referee_type","ARRIVAL");
        startActivity(gotoFinishProcedure);
    }

    public void chooseUploadStartRaces(View view) {
        Log.d(TAG,"Upload choosed");
        final ProgressDialog ringProgressDialog = ProgressDialog.show(RefereeMenuActivity.this,
                "Please wait ...", "Uploading Races Start Times...", true);
        new UpdateStartTimesJob(getApplicationContext(), currentEvent,
                new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object o) {
                        Log.d(TAG, "onTaskCompleted()");
                        ringProgressDialog.dismiss();
                    }
                }).execute();

        String toastStr = "Start times uploaded";
        Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();
    }

    public void chooseUploadResults(View view) {
        Log.d(TAG,"Upload results");
        final ProgressDialog ringProgressDialog = ProgressDialog.show(RefereeMenuActivity.this,
                "Please wait ...", "Uploading Races Start Times...", true);
        new UpdateResultsJob(getApplicationContext(), currentEvent,
                new OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Object o) {
                        Log.d(TAG, "onTaskCompleted()");
                        ringProgressDialog.dismiss();
                    }
                }).execute();

        String toastStr = "Results uploaded";
        Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();
    }
}
