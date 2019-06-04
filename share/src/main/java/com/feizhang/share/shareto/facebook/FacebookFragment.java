package com.feizhang.share.shareto.facebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.feizhang.share.Share;
import com.feizhang.share.ShareResult;
import com.feizhang.share.sharecontent.ImageBytes;
import com.feizhang.share.sharecontent.ImagePath;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.VideoUrl;
import com.feizhang.share.sharecontent.WebUrl;

import java.util.HashMap;

public class FacebookFragment extends Fragment {
    private static final String EXTRA_SHARE_CONTENT = "shareContent";
    private ShareContent mShareContent;
    private CallbackManager mCallbackManager;

    public static FacebookFragment newInstance(ShareContent shareContent){
        FacebookFragment fragment = new FacebookFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SHARE_CONTENT, shareContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            mShareContent = (ShareContent) savedInstanceState.getSerializable(EXTRA_SHARE_CONTENT);
        } else if (getArguments() != null){
            mShareContent = (ShareContent) getArguments().getSerializable(EXTRA_SHARE_CONTENT);
        }

        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Context context = getContext();
        if (mShareContent instanceof WebUrl){
            WebUrl webUrl = (WebUrl) mShareContent;
            ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse(webUrl.getWebUrl())).build();
            doShare(context, content);
            return;
        }

        if (mShareContent instanceof ImageUrl){
            ImageUrl imageUrl = (ImageUrl) mShareContent;
            SharePhoto photo = new SharePhoto.Builder().setImageUrl(Uri.parse(imageUrl.getImageUrl())).build();
            SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
            doShare(context, content);
            return;
        }

        if (mShareContent instanceof ImageBytes){
            ImageBytes imageBytes = (ImageBytes) mShareContent;
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes.getBytes(), 0, imageBytes.getBytes().length);
            if (bitmap != null){
                SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                doShare(context, content);
            }
            return;
        }

        if (mShareContent instanceof ImagePath){
            ImagePath imagePath = (ImagePath) mShareContent;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath.getImagePath());
            if (bitmap != null){
                SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                doShare(context, content);
            }
            return;
        }

        if (mShareContent instanceof VideoUrl){
            VideoUrl videoUrl = (VideoUrl) mShareContent;
            ShareVideo video = new ShareVideo.Builder().setLocalUrl(Uri.parse(videoUrl.getVideoUrl())).build();
            ShareVideoContent content = new ShareVideoContent.Builder().setVideo(video).build();
            doShare(context, content);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_SHARE_CONTENT, mShareContent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void doShare(Context context, com.facebook.share.model.ShareContent shareContent){
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Intent intent = new Intent(Share.buildAction(context));
                intent.putExtra(Share.EXTRA_SHARE_TO, Facebook.ID);
                intent.putExtra(Share.EXTRA_SHARE_RESULT, ShareResult.SUCCESS);
                intent.putExtra(Share.EXTRA_SHARE_INFO, new HashMap<>());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onCancel() {
                Intent intent = new Intent(Share.buildAction(context));
                intent.putExtra(Share.EXTRA_SHARE_TO, Facebook.ID);
                intent.putExtra(Share.EXTRA_SHARE_RESULT, ShareResult.CANCELED);
                intent.putExtra(Share.EXTRA_SHARE_INFO, new HashMap<>());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onError(FacebookException error) {
                Intent intent = new Intent(Share.buildAction(context));
                intent.putExtra(Share.EXTRA_SHARE_TO, Facebook.ID);
                intent.putExtra(Share.EXTRA_SHARE_RESULT, ShareResult.FAILED);
                intent.putExtra(Share.EXTRA_SHARE_INFO, new HashMap<>());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        shareDialog.show(shareContent);
    }
}
