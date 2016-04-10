package com.example.cugnet;

import android.widget.Toast;

public class ToastOwn {
	 public static void Long(String str){  
	        Toast.makeText(MyApplication.getInstance(), str, Toast.LENGTH_LONG).show();  
	    }  
	      
	    public static void Short(String str){  
	        Toast.makeText(MyApplication.getInstance(), str, Toast.LENGTH_SHORT).show();  
	    }  
}
