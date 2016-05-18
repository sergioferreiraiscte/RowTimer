package pt.iscte.row_timer.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Class that handles local (adnroid) database interaction
 */
public class RowingEventsDataSource {
    private static final String TAG = "RowingEventsDataSource";
    protected SQLiteDatabase db;
    protected RowingEventsDatabaseHelper dbhelper;

    public RowingEventsDataSource (Context c) {
        dbhelper = new RowingEventsDatabaseHelper(c);
    }

    public void open() {
        db = dbhelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    // CRUD

    /**
     * Create a new Rowing event witl all info associated
     *
     * @param rowingEvent
     * @return
     */
    public RowingEvent createRowingEvent(RowingEvent rowingEvent) {
        Log.d(TAG,"createRowingEvent()");
        ContentValues values = new ContentValues();
        values.put("id", rowingEvent.getId());
        values.put("name", rowingEvent.getName());

        long lastId = db.insert("rowing_event",null, values);

        return rowingEvent;
    }

    /**
     * Read the rowing event information
     *
     * @param eventId
     * @return
     */
    public RowingEvent readRowingEvent(String eventId) {
        Log.d(TAG,"readRowingEvent()");
        Cursor c = db.rawQuery("SELECT * FROM rowing_event WHERE id = '" + eventId + "'", null);

        if(c.getCount() == 0) {
            return null;
        } else {
            c.moveToFirst();
            RowingEvent rowingEvent = new RowingEvent(
                    eventId,
                    c.getString(1));
            c.close();
            Log.d(TAG,"Rowing Event : " + rowingEvent.getId() + "/" + rowingEvent.getName());
            return rowingEvent;
        }
    }

    public void update(RowingEvent rowingEvent) {

    }

    public void delete(String eventId) {
        int result = db.delete("rowing_event", "id=?", new String[] { eventId });
    }
}
