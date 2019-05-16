package com.feizhang.share.shareto;

import android.util.SparseArray;

public class ShareTos {

    private static final SparseArray<ShareTo> MAPPING = new SparseArray<>();

    static {
        MAPPING.put(WeChat.ID, new WeChat());
        MAPPING.put(Timeline.ID, new Timeline());
        MAPPING.put(QQ.ID, new QQ());
        MAPPING.put(QZone.ID, new QZone());
        MAPPING.put(Sms.ID, new Sms());
    }

    public static ShareTo parseFrom(int shareToId) {
        ShareTo shareTo = MAPPING.get(shareToId);
        if (shareTo == null){
            throw new IllegalArgumentException("unsupported shareTo: " + shareToId);
        }

        return shareTo;
    }
}
