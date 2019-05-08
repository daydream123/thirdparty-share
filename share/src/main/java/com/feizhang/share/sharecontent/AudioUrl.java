package com.feizhang.share.sharecontent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.feizhang.share.QQShareActivity;
import com.feizhang.share.Thumbnail;
import com.feizhang.share.shareto.QQ;
import com.feizhang.share.shareto.QZone;
import com.feizhang.share.shareto.ShareTo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangfei on 2017/10/6.
 */
public class AudioUrl extends ShareContent implements Serializable{
    private final String audioUrl;
    private String title;
    private String summary;
    private Thumbnail thumbnail;

    public AudioUrl(@NonNull String audioUrl) {
        this.audioUrl = audioUrl;
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
        return !TextUtils.isEmpty(audioUrl);
    }

    @Override
    public void qqShare(Context context, ShareTo shareTo) {
        Bundle bundle = new Bundle();
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        bundle.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl);

        if (thumbnail != null) {
            if (!TextUtils.isEmpty(thumbnail.getUrl())) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumbnail.getUrl());
            } else if (!TextUtils.isEmpty(thumbnail.getLocalPath())) {
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, thumbnail.getLocalPath());
            }
        }

        QQShareActivity.startActivity(context, bundle, shareTo.getAppId(context), QQ.ID);
    }

    @Override
    public void qzoneShare(Context context, ShareTo shareTo) {
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
        bundle.putString(QzoneShare.SHARE_TO_QQ_AUDIO_URL, audioUrl);

        if (thumbnail != null) {
            ArrayList<String> list = new ArrayList<>();
            if (thumbnail.getType() == Thumbnail.ThumbnailType.URL) {
                list.add(thumbnail.getUrl());
            } else if (thumbnail.getType() == Thumbnail.ThumbnailType.LOCAL_PATH) {
                list.add(thumbnail.getLocalPath());
            }
            bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        }
        QQShareActivity.startActivity(context, bundle, shareTo.getAppId(context), QZone.ID);
    }

    @Override
    public void wechatShare(Context context, ShareTo shareTo) {
        WXMusicObject object = new WXMusicObject();
        object.musicUrl = audioUrl;
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneSession,
                object, title, summary, thumbnail);
    }

    @Override
    public void timelineShare(Context context, ShareTo shareTo) {
        WXMusicObject object = new WXMusicObject();
        object.musicUrl = audioUrl;
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneTimeline,
                object, title, summary, thumbnail);
    }

    @Override
    public void smsShare(Context context, ShareTo shareTo) {
        Text.sendSms(context, audioUrl);
    }
}
