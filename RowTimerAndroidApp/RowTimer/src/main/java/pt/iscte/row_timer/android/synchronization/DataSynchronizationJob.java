package pt.iscte.row_timer.android.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import pt.iscte.row_timer.android.activities.OnTaskCompleted;
import pt.iscte.row_timer.android.database.RowingEventsDataSource;
import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Created by sergio on 21-05-2016.
 */
public class DataSynchronizationJob extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "DataSynchronizationJob";
    private Context context;
    private OnTaskCompleted listener;
    public static final int GET_EVENT_LIST = 0;
    public static final int GET_EVENT = 1;
    private Integer action;

    private String eventId;

    private List<RowingEvent> events = null;
    private RowingEvent event;

    public DataSynchronizationJob(Context context,Integer action,OnTaskCompleted  listener) {
        this.action = action;
        this.context = context;
        this.listener = listener;
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
        events = null;
        try {
            switch (action) {
                case GET_EVENT_LIST:
                    events = exec.getEventList();
                    database.insertRowingEventList(events);
                    break;
                case GET_EVENT:
                    event = exec.getEventData(eventId);
                    database.synchronizeEvent(event);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Exceuted automatically when the task is finished
     */
    @Override
    protected void onPostExecute(Boolean result) {
        Log.d(TAG,"onPostExecute()");
        // your stuff
        switch (action) {
            case GET_EVENT_LIST:
                listener.onTaskCompleted(events);
                break;
            case GET_EVENT:
                listener.onTaskCompleted(event);
                break;
        }

    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
