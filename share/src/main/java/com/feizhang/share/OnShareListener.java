package com.feizhang.share;

import android.content.Context;

import com.feizhang.share.shareto.ShareTo;

public interface OnShareListener {

    void onShare(Context context, ShareTo shareTo);
}
