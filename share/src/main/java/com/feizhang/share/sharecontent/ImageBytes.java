package com.feizhang.share.sharecontent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Share;

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

    public byte[] getBytes() {
        return bytes;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public ImageBytes(Drawable drawable){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        this.bytes = Share.toBytes(bitmapDrawable.getBitmap());
    }

    public ImageBytes(Context context, @DrawableRes int resId){
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        if (bitmapDrawable != null) {
            this.bytes = Share.toBytes(bitmapDrawable.getBitmap());
        }
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
        if (bytes == null || bytes.length == 0) {
            Toast.makeText(context, R.string.share_image_no_bytes, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
