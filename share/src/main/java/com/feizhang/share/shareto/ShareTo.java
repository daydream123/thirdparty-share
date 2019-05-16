package com.feizhang.share.shareto;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.feizhang.share.sharecontent.ShareContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public abstract class ShareTo implements Serializable {
    protected ShareContent mShareContent;

    @DrawableRes
    public abstract int getShareLogo();

    @StringRes
    public abstract int getShareName();

    public abstract int getSortId();

    public abstract String getPackageName(Context context);

    public abstract boolean isSupportToShare();

    public abstract void share(Context context);

    public ShareTo(ShareContent shareContent) {
        mShareContent = shareContent;
    }

    /**
     * Empty share content.
     */
    ShareTo() {
        mShareContent = new ShareContent() {
            @Override
            public boolean validate(Context context) {
                return false;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareTo shareTo = (ShareTo) o;

        return (getShareName() == shareTo.getShareName());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{getShareName()});
    }

    CharSequence getAppName(Context context) {
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    boolean isInstalled(Context context){
        if (context == null) {
            throw new IllegalArgumentException();
        }

        if (TextUtils.isEmpty(getPackageName(context))) {
            throw new IllegalArgumentException();
        }

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(getPackageName(context), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            return info == null;
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    @Nullable
    String saveAsFile(Context context, byte[] bytes) {
        File storeFile = new File(context.getExternalCacheDir() + File.separator + "temp_wx_share.png");
        if (!storeFile.getParentFile().exists()) {
            boolean created = storeFile.getParentFile().mkdir();
            if (!created){
                return "";
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(storeFile);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            return storeFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    String saveAsFile(Context context, Bitmap bitmap) {
        File storeFile = new File(context.getExternalCacheDir() + File.separator + "temp_share.png");
        if (!storeFile.getParentFile().exists()) {
            boolean created = storeFile.getParentFile().mkdir();
            if (!created){
                return "";
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(storeFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return storeFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
