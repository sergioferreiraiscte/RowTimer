package pt.iscte.row_timer.android;

import android.app.Application;

import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Contains the information loaded in the activities to be navigated and executed
 */
public class RowTimerApplication extends Application {
    private RowingEvent currentEvent;
    public String test;

    public RowingEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(RowingEvent currentEvent) {
        this.currentEvent = currentEvent;
    }
}
