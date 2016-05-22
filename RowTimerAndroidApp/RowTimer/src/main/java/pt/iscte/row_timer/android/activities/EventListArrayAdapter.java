package pt.iscte.row_timer.android.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Adapter to see the list of events
 */
public class EventListArrayAdapter extends ArrayAdapter<RowingEvent> {

    private final Context context;
    private final List<RowingEvent> rowingEvents;

    public EventListArrayAdapter(Context context, List<RowingEvent> rowingEvents) {
        super(context, R.layout.event_list_row, rowingEvents);

        this.context = context;
        this.rowingEvents= rowingEvents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.event_list_row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.value);

        // 4. Set the text for textView
        labelView.setText(rowingEvents.get(position).getId());
        valueView.setText(rowingEvents.get(position).getName());

        // 5. retrn rowView
        return rowView;
    }
}