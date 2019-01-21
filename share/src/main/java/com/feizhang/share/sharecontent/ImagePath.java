package com.feizhang.share.sharecontent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.QQShareActivity;
import com.feizhang.share.R;
import com.feizhang.share.Thumbnail;
import com.feizhang.share.shareto.QQ;
import com.feizhang.share.shareto.QZone;
import com.feizhang.share.shareto.ShareTo;
import com.feizhang.validation.Validator;
import com.feizhang.validation.annotations.NotBlank;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;

import java.io.Serializable;

/**
 * Created by zhangfei on 2017/10/6.
 */
public class ImagePath extends ShareContent implements Serializable {
    @NotBlank
    private final String imagePath;
    private String title;
    private String summary;

    public ImagePath(@NonNull String imagePath) {
        this.imagePath = imagePath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean validate(Context context) {
        return Validator.validate(context, this);
    }

    @Override
    public void qqShare(Context context, ShareTo shareTo) {
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
        QQShareActivity.startActivity(context, bundle, shareTo.getAppId(context), QQ.ID);
    }

    @Override
    public void qzoneShare(Context context, ShareTo shareTo) {
        Bundle bundle = new Bundle();
        bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
        bundle.putString(QzoneShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
        QQShareActivity.startActivity(context, bundle, shareTo.getAppId(context), QZone.ID);
    }

    @Override
    public void wechatShare(Context context, ShareTo shareTo) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        String filePath = saveAsFile(context, bitmap);
        if (TextUtils.isEmpty(filePath)){
            Toast.makeText(context, R.string.share_save_file_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        WXImageObject object = new WXImageObject();
        object.setImagePath(filePath);
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneSession,
                object, title, summary,
                new Thumbnail(Thumbnail.ThumbnailType.LOCAL_PATH, imagePath));
    }

    @Override
    public void timelineShare(Context context, ShareTo shareTo) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        String filePath = saveAsFile(context, bitmap);
        if (TextUtils.isEmpty(filePath)){
            Toast.makeText(context, R.string.share_save_file_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        WXImageObject object = new WXImageObject();
        object.setImagePath(filePath);
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneTimeline,
                object, title, summary,
                new Thumbnail(Thumbnail.ThumbnailType.LOCAL_PATH, imagePath));
    }

    @Override
    public void smsShare(Context context, ShareTo shareTo) {
        // not support
    }
}
