package com.feizhang.share.sharecontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.feizhang.share.Thumbnail;
import com.feizhang.share.shareto.ShareTo;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXVideoFileObject;

import java.io.Serializable;

/**
 * Created by zhangfei on 2017/10/6.
 */

public class VideoPath extends ShareContent implements Serializable {
    private final String videoPath;
    private String title;
    private String summary;
    private Thumbnail thumbnail;

    public VideoPath(@NonNull String videoPath) {
        this.videoPath = videoPath;
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
        return !TextUtils.isEmpty(videoPath);
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
        WXVideoFileObject object = new WXVideoFileObject();
        object.filePath = videoPath;
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneSession,
                object, title, summary, thumbnail);
    }

    @Override
    public void timelineShare(Context context, ShareTo shareTo) {
        WXVideoFileObject object = new WXVideoFileObject();
        object.filePath = videoPath;
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneTimeline,
                object, title, summary, thumbnail);
    }

    @Override
    public void smsShare(Context context, ShareTo shareTo) {
        // not support
    }
}
