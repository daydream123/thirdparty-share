package com.feizhang.share.shareto.qq;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Thumbnail;
import com.feizhang.share.sharecontent.AudioUrl;
import com.feizhang.share.sharecontent.ImagePath;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.WebUrl;
import com.feizhang.share.shareto.ShareTo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;

import java.util.ArrayList;

public class QZone extends ShareTo {
    public static final int ID = 4;

    public QZone(ShareContent shareContent) {
        super(shareContent);
    }

    public QZone(){
        super();
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_qzone;
    }

    @Override
    public int getShareName() {
        return R.string.share_qzone;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public String getPackageName(Context context) {
        return "com.tencent.mobileqq";
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
        if (!isInstalled(context)) {
            Toast.makeText(context, R.string.share_qq_not_installed_warning, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mShareContent.validate(context)) {
            return;
        }

        if (mShareContent instanceof AudioUrl){
            AudioUrl audioUrl = (AudioUrl) mShareContent;
            Bundle bundle = new Bundle();
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, audioUrl.getTitle());
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, audioUrl.getSummary());
            bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
            bundle.putString(QzoneShare.SHARE_TO_QQ_AUDIO_URL, audioUrl.getAudioUrl());

            Thumbnail thumbnail = audioUrl.getThumbnail();
            if (thumbnail != null) {
                ArrayList<String> list = new ArrayList<>();
                if (thumbnail.getType() == Thumbnail.ThumbnailType.URL) {
                    list.add(thumbnail.getUrl());
                } else if (thumbnail.getType() == Thumbnail.ThumbnailType.LOCAL_PATH) {
                    list.add(thumbnail.getLocalPath());
                }
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
            }
            QQShareActivity.startActivity(context, bundle, QZone.ID);
            return;
        }

        if (mShareContent instanceof ImagePath){
            ImagePath imagePath = (ImagePath) mShareContent;
            Bundle bundle = new Bundle();
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, imagePath.getTitle());
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, imagePath.getSummary());
            bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
            bundle.putString(QzoneShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath.getImagePath());
            QQShareActivity.startActivity(context, bundle, QZone.ID);
            return;
        }

        if (mShareContent instanceof ImageUrl){
            ImageUrl imageUrl = (ImageUrl) mShareContent;
            Bundle bundle = new Bundle();
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, imageUrl.getTitle());
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, imageUrl.getSummary());
            bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
            bundle.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrl.getImageUrl());
            QQShareActivity.startActivity(context, bundle, QZone.ID);
            return;
        }

        if (mShareContent instanceof WebUrl){
            WebUrl webUrl = (WebUrl) mShareContent;
            Bundle bundle = new Bundle();
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, webUrl.getTitle());
            bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, getAppName(context).toString());
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, webUrl.getSummary());
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webUrl.getWebUrl());
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl.getWebUrl());

            Thumbnail thumbnail = webUrl.getThumbnail();
            if (thumbnail != null) {
                ArrayList<String> list = new ArrayList<>();
                if (thumbnail.getType() == Thumbnail.ThumbnailType.URL) {
                    list.add(thumbnail.getUrl());
                } else if (thumbnail.getType() == Thumbnail.ThumbnailType.LOCAL_PATH) {
                    list.add(thumbnail.getLocalPath());
                }
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
            }

            QQShareActivity.startActivity(context, bundle, QZone.ID);
        }
    }
}
