package com.feizhang.share;

import android.content.Context;

import com.feizhang.share.shareto.ShareTo;

import java.util.Map;

public abstract class OnShareListener {
    public void onShareStart(Context context, ShareTo shareTo) {
    }

    public void onShareResult(int shareFrom, int shareResult, Map<String, String> resultInfo) {
    }
}
