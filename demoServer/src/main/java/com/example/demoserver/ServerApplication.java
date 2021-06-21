package com.example.demoserver;

import android.app.Application;

import com.margin.base.SnapConfig;

/**
 * Created by : mr.lu
 * Created at : 2021/6/20 at 01:06
 * Description:
 */
public class ServerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SnapConfig.LOG_PREFIX = "Server_";
    }
}
