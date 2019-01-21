package com.feizhang.share.sharecontent;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import com.feizhang.share.ShareSupport;
import com.feizhang.share.shareto.QQ;
import com.feizhang.share.shareto.QZone;
import com.feizhang.share.shareto.ShareTo;
import com.feizhang.share.shareto.Sms;
import com.feizhang.share.shareto.Timeline;
import com.feizhang.share.shareto.WeChat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by zhangfei on 2017/10/6.
 */

public abstract class ShareContent implements ShareSupport, Serializable{

    public abstract boolean validate(Context context);

    @Override
    public void share(Context context, ShareTo shareTo) {
        if (shareTo.installed(context) && validate(context)) {
            if (shareTo instanceof WeChat) {
                wechatShare(context, shareTo);
            } else if (shareTo instanceof Timeline){
                timelineShare(context, shareTo);
            } else if (shareTo instanceof QQ) {
                qqShare(context, shareTo);
            } else if (shareTo instanceof QZone) {
                qzoneShare(context, shareTo);
            } else if (shareTo instanceof Sms) {
                smsShare(context, shareTo);
            }
        }
    }

    CharSequence getAppName(Context context) {
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            return packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    @Nullable
    String saveAsFile(Context context, byte[] bytes) {
        File storeFile = new File(context.getExternalCacheDir() + File.separator + "temp_wx_share.png");
        if (!storeFile.getParentFile().exists()) {
            boolean created = storeFile.getParentFile().mkdir();
            if (!created){
                return "";
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(storeFile);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
            return storeFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    String saveAsFile(Context context, Bitmap bitmap) {
        File storeFile = new File(context.getExternalCacheDir() + File.separator + "temp_share.png");
        if (!storeFile.getParentFile().exists()) {
            boolean created = storeFile.getParentFile().mkdir();
            if (!created){
                return "";
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(storeFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return storeFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
