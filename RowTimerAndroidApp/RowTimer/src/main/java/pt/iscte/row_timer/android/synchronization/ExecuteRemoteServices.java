package pt.iscte.row_timer.android.synchronization;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import pt.iscte.row_timer.android.RowTimerException;
import pt.iscte.row_timer.android.database.RowingEventsDataSource;
import pt.iscte.row_timer.android.model.RowingEvent;

/**
 * Eexecute remote services
 */
public class ExecuteRemoteServices {
    private static final String TAG = "ExecuteRemoteServices";
    // TODO : The IP should be stored somewhere else
    private static final String SERVICE_URL = "http://192.168.1.91:8080/RowTimerBackOffice/api";

    /**
     * Execute any remote REST service.
     * TODO : Generalize to be able to use POST
     * @param apiURLRightSide
     * @param classType
     * @return
     * @throws RowTimerException
     */
    private String executeRESTService(String apiURLRightSide) throws RowTimerException {
        Log.d(TAG, "executeRESTService()");
        StringBuffer receivedStr = new StringBuffer();
        HttpURLConnection urlConnection = null;
        try {
            StringBuffer strURL = new StringBuffer();
            strURL.append(SERVICE_URL).append(apiURLRightSide);
            URL url = new URL(strURL.toString());
            urlConnection = (HttpURLConnection) url.openConnection();


            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            int data = in.read();
            while (data != -1) {
                char current = (char) data;
                data = in.read();
                receivedStr.append(current);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception ocurred : " + e.getMessage());
            throw new RowTimerException(e);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        Log.d(TAG, "Received : " + receivedStr.toString());

        return receivedStr.toString();
    }


    /**
     * Get the information of an event
     * @param eventId
     * @return
     */
    public RowingEvent getEventData(String eventId) throws RowTimerException {
        Log.d(TAG, "getEventData()");
        StringBuffer strURL = new StringBuffer();
        strURL.append("/event/").append(eventId);
        String jsonString =  executeRESTService(strURL.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        RowingEvent  rowingEvent = null;
        try {
            rowingEvent = objectMapper.readValue(jsonString.getBytes(), RowingEvent.class);
        } catch (IOException e) {
            throw new RowTimerException(e);
        }
        return rowingEvent;
    }

    /**
     * Get the list of events
     * TODO : Should get event list only after last pull
     * @return
     * @throws RowTimerException
     */
    public List<RowingEvent> getEventList() throws RowTimerException {
        Log.d(TAG, "getEventList()");
        StringBuffer strURL = new StringBuffer();
        strURL.append("/event");
        String jsonString =  executeRESTService(strURL.toString());
        List<RowingEvent> rowingEvents = null;
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, RowingEvent.class);
        try {
            rowingEvents  = objectMapper.readValue(jsonString.getBytes(), collectionType);
        } catch (IOException e) {
            throw new RowTimerException(e);
        }
        return rowingEvents;
    }
}
