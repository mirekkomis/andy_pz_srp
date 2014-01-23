package pl.projektzespolowy.srp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class Settings {

	private static Context appContext;
	private static SharedPreferences prefs;

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
	
	public static SharedPreferences getPrefs(Context context) {
		if (appContext == null)
			appContext = context;
		return prefs == null ? (prefs = PreferenceManager.getDefaultSharedPreferences(context)) : prefs;
	}
}
