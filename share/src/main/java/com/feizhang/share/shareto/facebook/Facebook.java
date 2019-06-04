package com.feizhang.share.shareto.facebook;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.feizhang.share.R;
import com.feizhang.share.sharecontent.ImageBytes;
import com.feizhang.share.sharecontent.ImagePath;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.VideoPath;
import com.feizhang.share.sharecontent.VideoUrl;
import com.feizhang.share.sharecontent.WebUrl;
import com.feizhang.share.shareto.ShareTo;

public class Facebook extends ShareTo {
    public static final int ID = 7;

    private FragmentManager mFragmentManager;

    public Facebook(FragmentManager fragmentManager, ShareContent shareContent){
        super(shareContent);
        mFragmentManager = fragmentManager;
    }

    public Facebook(){
        super();
    }

    public Facebook(ShareContent shareContent){
        super(shareContent);
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_facebook;
    }

    @Override
    public int getShareName() {
        return R.string.share_facebook;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public String getPackageName(Context context) {
        return "com.facebook.android";
    }

    @Override
    public boolean isSupportToShare() {
        return mShareContent instanceof WebUrl
                || mShareContent instanceof ImagePath
                || mShareContent instanceof ImageBytes
                || mShareContent instanceof ImageUrl
                || mShareContent instanceof VideoPath
                || mShareContent instanceof VideoUrl;
    }

    @Override
    public void share(Context context) {
        String tag = FacebookFragment.class.getName();
        FacebookFragment fragment = (FacebookFragment) mFragmentManager.findFragmentByTag(tag);

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (fragment != null){
            ft.remove(fragment);
        }
        ft.add(FacebookFragment.newInstance(mShareContent), tag);
        ft.commitAllowingStateLoss();
    }
}
