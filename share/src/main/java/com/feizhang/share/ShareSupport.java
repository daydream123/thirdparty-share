package com.feizhang.share;

import android.content.Context;

import com.feizhang.share.shareto.ShareTo;

public interface ShareSupport {

    void share(Context context, ShareTo shareTo);

    void qqShare(Context context, ShareTo shareTo);

    void qzoneShare(Context context, ShareTo shareTo);

    void wechatShare(Context context, ShareTo shareTo);

    void timelineShare(Context context, ShareTo shareTo);

    void smsShare(Context context, ShareTo shareTo);
}
