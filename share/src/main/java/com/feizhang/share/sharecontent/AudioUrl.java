package com.feizhang.share.sharecontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Thumbnail;

import java.io.Serializable;

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

    public String getAudioUrl() {
        return audioUrl;
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
        if (TextUtils.isEmpty(audioUrl)) {
            Toast.makeText(context, R.string.share_audio_no_url, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
