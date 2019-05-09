package com.feizhang.share.shareto;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.ShareConfig;
import com.feizhang.share.sharecontent.AudioUrl;
import com.feizhang.share.sharecontent.ImagePath;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.WebUrl;

import java.io.Serializable;

public class QQ extends ShareTo implements Serializable {
    public static final int ID = 3;

    public QQ(ShareContent shareContent) {
        super(shareContent);
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_qq;
    }

    @Override
    public int getShareName() {
        return R.string.share_qq;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public boolean installed(Context context) {
        if (isAppNotInstalled(context, "com.tencent.mobileqq")) {
            Toast.makeText(context, R.string.share_qq_not_installed_warning, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public String getAppId(Context context) {
        String appId = ShareConfig.getQQAppId();
        if (TextUtils.isEmpty(appId)){
            throw new IllegalArgumentException("No app id found for QQ, please config in Application with ShareConfig");
        }

        return appId;
    }

    @Override
    public boolean isSupportToShare() {
        return mShareContent instanceof AudioUrl
                || mShareContent instanceof ImagePath
                || mShareContent instanceof ImageUrl
                || mShareContent instanceof WebUrl;
    }
}
