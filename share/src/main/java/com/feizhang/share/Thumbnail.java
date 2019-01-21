package com.feizhang.share;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import java.io.File;
import java.io.Serializable;

/**
 * Created by zf08526 on 2017/9/30.
 */

public class Thumbnail implements Serializable{
    private ThumbnailType type;
    private String url;
    private byte[] bytes;
    private String localPath;

    public ThumbnailType getType(){
        return type;
    }

    public Thumbnail(@NonNull ThumbnailType type, @NonNull String thumbnail) {
        if (type == ThumbnailType.URL) {
            if (URLUtil.isNetworkUrl(thumbnail)) {
                this.type = type;
                this.url = thumbnail;
            }
        } else if (type == ThumbnailType.LOCAL_PATH) {
            if(new File(thumbnail).exists()) {
                this.type = type;
                this.localPath = thumbnail;
            }
        }
    }

    public Thumbnail(@NonNull byte[] bytes) {
        if (bytes.length > 0) {
            this.type = ThumbnailType.BYTES;
            this.bytes = bytes;
        }
    }

    @Nullable
    public String getUrl(){
        if (type == ThumbnailType.URL && !TextUtils.isEmpty(url)) {
            return url;
        }

        return null;
    }

    public boolean isValid(){
        return (type == ThumbnailType.BYTES && bytes != null && bytes.length > 0)
                || (type == ThumbnailType.URL && !TextUtils.isEmpty(url) && URLUtil.isNetworkUrl(url))
                || (type == ThumbnailType.LOCAL_PATH && !TextUtils.isEmpty(localPath) && new File(localPath).exists());
    }

    public byte[] getBytes(){
        if (type == ThumbnailType.BYTES && bytes != null && bytes.length > 0) {
            return bytes;
        } else if (type == ThumbnailType.LOCAL_PATH && !TextUtils.isEmpty(localPath)) {
            return Share.toBytes(BitmapFactory.decodeFile(localPath));
        }

        throw new RuntimeException("no thumbnail bitmap found.");
    }

    public String getLocalPath(){
        if (type == ThumbnailType.LOCAL_PATH && !TextUtils.isEmpty(localPath)) {
            return localPath;
        }

        throw new RuntimeException("no thumbnail path found.");
    }

    /**
     * Created by zf08526 on 2017/9/30.
     */
    public enum ThumbnailType implements Serializable{
        URL, BYTES, LOCAL_PATH
    }
}
