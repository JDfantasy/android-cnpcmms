package com.cnpc.zhibo.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//ÍøÂçÅÐ¶Ï
public class NetworkJudge {

	public static boolean isNetworkConnected(Context context){ 
		ConnectivityManager cm = (ConnectivityManager) 
				context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo(); 
		return info != null && info.isConnected();
	}
	
}
