package pt.iscte.row_timer.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.row_timer.android.RowTimerApplication;
import pt.iscte.row_timer.android.model.RowingEvent;
import pt.iscte.row_timer.android.synchronization.DataSynchronizationJob;

/**
 * Show the event list, and when the user choose one, it goes to the
 */
public class EventsListActivity extends AppCompatActivity {
    private static final String TAG = "EventsListActivity";
    private RowingEvent rowingEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        RowTimerApplication application = (RowTimerApplication) getApplication();
        List<RowingEvent> eventList = application.getEventList();
        if (eventList == null) {
            // TODO : This is temporary - Should tell that there are no list of events
            Log.d(TAG, "List of events is null");
            return;
        }

        // Create adapted passing the relevant events
        EventListArrayAdapter adapter = new EventListArrayAdapter(this, eventList);

        // Get ListView from activity_events_list.xml
        final ListView listView = (ListView) findViewById(R.id.listview);

        // setListAdapter
        if (listView != null) {
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    RowingEvent selectedRowingEvent = (RowingEvent) listView.getItemAtPosition(position);

                    Log.d(TAG, "getRemoteData()");
                    final ProgressDialog ringProgressDialog = ProgressDialog.show(EventsListActivity.this,
                            "Please wait ...", "Downloading Event Information ...", true);
                    DataSynchronizationJob dsj = new DataSynchronizationJob(getApplicationContext(),
                            DataSynchronizationJob.GET_EVENT,
                            new OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted(Object o) {
                                    Log.d(TAG, "onTaskCompleted()");
                                    rowingEvent = (RowingEvent) o;
                                    // TODO : Check if rowing event is saved as current on application
                                    ringProgressDialog.dismiss();
                                    chooseEvent(rowingEvent.getId());
                                }
                            });
                    dsj.setEventId(selectedRowingEvent.getId());
                    dsj.execute();
                    String toastStr = "ID: " + selectedRowingEvent.getId() + " - " + selectedRowingEvent.getName();
                    Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /**
     * Clicked to choose the event from the list.
     * When is choosed, it starts an Intent to execute/show EventDetailActivity
     */
    public void chooseEvent(String eventID) {
        Log.d(TAG, "Event choosed : " + eventID);
        RowTimerApplication application = (RowTimerApplication) getApplication();
        application.setCurrentEvent(rowingEvent);
        Intent gotoRefereeMenu = new Intent(this, RefereeMenuActivity.class);
        startActivity(gotoRefereeMenu);
        /*
        Intent gotoEventDetail = new Intent(this, EventDetailActivity.class);
        startActivity(gotoEventDetail);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

}
