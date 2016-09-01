package com.example.android.movieproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by lavanya on 9/1/16.
 */
public class NetworkchangeReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean connected = false;
		boolean iswifi = false;
		ConnectivityManager cm =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null) {
			iswifi = netInfo.getType() == ConnectivityManager.TYPE_WIFI;
			connected = (netInfo.isAvailable() && netInfo.isConnected());
		}

		if (connected && iswifi) {
			Toast.makeText(context, "internet connection available", Toast.LENGTH_SHORT).show();
			Intent intentnet = new Intent(context, MainActivity.class);
			intentnet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			context.startActivity(intentnet);
		} else {
			Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
		}
	}
}
