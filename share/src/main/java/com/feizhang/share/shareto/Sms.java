package com.feizhang.share.shareto;

import android.content.Context;

import com.feizhang.share.R;
import com.feizhang.share.sharecontent.AudioUrl;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.Text;
import com.feizhang.share.sharecontent.VideoUrl;
import com.feizhang.share.sharecontent.WebUrl;

public class Sms extends ShareTo{
    public static final int ID = 5;

    public Sms(ShareContent shareContent) {
        super(shareContent);
    }

    public Sms(){
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_sms;
    }

    @Override
    public int getShareName() {
        return R.string.share_sms;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public boolean installed(Context context) {
        return true;
    }

    @Override
    public String getAppId(Context context) {
        // no app id for sms
        return null;
    }

    @Override
    public boolean isSupportToShare() {
        return mShareContent instanceof AudioUrl
                || mShareContent instanceof ImageUrl
                || mShareContent instanceof Text
                || mShareContent instanceof VideoUrl
                || mShareContent instanceof WebUrl;
    }
}
