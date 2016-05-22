package pt.iscte.row_timer.android.synchronization;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility methods to handle connections
 */
public class ConnectionUtil {

    /**
     * Check whether the device is connected, and if so, whether the connection
     * is wifi or mobile (it could be something else).
     */
    public static Boolean haveNetworkConnection(Context context) {
        boolean wifiConnected = false;
        boolean mobileConnected = false;

        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();

        if (activeInfo != null && activeInfo.isConnected()) {
            if ( activeInfo.getType() == ConnectivityManager.TYPE_WIFI)
                wifiConnected = true;

            if ( activeInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            mobileConnected = true;

            if(wifiConnected || mobileConnected) {
                return true;
            }
        }
        return false;
    }
}
