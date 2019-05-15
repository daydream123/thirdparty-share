package com.feizhang.share.sharecontent;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Thumbnail;

public class MiniProgram extends ShareContent {
    private final Thumbnail thumbnail;
    private final String webPageUrl;
    private final String userName;
    private final String path;
    private int programType;
    private String title;
    private String summary;

    public MiniProgram(String webPageUrl, String userName,
                       String path, Thumbnail thumbnail) {
        this.webPageUrl = webPageUrl;
        this.userName = userName;
        this.path = path;
        this.thumbnail = thumbnail;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public String getWebPageUrl() {
        return webPageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPath() {
        return path;
    }

    public String getSummary() {
        return summary;
    }

    public void setProgramType(int programType) {
        this.programType = programType;
    }

    public int getProgramType() {
        return programType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean validate(Context context) {
        if (thumbnail == null) {
            Toast.makeText(context, R.string.share_mini_program_no_thumbnail, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(webPageUrl)){
            Toast.makeText(context, R.string.share_mini_program_no_webPageUrl, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(context, R.string.share_mini_program_no_userName, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(path)){
            Toast.makeText(context, R.string.share_mini_program_no_path, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
