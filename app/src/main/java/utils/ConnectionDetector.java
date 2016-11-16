package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

public class ConnectionDetector {

	public ConnectionDetector() {
	}

	/** checks if net connection is available */
	public static boolean isConnectingToInternet(Context _c) {
		ConnectivityManager connectivtiy = (ConnectivityManager) _c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivtiy != null) {
			NetworkInfo[] info = connectivtiy.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}


	//This function is used for check user has internet or not
    public static boolean hasInternetAvailable() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}