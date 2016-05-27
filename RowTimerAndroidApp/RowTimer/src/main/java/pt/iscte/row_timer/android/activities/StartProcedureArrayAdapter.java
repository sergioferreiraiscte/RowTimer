package pt.iscte.row_timer.android.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pt.iscte.row_timer.android.model.Alignment;
import pt.iscte.row_timer.android.model.Race;

/**
 * Adapter to see the lanes of race alignment
 */
public class StartProcedureArrayAdapter extends ArrayAdapter<Alignment> {

    private final Context context;
    private final List<Alignment> alignment;

    public StartProcedureArrayAdapter(Context context, List<Alignment> alignment) {
        super(context, R.layout.activity_start_procedure, alignment);

        this.context = context;
        this.alignment = alignment;
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
        labelView.setText(alignment.get(position).getLane().toString());
        valueView.setText(alignment.get(position).getCrew().getCompetitor().getName());

        // 5. retrn rowView
        return rowView;
    }
}