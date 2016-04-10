package com.example.cugnet;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class getIP {
public static String getWIFILocalIpAdress(Context mContext) {  
        
        //获取wifi服务  
        WifiManager wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        //wifiManager.setWifiEnabled(true); 
        	return "000.000.00.00";
        }  
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();      
        int ipAddress = wifiInfo.getIpAddress();  
        String ip = formatIpAddress(ipAddress);  
        return ip;  
    }    
    private static String formatIpAddress(int ipAdress) {      
        
         return (ipAdress & 0xFF ) + "." +      
        ((ipAdress >> 8 ) & 0xFF) + "." +      
        ((ipAdress >> 16 ) & 0xFF) + "." +      
        ( ipAdress >> 24 & 0xFF) ;  
     }  
}
