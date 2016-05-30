package pt.iscte.row_timer.android;

import android.app.Application;
import android.util.Log;
import android.view.View;

import java.util.List;

import pt.iscte.row_timer.android.activities.EventsListActivity;
import pt.iscte.row_timer.android.activities.GetRemoteDataJob;
import pt.iscte.row_timer.android.activities.OnTaskCompleted;
import pt.iscte.row_timer.android.database.RowingEventsDataSource;
import pt.iscte.row_timer.android.model.Login;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.model.RowingEvent;
import pt.iscte.row_timer.android.synchronization.ConnectionUtil;
import pt.iscte.row_timer.android.synchronization.DataSynchronizationJob;

/**
 * Contains the information loaded in the activities to be navigated and executed
 * When the application starts, check if there is connectivity, if so, check if the info it have
 * is actual, and if not, get the current information
 */
public class RowTimerApplication extends Application {
    private static final String TAG = "RowTimerApplication";
    private List<RowingEvent> eventList;
    private RowingEvent currentEvent;
    private Race currentRace;
    private Login login;

    @Override
    public void onCreate() {
        super.onCreate();
        if ( ConnectionUtil.haveNetworkConnection(this) )
          synchronizeRemoteEventList();
        else
          getLocalEventList();
    }

    /**
     * Do Sychronization of Information
     */
    private void synchronizeRemoteEventList() {
        Log.d(TAG,"getRemoteData()");
        new DataSynchronizationJob(this, DataSynchronizationJob.GET_EVENT_LIST, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object o) {
                Log.d(TAG,"onTaskCompleted()");
                eventList = (List<RowingEvent>) o;
            }
        }).execute();
    }

    /**
     * Get information that exist on current mobile
     */
    private void getLocalEventList() {
        RowingEventsDataSource database = new RowingEventsDataSource(this);
        //eventList = database.readRowingEventList();
    }

    /**
     * Check if username and password are valid and store the information in application level
     *
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        // If have connectivity
        // Check on remote database
       // Check in local database
        // If valid
        // store info on Login object
    }


    public RowingEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(RowingEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    public List<RowingEvent> getEventList() {
        return eventList;
    }

    public void setEventList(List<RowingEvent> eventList) {
        this.eventList = eventList;
    }

    public Race getCurrentRace() {
        return currentRace;
    }

    public void setCurrentRace(Race currentRace) {
        this.currentRace = currentRace;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
