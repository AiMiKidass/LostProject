package com.example.alex.newtestproject;

import android.app.Application;

import com.example.alex.newtestproject.application.CrashHandler;

/**
 * Created by Administrator on 2017/6/8.
 * 全局Application
 */
public class App extends Application {

    private static App INSTANCE;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
