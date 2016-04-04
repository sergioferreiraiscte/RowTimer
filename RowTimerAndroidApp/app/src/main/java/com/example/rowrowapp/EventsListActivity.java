package com.example.rowrowapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EventsListActivity extends AppCompatActivity {

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
}
