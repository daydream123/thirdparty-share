package com.feizhang.share.shareto;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import com.feizhang.share.OnShareListener;
import com.feizhang.share.sharecontent.ShareContent;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Properties;

public abstract class ShareTo implements Serializable {
    protected ShareContent mShareContent;
    private OnShareListener mOnShareListener;

    @DrawableRes
    public abstract int getShareLogo();

    @StringRes
    public abstract int getShareName();

    public abstract int getSortId();

    public abstract boolean installed(Context context);

    public abstract String getAppId(Context context);

    public abstract boolean isSupportToShare();

    public ShareTo(ShareContent shareContent){
        mShareContent = shareContent;
    }

    public ShareContent getShareContent() {
        return mShareContent;
    }

    public void share(Context context){
        if(mOnShareListener != null){
            mOnShareListener.onShare(context, this);
        }

        mShareContent.share(context, this);
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

    public void setOnShareListener(OnShareListener listener) {
        mOnShareListener = listener;
    }

    boolean isAppNotInstalled(Context context, String packageName) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        if (TextUtils.isEmpty(packageName)) {
            throw new IllegalArgumentException();
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            return info == null;
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    String getPropertyValue(@NonNull Context context, @NonNull String keyName) {
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            inputStream = context.getAssets().open("third_party_apps.properties");
            properties.load(inputStream);

            String appId = properties.getProperty(keyName);
            if (TextUtils.isEmpty(appId)) {
                throw new IllegalArgumentException("no value was set for " + keyName);
            }

            return appId;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no third_party_apps.properties found in assert");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
