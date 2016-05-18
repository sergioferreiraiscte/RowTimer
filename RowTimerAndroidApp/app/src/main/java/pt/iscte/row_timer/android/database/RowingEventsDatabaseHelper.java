package pt.iscte.row_timer.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database lifecycle management and access
 */
public class RowingEventsDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    public RowingEventsDatabaseHelper(Context c) {
        super(c, "ROWING_EVENTS_DATABASE", null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE rowing_event (" +
                "id VARCHAR(128) PRIMARY KEY," +
                "name VARCHAR(128)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS rowing_event");
        onCreate(db);
    }
}
