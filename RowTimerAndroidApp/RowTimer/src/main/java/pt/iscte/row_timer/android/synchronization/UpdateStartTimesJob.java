package pt.iscte.row_timer.android.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import pt.iscte.row_timer.android.activities.OnTaskCompleted;
import pt.iscte.row_timer.android.database.RowingEventsDataSource;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.model.RowingEvent;
import pt.iscte.row_timer.android.model.StartRace;

/**
 * Created by sergio on 24-05-2016.
 */
public class UpdateStartTimesJob extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "UpdateStartTimesJob";
    private Context context;
    private OnTaskCompleted listener;

    private RowingEvent event;

    public UpdateStartTimesJob(Context context, RowingEvent rowingEvent, OnTaskCompleted  listener) {
        this.context = context;
        this.listener = listener;
        this.event = rowingEvent;
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
        List<StartRace> startTimes = database.readStartTimes(event.getId());
        try {
            exec.sendStartTimes(event.getId(), startTimes);
        } catch (Exception e) {
            String toastStr = "Error sending start times to server";
            Toast.makeText(context, toastStr, Toast.LENGTH_SHORT).show();
        }
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
