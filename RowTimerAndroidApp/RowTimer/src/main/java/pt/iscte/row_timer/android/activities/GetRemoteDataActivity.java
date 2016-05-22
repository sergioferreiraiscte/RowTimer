package pt.iscte.row_timer.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


/**
 * Get remote information from server and store it in databases
 */
public class GetRemoteDataActivity extends AppCompatActivity {
    private static final String TAG = "GetRemoteDataActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_remote_data);
        setTitle(R.string.title_activity_get_remote_data);
    }

    /**
     * Connect to URL and get JSON
     */
    public void getRemoteData(View view) {
        Log.d(TAG,"getRemoteData()");

        new GetRemoteDataJob(this, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Object o) {
                // TODO : Sore data
                Log.d(TAG,"Callback Executed");
            }
        }).execute();
    }

}
