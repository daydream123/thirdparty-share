package com.feizhang.share;

import android.content.Context;

import com.feizhang.share.shareto.ShareTo;

import java.util.Map;

public abstract class OnShareListener {
    public void onStart(Context context, ShareTo shareTo) {
    }

    public void onSuccess(ShareTo shareTo, Map<String, String> resultInfo) {
    }

    public void onFailed(ShareTo shareTo) {
    }

    public void onCanceled(ShareTo shareTo) {
    }

}
