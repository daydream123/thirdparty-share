package com.feizhang.share.sharecontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.feizhang.share.Thumbnail;
import com.feizhang.share.shareto.ShareTo;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;

import java.io.Serializable;

/**
 * Created by zhangfei on 2017/10/6.
 */

public class VideoUrl extends ShareContent implements Serializable {
    private final String videoUrl;
    private String title;
    private String summary;
    private Thumbnail thumbnail;

    public VideoUrl(@NonNull String videoUrl){
        this.videoUrl = videoUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean validate(Context context) {
        return !TextUtils.isEmpty(videoUrl);
    }

    @Override
    public void qqShare(Context context, ShareTo shareTo) {
        // not support
    }

    @Override
    public void qzoneShare(Context context, ShareTo shareTo) {
        // not support
    }

    @Override
    public void wechatShare(Context context, ShareTo shareTo) {
        WXVideoObject object = new WXVideoObject();
        object.videoUrl = videoUrl;
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneSession,
                object, title, summary, thumbnail);
    }

    @Override
    public void timelineShare(Context context, ShareTo shareTo) {
        WXVideoObject object = new WXVideoObject();
        object.videoUrl = videoUrl;
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneTimeline,
                object, title, summary, thumbnail);
    }

    @Override
    public void smsShare(Context context, ShareTo shareTo) {
        Text.sendSms(context, videoUrl);
    }
}
