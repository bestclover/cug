package com.example.cugnet;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.widget.Toast;

public class NetReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		try{
		/*State wifiState = null;
		State mobileState = null;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
*/
		/*
		 * if (wifiState != null && mobileState != null && State.CONNECTED !=
		 * wifiState && State.CONNECTED == mobileState) { // 手机网络连接成功 } else if
		 * (wifiState != null && mobileState != null && State.CONNECTED !=
		 * wifiState && State.CONNECTED != mobileState) { // 手机没有任何的网络
		 */
/*
		if (wifiState != null && State.CONNECTED == wifiState) {

			// 无线网络连接成功
			String ip = new getIP().getWIFILocalIpAdress(context);

			Pattern pattern2 = Pattern.compile("\\d+");
			Matcher matcher2 = pattern2.matcher(ip);
			ArrayList<String> IP = new ArrayList<String>();
			while (matcher2.find()) {
				IP.add(matcher2.group(0));
			}
			SharedPreferences sp;
			sp = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
			String username = sp.getString("USER_NAME", "");
			String password = sp.getString("PASSWORD", "");
			int select = sp.getInt("SELECT", 0);
			String ip_own=sp.getString(ip, "");
			
		

			Pattern pattern3 = Pattern.compile("\\d+");
			Matcher matcher3 = pattern3.matcher(ip_own);
			ArrayList<String> IP2 = new ArrayList<String>();
			while (matcher3.find()) {
				IP2.add(matcher3.group(0));
			}
			if ((IP.get(0).equals("172") && IP.get(1).equals("31") && username.length() != 0)||( IP.get(0).equals( IP2.get(0) )&& IP.get(1).equals( IP2.get(1) ) ) ){
				Intent intent2 = new Intent(context, NetService.class);

				intent2.putExtra("password", password);
				intent2.putExtra("select", select);
				intent2.putExtra("username", username);
				intent2.putExtra("ip", ip);

				context.startService(intent2);
			}
		}
		*/
		
			
			if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {  
	            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);  
	            if (info.getState().equals(NetworkInfo.State.CONNECTED)) {  
	       //      	<action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
	           
	                String ip = new getIP().getWIFILocalIpAdress(context);

	    			Pattern pattern2 = Pattern.compile("\\d+");
	    			Matcher matcher2 = pattern2.matcher(ip);
	    			ArrayList<String> IP = new ArrayList<String>();
	    			while (matcher2.find()) {
	    				IP.add(matcher2.group(0));
	    			}
	    			SharedPreferences sp;
	    			sp = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
	    			String username = sp.getString("USER_NAME", "");
	    			String password = sp.getString("PASSWORD", "");
	    			int select = sp.getInt("SELECT", 0);
	    			String ip_own=sp.getString(ip, "");
	    			
	    		

	    			Pattern pattern3 = Pattern.compile("\\d+");
	    			Matcher matcher3 = pattern3.matcher(ip_own);
	    			ArrayList<String> IP2 = new ArrayList<String>();
	    			while (matcher3.find()) {
	    				IP2.add(matcher3.group(0));
	    			}
	    			if ((IP.get(0).equals("172") && IP.get(1).equals("31") && username.length() != 0)||( IP.get(0).equals( IP2.get(0) )&& IP.get(1).equals( IP2.get(1) ) ) ){
	    				Intent intent2 = new Intent(context, NetService.class);

	    				intent2.putExtra("password", password);
	    				intent2.putExtra("select", select);
	    				intent2.putExtra("username", username);
	    				intent2.putExtra("ip", ip);

	    				context.startService(intent2);
	    			}
	            }  
	        }  
	
	}catch (Exception e){
		
	}
	}
}
