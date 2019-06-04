package com.feizhang.share.shareto.sms;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.sharecontent.AudioUrl;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.Text;
import com.feizhang.share.sharecontent.VideoUrl;
import com.feizhang.share.sharecontent.WebUrl;
import com.feizhang.share.shareto.ShareTo;

public class Sms extends ShareTo {
    public static final int ID = 5;

    public Sms(ShareContent shareContent) {
        super(shareContent);
    }

    public Sms(){
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_sms;
    }

    @Override
    public int getShareName() {
        return R.string.share_sms;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public String getPackageName(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Telephony.Sms.getDefaultSmsPackage(context);
        } else {
            return "com.android.mms";
        }
    }

    @Override
    public boolean isSupportToShare() {
        return mShareContent instanceof AudioUrl
                || mShareContent instanceof ImageUrl
                || mShareContent instanceof Text
                || mShareContent instanceof VideoUrl
                || mShareContent instanceof WebUrl;
    }

    @Override
    public void share(Context context) {
        if (!isInstalled(context)) {
            Toast.makeText(context, R.string.share_mms_not_installed_warning, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mShareContent.validate(context)) {
            return;
        }

        if (mShareContent instanceof AudioUrl){
            sendSms(context, ((AudioUrl) mShareContent).getAudioUrl());
        } else if (mShareContent instanceof ImageUrl){
            sendSms(context, ((ImageUrl) mShareContent).getImageUrl());
        } else if (mShareContent instanceof Text){
            sendSms(context, ((Text) mShareContent).getText());
        } else if (mShareContent instanceof VideoUrl){
            sendSms(context, ((VideoUrl) mShareContent).getVideoUrl());
        } else if (mShareContent instanceof WebUrl){
            sendSms(context, ((WebUrl) mShareContent).getWebUrl());
        }
    }

    private void sendSms(Context context, String content) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
            smsIntent.putExtra("sms_body", "body");
            context.startActivity(smsIntent);
        }
    }
}
