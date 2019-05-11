package com.feizhang.share.shareto;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.feizhang.share.OnShareListener;
import com.feizhang.share.sharecontent.ShareContent;

import java.io.Serializable;
import java.util.Arrays;

public abstract class ShareTo implements Serializable {
    protected ShareContent mShareContent;

    @DrawableRes
    public abstract int getShareLogo();

    @StringRes
    public abstract int getShareName();

    public abstract int getSortId();

    public abstract boolean installed(Context context);

    public abstract String getAppId(Context context);

    public abstract boolean isSupportToShare();

    public ShareTo(ShareContent shareContent) {
        mShareContent = shareContent;
    }

    public ShareTo() {
    }

    public void setShareContent(ShareContent shareContent) {
        mShareContent = shareContent;
    }

    public ShareContent getShareContent() {
        return mShareContent;
    }

    public void share(Context context) {
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

    public static ShareTo parseFrom(int shareTo) {
        switch (shareTo) {
            case QQ.ID:
                return new QQ();

            case QZone.ID:
                return new QZone();

            case Sms.ID:
                return new Sms();

            case Timeline.ID:
                return new Timeline();

            case WeChat.ID:
                return new WeChat();

            default:
                throw new IllegalArgumentException("unsupported shareTo: " + shareTo);
        }
    }
}
