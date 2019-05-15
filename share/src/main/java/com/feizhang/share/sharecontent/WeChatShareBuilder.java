package com.feizhang.share.sharecontent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Share;
import com.feizhang.share.Thumbnail;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class WeChatShareBuilder {
    public static void buildAndSent(final Context context,
                                    String appId,
                                    final int wxScene,
                                    WXMediaMessage.IMediaObject mediaObject,
                                    String title,
                                    String summary,
                                    Thumbnail thumbnail) {

        // check title limit
        if (!TextUtils.isEmpty(title)){
            title = title.length() > WXMediaMessage.TITLE_LENGTH_LIMIT ? title.substring(0, WXMediaMessage.TITLE_LENGTH_LIMIT) : title;
        }

        // check thumbnail limit
        AtomicInteger thumbnailLimit = new AtomicInteger(WXMediaMessage.THUMB_LENGTH_LIMIT);
        if (mediaObject instanceof WXMiniProgramObject){
            thumbnailLimit.set(WXMediaMessage.MINI_PROGRAM__THUMB_LENGHT);
        }

        // check summary limit
        if (!TextUtils.isEmpty(summary)){
            summary = summary.length() > WXMediaMessage.DESCRIPTION_LENGTH_LIMIT ? summary.substring(0, WXMediaMessage.DESCRIPTION_LENGTH_LIMIT) : summary;
        }

        final IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, appId);
        final WXMediaMessage msg = new WXMediaMessage(mediaObject);
        msg.title = title;
        msg.description = summary;

        if (thumbnail != null && thumbnail.isValid()) {
            Thumbnail.ThumbnailType thumbnailType = thumbnail.getType();
            if (thumbnailType == Thumbnail.ThumbnailType.BYTES
                    || thumbnailType == Thumbnail.ThumbnailType.LOCAL_PATH) {
                msg.thumbData = Share.scaleToLimitedSize(thumbnail.getBytes(), thumbnailLimit.get());
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = wxScene;
                iwxapi.sendReq(req);
            } else if (thumbnailType == Thumbnail.ThumbnailType.URL) {
                downloadBytes(thumbnail.getUrl(), new OnDownloadListener() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        msg.thumbData = Share.scaleToLimitedSize(bytes, thumbnailLimit.get());
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene = wxScene;
                        iwxapi.sendReq(req);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context, R.string.share_download_thumbnail_failed, Toast.LENGTH_SHORT).show();
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        req.scene = wxScene;
                        iwxapi.sendReq(req);
                    }
                });
            }
        } else {
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = wxScene;
            iwxapi.sendReq(req);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public static void downloadBytes(final String httpUrl, final OnDownloadListener listener) {
        new AsyncTask<Void, Void, byte[]>() {

            @Override
            protected byte[] doInBackground(Void... params) {
                if (!TextUtils.isEmpty(httpUrl)) {
                    try {
                        URL url = new URL(httpUrl);
                        return Share.toByteArray(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                if (bytes != null && bytes.length > 0) {
                    listener.onSuccess(bytes);
                } else {
                    listener.onError();
                }
            }
        }.execute();
    }

    public interface OnDownloadListener {
        void onSuccess(byte[] bytes);

        void onError();
    }
}
