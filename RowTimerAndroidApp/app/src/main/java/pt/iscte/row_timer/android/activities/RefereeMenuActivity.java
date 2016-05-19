package pt.iscte.row_timer.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import pt.iscte.row_timer.android.RowTimerApplication;

public class RefereeMenuActivity extends AppCompatActivity {
    private static final String TAG = "RefereeMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referee_menu);
        RowTimerApplication app = (RowTimerApplication)getApplication();
    }

    /**
     * Clicked to choose to go to start race procedure menu.
     * When choosed, starts an Intent to execute/show StartProcedureActivity
     */
    public void chooseStartProcedure(View view) {
        Log.d(TAG,"Start Procedure choosed");
        // TODO : Pass info informing that we want to go to start procedure after choosing the race
        Intent gotoStartProcedure = new Intent(this, RaceListActivity.class);
        startActivity(gotoStartProcedure);
    }

    /**
     * Clicked to choose to go to start race procedure menu.
     * When choosed, starts an Intent to execute/show StartProcedureActivity
     */
    public void chooseFinishProcedure(View view) {
        Log.d(TAG,"Finish Procedure choosed");
        // TODO : Pass info informing that we want to go to finish procedure after choosing the race
        Intent gotoFinishProcedure = new Intent(this, RaceListActivity.class);
        startActivity(gotoFinishProcedure);
    }

    /**
     * Clicked to choose to go to start race procedure menu.
     * When choosed, starts an Intent to execute/show StartProcedureActivity
     */
    public void chooseUpload(View view) {
        Log.d(TAG,"Upload choosed");
    }

    /**
     * Clicked to choose to go to start race procedure menu.
     * When choosed, starts an Intent to execute/show StartProcedureActivity
     */
    public void chooseDownload(View view) {
        Log.d(TAG,"Download choosed");
    }
}
