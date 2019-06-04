package com.feizhang.share.sample;

import android.app.Application;

import com.feizhang.share.ShareConfig;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ShareConfig.setWeChatAppId("wx111111111");
        ShareConfig.setQQAppId("222222222");
        ShareConfig.setLineAppId("33333333");
        ShareConfig.setFacebookAppId("444444444");
    }
}
