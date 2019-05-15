package com.feizhang.share.shareto;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.QQShareActivity;
import com.feizhang.share.R;
import com.feizhang.share.ShareConfig;
import com.feizhang.share.Thumbnail;
import com.feizhang.share.sharecontent.AudioUrl;
import com.feizhang.share.sharecontent.ImagePath;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.WebUrl;
import com.tencent.connect.share.QQShare;

import java.io.Serializable;

public class QQ extends ShareTo implements Serializable {
    public static final int ID = 3;

    public QQ(ShareContent shareContent) {
        super(shareContent);
    }

    public QQ(){
        super();
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_qq;
    }

    @Override
    public int getShareName() {
        return R.string.share_qq;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public boolean installed(Context context) {
        if (isAppNotInstalled(context, "com.tencent.mobileqq")) {
            Toast.makeText(context, R.string.share_qq_not_installed_warning, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public String getAppId(Context context) {
        return ShareConfig.getQQAppId();
    }

    @Override
    public boolean isSupportToShare() {
        return mShareContent instanceof AudioUrl
                || mShareContent instanceof ImagePath
                || mShareContent instanceof ImageUrl
                || mShareContent instanceof WebUrl;
    }

    @Override
    public void share(Context context) {
        if (!mShareContent.validate(context)) {
            return;
        }

        if (mShareContent instanceof AudioUrl){
            AudioUrl audioUrl = (AudioUrl) mShareContent;
            Bundle bundle = new Bundle();
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, audioUrl.getTitle());
            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, audioUrl.getSummary());
            bundle.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl.getAudioUrl());

            Thumbnail thumbnail = audioUrl.getThumbnail();
            if (thumbnail != null) {
                if (!TextUtils.isEmpty(thumbnail.getUrl())) {
                    bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumbnail.getUrl());
                } else if (!TextUtils.isEmpty(thumbnail.getLocalPath())) {
                    bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, thumbnail.getLocalPath());
                }
            }

            QQShareActivity.startActivity(context, bundle, QQ.ID);
            return;
        }

        if (mShareContent instanceof ImagePath){
            ImagePath imagePath = (ImagePath) mShareContent;
            Bundle bundle = new Bundle();
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, imagePath.getTitle());
            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, imagePath.getSummary());
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath.getImagePath());
            QQShareActivity.startActivity(context, bundle, QQ.ID);
            return;
        }

        if (mShareContent instanceof ImageUrl){
            ImageUrl imageUrl = (ImageUrl) mShareContent;
            Bundle bundle = new Bundle();
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, imageUrl.getTitle());
            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, imageUrl.getSummary());
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl.getImageUrl());
            QQShareActivity.startActivity(context, bundle, QQ.ID);
            return;
        }

        if (mShareContent instanceof WebUrl){
            WebUrl webUrl = (WebUrl) mShareContent;
            Bundle bundle = new Bundle();
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, webUrl.getTitle());
            bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
            bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, webUrl.getSummary());
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl.getWebUrl());

            Thumbnail thumbnail = webUrl.getThumbnail();
            if (thumbnail != null) {
                if (!TextUtils.isEmpty(thumbnail.getUrl())) {
                    bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumbnail.getUrl());
                } else if (!TextUtils.isEmpty(thumbnail.getLocalPath())) {
                    bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, thumbnail.getLocalPath());
                }
            }

            QQShareActivity.startActivity(context, bundle, QQ.ID);
        }
    }
}
