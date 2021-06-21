package com.example.binderdemo;

import android.app.Application;

import com.margin.base.SnapConfig;

/**
 * Created by : mr.lu
 * Created at : 2021/6/20 at 01:07
 * Description:
 */
public class ClinentApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SnapConfig.LOG_PREFIX = "Client_";
    }
}
