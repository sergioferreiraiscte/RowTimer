package pt.iscte.row_timer.android;

/**
 * Created by sergio on 21-05-2016.
 */
public class RowTimerException extends Exception {
    public RowTimerException(Exception e) {
        super(e);
    }

    public RowTimerException(String message) {
        super(message);
    }
}
