package pt.iscte.row_timer.android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Show the event list, and when the user choose one, it goes to the
 */
public class EventsListActivity extends AppCompatActivity {
    private static final String TAG = "EventsListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        setTitle(R.string.title_activity_event_list);

        // Create adapted passing the relevant events
        EventListArrayAdapter adapter = new EventListArrayAdapter(this, generateData());

        // Get ListView from activity_events_list.xml
        final ListView listView = (ListView) findViewById(R.id.listview);

        // setListAdapter
        if (listView != null) {
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Event event = (Event)listView.getItemAtPosition(position);
                    String toastStr = "ID: " + event.getId() + " - " + event.getTitle();
                    Toast.makeText(getApplicationContext(), toastStr, Toast.LENGTH_SHORT).show();
                    chooseEvent(event.getId());
                }
            });
        }
    }

    private ArrayList<Event> generateData(){
        ArrayList<Event> events = new ArrayList<>();
        for (int i = 1; i <= 10; i++ ){
            events.add(new Event(i, "Event " + i, "Event " + i + " description" ));
        }

        return events;
    }

    /**
     * Clicked to choose the event from the list.
     * When is choosed, it starts an Intent to execute/show EventDetailActivity
     */
    public void chooseEvent(Integer eventID) {
        Log.d(TAG,"Event choosed : "  + eventID);
        Intent gotoEventDetail = new Intent(this, EventDetailActivity.class);
        startActivity(gotoEventDetail);
    }
}
