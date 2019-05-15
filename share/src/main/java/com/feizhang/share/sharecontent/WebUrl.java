package com.feizhang.share.sharecontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Thumbnail;

import java.io.Serializable;

public class WebUrl extends ShareContent implements Serializable {
    private final String webUrl;
    private final String title;
    private String summary;
    private Thumbnail thumbnail;

    public WebUrl(@NonNull String webUrl, @NonNull String title) {
        this.webUrl = webUrl;
        this.title = title;
    }

    public void setSummary(String summary){
        this.summary = summary;
    }

    public void setThumbnail(Thumbnail thumbnail){
        this.thumbnail = thumbnail;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean validate(Context context) {
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(context, R.string.share_url_no_title, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(webUrl)){
            Toast.makeText(context, R.string.share_url_no_url , Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
