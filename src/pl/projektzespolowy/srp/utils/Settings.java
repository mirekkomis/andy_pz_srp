package pl.projektzespolowy.srp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Settings {

	/**
	 * Checks if Device is connected to Internet
	 * 
	 * @param context
	 *            - Your Application Context
	 * @return true if device is online, false otherwise.
	 * @author <a href="mailto:m.depta@examobile.eu">m.depta@examobile.eu</a>
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}
}
