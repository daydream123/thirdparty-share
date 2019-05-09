package com.feizhang.share.sharecontent;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.ShareSupport;
import com.feizhang.share.Thumbnail;
import com.feizhang.share.shareto.ShareTo;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;

public class MiniProgram extends ShareContent implements ShareSupport {
    private final Thumbnail thumbnail;
    private final String webPageUrl;
    private final String userName;
    private final String path;
    private String title;
    private String summary;

    public MiniProgram(String webPageUrl, String userName,
                       String path, Thumbnail thumbnail) {
        this.webPageUrl = webPageUrl;
        this.userName = userName;
        this.path = path;
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean validate(Context context) {
        if (thumbnail == null) {
            Toast.makeText(context, R.string.share_mini_program_no_thumbnail, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(webPageUrl)){
            Toast.makeText(context, R.string.share_mini_program_no_webPageUrl, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(context, R.string.share_mini_program_no_userName, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(path)){
            Toast.makeText(context, R.string.share_mini_program_no_path, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void qqShare(Context context, ShareTo shareTo) {
        // not support
    }

    @Override
    public void qzoneShare(Context context, ShareTo shareTo) {
        // not support
    }

    @Override
    public void wechatShare(Context context, ShareTo shareTo) {
        WXMiniProgramObject object = new WXMiniProgramObject();
        object.webpageUrl = webPageUrl;
        object.userName = userName;
        object.path = path;
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneSession,
                object, title, summary, thumbnail);
    }

    @Override
    public void timelineShare(Context context, ShareTo shareTo) {
        // not support
    }

    @Override
    public void smsShare(Context context, ShareTo shareTo) {
        // not support
    }
}
