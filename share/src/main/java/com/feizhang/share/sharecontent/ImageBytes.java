package com.feizhang.share.sharecontent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Share;
import com.feizhang.share.Thumbnail;
import com.feizhang.share.shareto.ShareTo;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;

import java.io.Serializable;

public class ImageBytes extends ShareContent implements Serializable {
    private byte[] bytes;
    private String title;
    private String summary;

    public ImageBytes(byte[] bytes){
        this.bytes = bytes;
    }

    public ImageBytes(Bitmap bitmap){
        this.bytes = Share.toBytes(bitmap);
    }

    public ImageBytes(Drawable drawable){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        this.bytes = Share.toBytes(bitmapDrawable.getBitmap());
    }

    public ImageBytes(Context context, @DrawableRes int resId){
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        this.bytes = Share.toBytes(bitmapDrawable.getBitmap());
    }

    public ImageBytes(String base64){
        base64 = base64.replace("data:image/png;base64,", "");
        this.bytes = Base64.decode(base64, Base64.DEFAULT);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean validate(Context context) {
        return bytes != null && bytes.length > 0;
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
        String filePath = saveAsFile(context, bytes);
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
                new Thumbnail(Share.scaleToLimitedSize(bytes, WXMediaMessage.THUMB_LENGTH_LIMIT)));
    }

    @Override
    public void timelineShare(Context context, ShareTo shareTo) {
        String filePath = saveAsFile(context, bytes);
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
                new Thumbnail(Share.scaleToLimitedSize(bytes, WXMediaMessage.THUMB_LENGTH_LIMIT)));
    }

    @Override
    public void smsShare(Context context, ShareTo shareTo) {
        // not support
    }
}
