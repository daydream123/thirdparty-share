package com.feizhang.share;

import android.text.TextUtils;

/**
 * Third-Party app id configure, it should be configured in your application.
 */
public class ShareConfig {
    private String weChatAppId;
    private String qqAppId;

    private static ShareConfig instance;

    private static ShareConfig getInstance(){
        if(instance == null) {
            synchronized (ShareConfig.class){
                instance = new ShareConfig();
            }
        }

        return instance;
    }

    public static String getWeChatAppId(){
        String appId = getInstance().weChatAppId;
        if (TextUtils.isEmpty(appId)){
            throw new IllegalArgumentException("No app id found for WeChat, please config it in Application with ShareConfig");
        }
        return appId;
    }

    public static void setWetChatAppId(String appId){
        getInstance().weChatAppId = appId;
    }

    public static String getQQAppId(){
        String appId = getInstance().qqAppId;
        if (TextUtils.isEmpty(appId)){
            throw new IllegalArgumentException("No app id found for QQ, please config it in Application with ShareConfig");
        }

        return appId;
    }

    public static void setQQAppId(String appId){
        getInstance().qqAppId = appId;
    }
}
