package com.feizhang.share.shareto;

import android.content.Context;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.sharecontent.AudioUrl;
import com.feizhang.share.sharecontent.ImageBytes;
import com.feizhang.share.sharecontent.ImagePath;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.MiniProgram;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.Text;
import com.feizhang.share.sharecontent.VideoPath;
import com.feizhang.share.sharecontent.VideoUrl;
import com.feizhang.share.sharecontent.WebUrl;


public class WeChat extends ShareTo {
    public static final int ID = 1;

    public WeChat(ShareContent shareContent) {
        super(shareContent);
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_wechat;
    }

    @Override
    public int getShareName() {
        return R.string.share_wechat;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public boolean installed(Context context) {
        if (isAppNotInstalled(context, "com.tencent.mm")) {
            Toast.makeText(context, R.string.share_we_chat_not_installed_warning, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public String getAppId(Context context) {
        return getPropertyValue(context, "wechat_app_id");
    }

    @Override
    public boolean isSupportToShare() {
        return mShareContent instanceof AudioUrl
                || mShareContent instanceof ImageBytes
                || mShareContent instanceof ImagePath
                || mShareContent instanceof ImageUrl
                || mShareContent instanceof MiniProgram
                || mShareContent instanceof Text
                || mShareContent instanceof VideoPath
                || mShareContent instanceof VideoUrl
                || mShareContent instanceof WebUrl;
    }
}
