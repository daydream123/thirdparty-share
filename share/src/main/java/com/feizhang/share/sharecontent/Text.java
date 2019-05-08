package com.feizhang.share.sharecontent;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.text.TextUtils;

import com.feizhang.share.Thumbnail;
import com.feizhang.share.shareto.ShareTo;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;

public class Text extends ShareContent{
    private final String text;
    private String title;
    private String summary;
    private Thumbnail thumbnail;

    public Text(String text){
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean validate(Context context) {
        return !TextUtils.isEmpty(text);
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
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneSession,
                new WXTextObject(text),
                title, summary, thumbnail);
    }

    @Override
    public void timelineShare(Context context, ShareTo shareTo) {
        WeChatShareBuilder.buildAndSent(context,
                shareTo.getAppId(context),
                SendMessageToWX.Req.WXSceneTimeline,
                new WXTextObject(text),
                title, summary, thumbnail);
    }

    @Override
    public void smsShare(Context context, ShareTo shareTo) {
        sendSms(context, text);
    }

    static void sendSms(Context context, String content){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String packageName = Telephony.Sms.getDefaultSmsPackage(context);
            Intent smsIntent = new Intent(Intent.ACTION_SEND);
            smsIntent.setType("text/plain");
            smsIntent.putExtra(Intent.EXTRA_TEXT, content);

            //if no default app is configured, then choose any app that support this intent.
            if (packageName != null) {
                smsIntent.setPackage(packageName);
            }
            context.startActivity(smsIntent);
        } else {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", content);
            smsIntent.putExtra("sms_body","body");
            context.startActivity(smsIntent);
        }
    }
}
