package pt.iscte.row_timer.android.synchronization;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import pt.iscte.row_timer.android.RowTimerException;
import pt.iscte.row_timer.android.database.RowingEventsDataSource;
import pt.iscte.row_timer.android.model.Login;
import pt.iscte.row_timer.android.model.Race;
import pt.iscte.row_timer.android.model.Result;
import pt.iscte.row_timer.android.model.RowingEvent;
import pt.iscte.row_timer.android.model.StartRace;

/**
 * Eexecute remote services
 */
public class ExecuteRemoteServices {
    private static final String TAG = "ExecuteRemoteServices";
    // TODO : The IP should be stored somewhere else
    // Work IP
    //private static final String IP = "192.168.1.37";
    // Home IP
    private static final String IP = "164.132.108.42";
    private static final String SERVICE_URL = "http://" + IP + ":8181/RowTimerBackOffice/api";

    /**
     * Execute any remote REST service.
     * TODO : Generalize to be able to use POST
     *
     * @param apiURLRightSide
     * @param apiURLRightSide
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
        } catch (FileNotFoundException fne) {
            // 404 - No event after pulled time
            return null;
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
     * Execute any remote REST service.
     * TODO : Generalize to be able to use POST
     *
     * @param apiURLRightSide
     * @param apiURLRightSide
     * @return
     * @throws RowTimerException
     */
    private String executeGET(String apiURLRightSide, String jsonStr) throws RowTimerException {
        Log.d(TAG, "executeRESTService()");
        StringBuffer receivedStr = new StringBuffer();
        HttpURLConnection urlConnection = null;
        try {
            StringBuffer strURL = new StringBuffer();
            strURL.append(SERVICE_URL).append(apiURLRightSide);
            URL url = new URL(strURL.toString());
            urlConnection = (HttpURLConnection) url.openConnection();

            // Send data in the body
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(jsonStr);
            writer.flush();
            writer.close();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            int data = in.read();
            while (data != -1) {
                char current = (char) data;
                data = in.read();
                receivedStr.append(current);
            }
        } catch (FileNotFoundException fne) {
            // 404 - No event after pulled time
            return null;
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
     * Execute a POST REST service that receives the JSON information in the body
     *
     * @return
     */
    private void executePOST(String apiURL, String jsonStr) {
        HttpURLConnection urlConnection = null;
        try {
            StringBuffer strURL = new StringBuffer();
            strURL.append(SERVICE_URL).append(apiURL);
            URL url = new URL(strURL.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestMethod("POST");

            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(jsonStr);
            writer.flush();
            writer.close();

        } catch (Exception e) {

        } finally {
            urlConnection.disconnect();
        }
    }


    /**
     * Get the information of an event.
     * It only get it if the information on server was changed
     *
     * @param eventId
     * @return
     */
    public RowingEvent getEventData(String eventId, Date lastPulldate) throws RowTimerException {
        Log.d(TAG, "getEventData()");
        StringBuffer strURL = new StringBuffer();
        strURL.append("/event/").append(eventId);
        // TODO : Forcing to allways get information
        //strURL.append("?pulled=").append(lastPulldate.getTime());
        String jsonString = executeRESTService(strURL.toString());
        if (jsonString == null )
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        RowingEvent rowingEvent = null;
        try {
            rowingEvent = objectMapper.readValue(jsonString.getBytes(), RowingEvent.class);
        } catch (IOException e) {
            throw new RowTimerException(e);
        }
        return rowingEvent;
    }

    /**
     * Get the list of events
     *
     * @return
     * @throws RowTimerException
     */
    public List<RowingEvent> getEventList() throws RowTimerException {
        Log.d(TAG, "getEventList()");
        StringBuffer strURL = new StringBuffer();
        strURL.append("/event");
        String jsonString = executeRESTService(strURL.toString());
        List<RowingEvent> rowingEvents = null;
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, RowingEvent.class);
        try {
            rowingEvents = objectMapper.readValue(jsonString.getBytes(), collectionType);
        } catch (IOException e) {
            throw new RowTimerException(e);
        }
        return rowingEvents;
    }

    /**
     * Send start times acumulated by start referee
     */
    public void sendStartTimes(String eventId, List<StartRace> startTimes) throws RowTimerException {
        Log.d(TAG, "sendStartTimes()");

        // Build JSON
        String jsonStr = null;
        ObjectMapper mapper = new ObjectMapper();
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, StartRace.class);
        try {
            jsonStr = mapper.writeValueAsString(startTimes);
        } catch (IOException e) {
            throw new RowTimerException(e);
        }

        StringBuffer strURL = new StringBuffer();
        strURL.append("/event/").append(eventId).append("/start/times");
        executePOST(strURL.toString(),jsonStr);
    }

    /**
     * Send results acumulated by arrival referee
     */
    public void sendResults(String eventId, List<Result> results) throws RowTimerException {
        Log.d(TAG, "sendStartTimes()");

        // Build JSON
        String jsonStr = null;
        ObjectMapper mapper = new ObjectMapper();
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, Result.class);
        try {
            jsonStr = mapper.writeValueAsString(results);
        } catch (IOException e) {
            throw new RowTimerException(e);
        }

        StringBuffer strURL = new StringBuffer();
        strURL.append("/event/").append(eventId).append("/arrival/times");
        executePOST(strURL.toString(),jsonStr);
    }

    /**
     * Send the movie related to a race to the server.
     *
     * TODO : Implement and test
     *
     * @param filename
     */
    public void sendMovie(Race race, String filename) {

    }

    /**
     * Execute the remote login
     *
     * @param username Name of the emote user
     * @param password encrypted pwssword to be compared
     * @return
     */
    public Login remoteLogin(String username, String password) throws RowTimerException {
        Log.d(TAG, "remoteLogin()");
        StringBuffer strURL = new StringBuffer();
        strURL.append("/login/").append(username);

        String jsonStr = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonStr = mapper.writeValueAsString(password);
        } catch (JsonProcessingException e) {
            throw new RowTimerException(e);
        }

        String jsonString = executeGET(strURL.toString(),jsonStr);
        if (jsonString == null )
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        Login login = null;
        try {
            login = objectMapper.readValue(jsonString.getBytes(), Login.class);
        } catch (IOException e) {
            throw new RowTimerException(e);
        }
        return login;
    }
}
