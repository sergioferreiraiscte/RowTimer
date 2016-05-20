package pt.iscte.row_timer.android;

import android.app.Application;
import android.util.Log;
import android.view.View;

import java.util.List;

import pt.iscte.row_timer.android.activities.GetRemoteDataJob;
import pt.iscte.row_timer.android.activities.OnTaskCompleted;
import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Contains the information loaded in the activities to be navigated and executed
 * When the application starts, check if there is connectivity, if so, check if the info it have
 * is actual, and if not, get the current information
 */
public class RowTimerApplication extends Application {
    private static final String TAG = "RowTimerApplication";
    private List<RowingEvent> eventList;
    private RowingEvent currentEvent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Connect to URL and get JSON
     * TODO : Some stuff should be done to have good synchronization
     */
    public void getRemoteData(View view) {
        Log.d(TAG,"getRemoteData()");
        new GetRemoteDataJob(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object o) {
                // TODO : Sore data
            }
        }).execute();
    }


    public RowingEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(RowingEvent currentEvent) {
        this.currentEvent = currentEvent;
    }
}
