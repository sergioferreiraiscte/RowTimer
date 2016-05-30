package pt.iscte.row_timer.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database lifecycle management and access
 */
public class RowingEventsDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "RowingEventsDBHelper";
    private static final int DATABASE_VERSION = 7;

    public RowingEventsDatabaseHelper(Context c) {
        super(c, "ROWING_EVENTS_DATABASE", null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate()");
        db.execSQL("CREATE TABLE rowing_event (" +
                "  id VARCHAR(128) PRIMARY KEY," +
                "  name VARCHAR(128)," +
                "  event_date DATETIME," +
                "  change_moment DATETIME, " +
                "  location VARCHAR(128)" +
                ")");
        db.execSQL("CREATE TABLE category ( " +
                "  id VARCHAR(20), " +
                "  name VARCHAR(128), " +
                "  gender CHAR(1) " +
                "); ");
        db.execSQL("CREATE TABLE boat_type ( " +
                "  boat_id CHAR(10) NOT NULL PRIMARY KEY, " +
                "  name VARCHAR(128) " +
                "); ");
        db.execSQL("CREATE TABLE race ( " +
                "  event_id VARCHAR(128), " +
                "  seqno INTEGER, " +
                "  hour TIMESTAMP, " +
                "  boat_type CHAR(10), " +
                "  category VARCHAR(20), " +
                "  start_time TIMESTAMP, " +
                "  PRIMARY KEY (event_id,seqno) " +
                "); ");
        db.execSQL( "CREATE TABLE competitor ( " +
                "   id VARCHAR(10) NOT NULL PRIMARY KEY, " +
                "   name VARCHAR(128), " +
                "   acronym VARCHAR(10) " +
                "); ");
        db.execSQL("CREATE TABLE person ( " +
                "  id VARCHAR(20) NOT NULL PRIMARY KEY, " +
                "  name VARCHAR(128) NOT NULL " +
                "); ");
        db.execSQL( "CREATE TABLE crew ( " +
                "  id VARCHAR(20) NOT NULL PRIMARY KEY, " +
                "  competitor_id VARCHAR(10), " +
                "  description VARCHAR(128) " +
                "); ");
        db.execSQL("CREATE TABLE crew_member ( " +
                "  crew_id VARCHAR(10) NOT NULL, " +
                "  position INTEGER, " +
                "  person_id VARCHAR(20) NOT NULL, " +
                "  PRIMARY KEY (crew_id,person_id) " +
                "); ");
        db.execSQL("CREATE TABLE alignment ( " +
                "  event_id VARCHAR(128), " +
                "  race_no INTEGER, " +
                "  lane INTEGER, " +
                "  crew VARCHAR(20), " +
                "  end_time TIMESTAMP " +
                "); ");
        db.execSQL("CREATE TABLE login ( " +
                "  username VARCHAR(128) PRIMARY KEY, " +
                "  logged INTEGER, " +
                "  password VARCHAR(128) " +
                "); ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS rowing_event");
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS race");
        db.execSQL("DROP TABLE IF EXISTS alignment");
        db.execSQL("DROP TABLE IF EXISTS competitor");
        db.execSQL("DROP TABLE IF EXISTS person");
        db.execSQL("DROP TABLE IF EXISTS crew");
        db.execSQL("DROP TABLE IF EXISTS crew_member");
        db.execSQL("DROP TABLE IF EXISTS alignment");
        db.execSQL("DROP TABLE IF EXISTS boat_type");
        db.execSQL("DROP TABLE IF EXISTS login");
        onCreate(db);
    }

}
