package com.feizhang.share;

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
        return getInstance().weChatAppId;
    }

    public static void setWetChatAppId(String appId){
        getInstance().weChatAppId = appId;
    }

    public static String getQQAppId(){
        return getInstance().qqAppId;
    }

    public static void setQQAppId(String appId){
        getInstance().qqAppId = appId;
    }
}
