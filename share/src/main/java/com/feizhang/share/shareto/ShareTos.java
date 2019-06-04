package com.feizhang.share.shareto;

import android.util.SparseArray;

import com.feizhang.share.shareto.facebook.Facebook;
import com.feizhang.share.shareto.line.Line;
import com.feizhang.share.shareto.qq.QQ;
import com.feizhang.share.shareto.qq.QZone;
import com.feizhang.share.shareto.sms.Sms;
import com.feizhang.share.shareto.wechat.Timeline;
import com.feizhang.share.shareto.wechat.WeChat;

public final class ShareTos {

    private static final SparseArray<ShareTo> MAPPING = new SparseArray<>();

    static {
        MAPPING.put(WeChat.ID, new WeChat());
        MAPPING.put(Timeline.ID, new Timeline());
        MAPPING.put(QQ.ID, new QQ());
        MAPPING.put(QZone.ID, new QZone());
        MAPPING.put(Sms.ID, new Sms());
        MAPPING.put(Line.ID, new Line());
        MAPPING.put(Facebook.ID, new Facebook());
    }

    public static ShareTo parseFrom(int shareToId) {
        ShareTo shareTo = MAPPING.get(shareToId);
        if (shareTo == null){
            throw new IllegalArgumentException("unsupported shareTo: " + shareToId);
        }

        return shareTo;
    }
}
