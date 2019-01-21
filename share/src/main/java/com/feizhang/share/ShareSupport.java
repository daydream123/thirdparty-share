package com.feizhang.share;

import android.content.Context;

import com.feizhang.share.shareto.ShareTo;

public interface ShareSupport {

    void share(Context context, ShareTo shareTo);

    default void qqShare(Context context, ShareTo shareTo) {
    }

    default void qzoneShare(Context context, ShareTo shareTo) {
    }

    default void wechatShare(Context context, ShareTo shareTo) {
    }

    default void timelineShare(Context context, ShareTo shareTo) {
    }

    default void smsShare(Context context, ShareTo shareTo) {
    }
}
