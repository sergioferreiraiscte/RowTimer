package pt.iscte.row_timer.android.synchronization;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import pt.iscte.row_timer.android.RowTimerException;
import pt.iscte.row_timer.android.activities.OnTaskCompleted;
import pt.iscte.row_timer.android.database.RowingEventsDataSource;
import pt.iscte.row_timer.android.model.Login;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Created by sergio on 24-05-2016.
 */
public class LoginJob extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "LoginJob";
    private Context context;
    private OnTaskCompleted listener;
    private Login login;

    public LoginJob(Context context, Race race, OnTaskCompleted  listener, Login login) {
        this.context = context;
        this.listener = listener;
        this.login = login;
    }

    /**
     * TODO : Add check connectivity
     * @param params
     * @return
     */
    @Override
    protected Boolean doInBackground(String... params) {
        Log.d(TAG,"doInBackground()");
        ExecuteRemoteServices exec = new ExecuteRemoteServices();
        try {
            login = exec.remoteLogin(login.getUsername(),login.getPassword());
        } catch (RowTimerException e) {
            e.printStackTrace();
        }
        RowingEventsDataSource database = new RowingEventsDataSource(context);
        database.storeLoginInfo(login);
        return true;
    }

    /**
     * Exceuted automatically when the task is finished
     */
    @Override
    protected void onPostExecute(Boolean result) {
        Log.d(TAG,"onPostExecute()");
        listener.onTaskCompleted(login);
    }

}
