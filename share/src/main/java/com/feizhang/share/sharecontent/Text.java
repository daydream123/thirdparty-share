package com.feizhang.share.sharecontent;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Thumbnail;

public class Text extends ShareContent {
    private final String text;
    private String title;
    private String summary;
    private Thumbnail thumbnail;

    public Text(String text) {
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

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    @Override
    public boolean validate(Context context) {
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(context, R.string.share_text_no_text, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    static void sendSms(Context context, String content) {
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
