package com.feizhang.share.sharecontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Thumbnail;

import java.io.Serializable;

public class VideoUrl extends ShareContent implements Serializable {
    private final String videoUrl;
    private String title;
    private String summary;
    private Thumbnail thumbnail;

    public VideoUrl(@NonNull String videoUrl) {
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

    public String getVideoUrl() {
        return videoUrl;
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
        if (TextUtils.isEmpty(videoUrl)) {
            Toast.makeText(context, R.string.share_video_no_url, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
