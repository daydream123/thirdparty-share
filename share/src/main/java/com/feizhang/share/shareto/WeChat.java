package com.feizhang.share.shareto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Share;
import com.feizhang.share.ShareConfig;
import com.feizhang.share.Thumbnail;
import com.feizhang.share.sharecontent.AudioUrl;
import com.feizhang.share.sharecontent.ImageBytes;
import com.feizhang.share.sharecontent.ImagePath;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.MiniProgram;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.Text;
import com.feizhang.share.sharecontent.VideoPath;
import com.feizhang.share.sharecontent.VideoUrl;
import com.feizhang.share.sharecontent.WeChatShareBuilder;
import com.feizhang.share.sharecontent.WebUrl;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoFileObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;


public class WeChat extends ShareTo {
    public static final int ID = 1;

    public WeChat(ShareContent shareContent) {
        super(shareContent);
    }

    public WeChat(){
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_wechat;
    }

    @Override
    public int getShareName() {
        return R.string.share_wechat;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public boolean installed(Context context) {
        if (isAppNotInstalled(context, "com.tencent.mm")) {
            Toast.makeText(context, R.string.share_we_chat_not_installed_warning, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public String getAppId(Context context) {
        return ShareConfig.getWeChatAppId();
    }

    @Override
    public boolean isSupportToShare() {
        return mShareContent instanceof AudioUrl
                || mShareContent instanceof ImageBytes
                || mShareContent instanceof ImagePath
                || mShareContent instanceof ImageUrl
                || mShareContent instanceof MiniProgram
                || mShareContent instanceof Text
                || mShareContent instanceof VideoPath
                || mShareContent instanceof VideoUrl
                || mShareContent instanceof WebUrl;
    }

    @Override
    public void share(Context context) {
        if (!mShareContent.validate(context)) {
            return;
        }

        String appId = ShareConfig.getWeChatAppId();
        if (mShareContent instanceof AudioUrl) {
            AudioUrl audioUrl = (AudioUrl) mShareContent;
            WXMusicObject object = new WXMusicObject();
            object.musicUrl = audioUrl.getAudioUrl();
            WeChatShareBuilder.buildAndSent(context,
                    appId,
                    SendMessageToWX.Req.WXSceneSession,
                    object,
                    audioUrl.getTitle(),
                    audioUrl.getSummary(),
                    audioUrl.getThumbnail());
            return;
        }

        if (mShareContent instanceof ImageBytes) {
            ImageBytes imageBytes = (ImageBytes) mShareContent;
            String filePath = saveAsFile(context, imageBytes.getBytes());
            if (TextUtils.isEmpty(filePath)){
                Toast.makeText(context, R.string.share_save_file_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            WXImageObject object = new WXImageObject();
            object.setImagePath(filePath);
            WeChatShareBuilder.buildAndSent(context,
                    appId,
                    SendMessageToWX.Req.WXSceneSession,
                    object,
                    imageBytes.getTitle(),
                    imageBytes.getSummary(),
                    new Thumbnail(
                            Share.scaleToLimitedSize(imageBytes.getBytes(),
                            WXMediaMessage.THUMB_LENGTH_LIMIT)));
            return;
        }

        if (mShareContent instanceof ImagePath) {
            ImagePath imagePath = (ImagePath) mShareContent;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getImagePath());
            String filePath = saveAsFile(context, bitmap);
            if (TextUtils.isEmpty(filePath)){
                Toast.makeText(context, R.string.share_save_file_failed, Toast.LENGTH_SHORT).show();
                return;
            }

            WXImageObject object = new WXImageObject();
            object.setImagePath(filePath);
            WeChatShareBuilder.buildAndSent(context,
                    appId,
                    SendMessageToWX.Req.WXSceneSession,
                    object,
                    imagePath.getTitle(),
                    imagePath.getSummary(),
                    new Thumbnail(Thumbnail.ThumbnailType.LOCAL_PATH, imagePath.getImagePath()));
            return;
        }

        if (mShareContent instanceof ImageUrl) {
            ImageUrl imageUrl = (ImageUrl) mShareContent;
            WeChatShareBuilder.downloadBytes(imageUrl.getImageUrl(), new WeChatShareBuilder.OnDownloadListener() {
                @Override
                public void onSuccess(byte[] bytes) {
                    String filePath = saveAsFile(context, bytes);
                    if (TextUtils.isEmpty(filePath)){
                        Toast.makeText(context, R.string.share_save_file_failed, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    WXImageObject object = new WXImageObject();
                    object.setImagePath(filePath);
                    WeChatShareBuilder.buildAndSent(context,
                            appId,
                            SendMessageToWX.Req.WXSceneSession,
                            object, imageUrl.getTitle(), imageUrl.getSummary(),
                            new Thumbnail(Thumbnail.ThumbnailType.URL, imageUrl.getImageUrl()));
                }

                @Override
                public void onError() {
                    Toast.makeText(context, R.string.share_download_image_failed, Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        if (mShareContent instanceof MiniProgram){
            MiniProgram miniProgram = (MiniProgram) mShareContent;
            WXMiniProgramObject object = new WXMiniProgramObject();
            object.webpageUrl = miniProgram.getWebPageUrl();
            object.userName = miniProgram.getUserName();
            object.path = miniProgram.getPath();
            object.miniprogramType = miniProgram.getProgramType();
            WeChatShareBuilder.buildAndSent(context,
                    appId,
                    SendMessageToWX.Req.WXSceneSession,
                    object,
                    miniProgram.getTitle(),
                    miniProgram.getSummary(),
                    miniProgram.getThumbnail());
            return;
        }

        if (mShareContent instanceof Text){
            Text text = (Text) mShareContent;
            WeChatShareBuilder.buildAndSent(context,
                    appId,
                    SendMessageToWX.Req.WXSceneSession,
                    new WXTextObject(text.getText()),
                    text.getTitle(),
                    text.getSummary(),
                    text.getThumbnail());
            return;
        }

        if (mShareContent instanceof VideoPath) {
            VideoPath videoPath = (VideoPath) mShareContent;
            WXVideoFileObject object = new WXVideoFileObject();
            object.filePath = videoPath.getVideoPath();
            WeChatShareBuilder.buildAndSent(context,
                    appId,
                    SendMessageToWX.Req.WXSceneSession,
                    object, videoPath.getTitle(),
                    videoPath.getSummary(),
                    videoPath.getThumbnail());
            return;
        }

        if (mShareContent instanceof VideoUrl){
            VideoUrl videoUrl = (VideoUrl) mShareContent;
            WXVideoObject object = new WXVideoObject();
            object.videoUrl = videoUrl.getVideoUrl();
            WeChatShareBuilder.buildAndSent(context,
                    appId,
                    SendMessageToWX.Req.WXSceneSession,
                    object,
                    videoUrl.getTitle(),
                    videoUrl.getSummary(),
                    videoUrl.getThumbnail());
            return;
        }

        if (mShareContent instanceof WebUrl){
            WebUrl webUrl = (WebUrl) mShareContent;
            WXWebpageObject object = new WXWebpageObject(webUrl.getWebUrl());
            WeChatShareBuilder.buildAndSent(context,
                    appId,
                    SendMessageToWX.Req.WXSceneSession,
                    object,
                    webUrl.getTitle(),
                    webUrl.getSummary(),
                    webUrl.getThumbnail());
        }
    }
}
