package pt.iscte.row_timer.android.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pt.iscte.row_timer.android.model.RowingEvent;
import pt.iscte.row_timer.android.database.RowingEventsDataSource;

/**
 * Created by sergio on 18-05-2016.
 */
public class GetRemoteDataJob extends AsyncTask <String, Void, Boolean>  {
    private static final String TAG = "GetRemoteDataJob";
    private Context context;

    public GetRemoteDataJob(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.d(TAG,"doInBackground()");
        StringBuffer receivedStr = new StringBuffer();
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL("http://192.168.1.91:8080/RowTimerBackOffice/api/event/CNV");
            urlConnection = (HttpURLConnection) url.openConnection();


            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            int data = in.read();
            while (data != -1) {
                char current = (char) data;
                data = in.read();
                receivedStr.append(current);
                // System.out.print(current);
            }
        } catch (Exception e) {
            Log.d(TAG,"Exception ocurred : " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            if ( urlConnection != null )
                urlConnection.disconnect();
        }
        Log.d(TAG,"Received : " + receivedStr.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        RowingEvent rowingEvent = null;
        try {
            rowingEvent = objectMapper.readValue(receivedStr.toString().getBytes(), RowingEvent.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"POJO after deserializing : " + rowingEvent.getId() + " ; " + rowingEvent.getName());

        // Save in Database
        RowingEventsDataSource ds = new RowingEventsDataSource(context);
        ds.open();
        ds.delete(rowingEvent.getId());
        ds.createRowingEvent(rowingEvent);
        RowingEvent dbRowingEvent = ds.readRowingEvent("CNV");

        //return receivedStr.toString();
        return true;

    }
}
