package com.feizhang.share.sharecontent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;

import java.io.Serializable;

public class ImagePath extends ShareContent implements Serializable {
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

    public String getImagePath() {
        return imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    @Override
    public boolean validate(Context context) {
        if (TextUtils.isEmpty(imagePath)){
            Toast.makeText(context, R.string.share_image_no_path, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
