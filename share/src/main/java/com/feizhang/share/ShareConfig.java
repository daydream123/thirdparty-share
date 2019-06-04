package com.feizhang.share;

import android.text.TextUtils;

import com.facebook.FacebookSdk;

/**
 * Third-Party app id configure, it should be configured in your application.
 */
public class ShareConfig {
    private String weChatAppId;
    private String qqAppId;
    private String lineAppId;
    private String facebookAppId;

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
            throw new IllegalArgumentException("No app id found for WeChat, " +
                    "please config it in Application with ShareConfig");
        }
        return appId;
    }

    public static void setWeChatAppId(String appId){
        getInstance().weChatAppId = appId;
    }

    public static String getQQAppId(){
        String appId = getInstance().qqAppId;
        if (TextUtils.isEmpty(appId)){
            throw new IllegalArgumentException("No app id found for QQ, " +
                    "please config it in Application with ShareConfig");
        }

        return appId;
    }

    public static void setQQAppId(String appId){
        getInstance().qqAppId = appId;
    }

    public static String getLineAppId(){
        String appId = getInstance().lineAppId;
        if (TextUtils.isEmpty(appId)) {
            throw new IllegalArgumentException("No app id found for QQ, " +
                    "please config it in Application with ShareConfig");
        }

        return appId;
    }

    public static void setLineAppId(String appId){
        getInstance().lineAppId = appId;
    }

    public static String getFacebookAppId(){
        String appId = getInstance().facebookAppId;
        if (TextUtils.isEmpty(appId)) {
            throw new IllegalArgumentException("No app id found for Facebook, " +
                    "please config it in Application with ShareConfig");
        }

        return appId;
    }

    public static void setFacebookAppId(String appId){
        getInstance().facebookAppId = appId;
        FacebookSdk.setApplicationId(appId);
    }
}
