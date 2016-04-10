package com.example.cugnet;

import android.app.Application;

public class MyApplication extends Application{
	private static MyApplication application;  
    
    @Override  
    public void onCreate() {  
        super.onCreate();  
        application =this;  
    }  
  
      
    public static MyApplication getInstance() {  
        return application;  
    }  
}
