package com.feizhang.share.sample;

import android.app.Application;

import com.feizhang.share.ShareConfig;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ShareConfig.setWetChatAppId("wx3909318230292e7d");
        ShareConfig.setQQAppId("1104718080");
    }
}
