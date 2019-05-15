package com.feizhang.share.sharecontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;

import java.io.Serializable;

public class ImageUrl extends ShareContent implements Serializable {
    private final String imageUrl;
    private String title;
    private String summary;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public ImageUrl(@NonNull String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean validate(Context context) {
        if(TextUtils.isEmpty(imageUrl)){
            Toast.makeText(context, R.string.share_image_no_url, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
