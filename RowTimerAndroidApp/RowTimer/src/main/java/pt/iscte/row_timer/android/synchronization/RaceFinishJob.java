package pt.iscte.row_timer.android.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import pt.iscte.row_timer.android.activities.OnTaskCompleted;
import pt.iscte.row_timer.android.database.RowingEventsDataSource;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Created by sergio on 24-05-2016.
 */
public class RaceFinishJob extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "RaceStartJob";
    private Context context;
    private OnTaskCompleted listener;
    private Race race;

    private List<RowingEvent> events = null;
    private RowingEvent event;

    public RaceFinishJob(Context context, Race race, OnTaskCompleted  listener) {
        this.context = context;
        this.listener = listener;
        this.race = race;
    }

    /**
     * TODO : Add check connectivity
     * @param params
     * @return
     */
    @Override
    protected Boolean doInBackground(String... params) {
        Log.d(TAG,"doInBackground()");
        ExecuteRemoteServices exec = new ExecuteRemoteServices();
        RowingEventsDataSource database = new RowingEventsDataSource(context);
        database.storeResults(race);
        return true;
    }

    /**
     * Exceuted automatically when the task is finished
     */
    @Override
    protected void onPostExecute(Boolean result) {
        Log.d(TAG,"onPostExecute()");
        listener.onTaskCompleted(null);
    }

}
