package com.feizhang.share.shareto.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.feizhang.share.R;
import com.feizhang.share.Share;
import com.feizhang.share.ShareConfig;
import com.feizhang.share.ShareResult;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.HashMap;

public class QQShareActivity extends Activity {
    private static final String EXTRA_BUNDLE = "bundle";
    private static final String EXTRA_SHARE_TO_ID = "shareToId";

    private Bundle mBundle;
    private int mShareToId;
    private IUiListener mShareListener;

    public static void startActivity(@NonNull Context context, Bundle bundle, int shareToID) {
        Intent intent = new Intent(context, QQShareActivity.class);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_SHARE_TO_ID, shareToID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle(EXTRA_BUNDLE);
            mShareToId = savedInstanceState.getInt(EXTRA_SHARE_TO_ID, QQ.ID);
        } else {
            mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE);
            mShareToId = getIntent().getIntExtra(EXTRA_SHARE_TO_ID, QQ.ID);
        }

        Tencent tencent = Tencent.createInstance(ShareConfig.getQQAppId(), this);
        if (tencent == null) {
            throw new RuntimeException("Tencent instance create failed, please check config in Manifest");
        }

        Context context = this;
        mShareListener = new IUiListener() {

            @Override
            public void onComplete(Object o) {
                Toast.makeText(context, R.string.share_result_completed, Toast.LENGTH_SHORT).show();

                // share result feedback
                Intent intent = new Intent(Share.buildAction(context));
                intent.putExtra(Share.EXTRA_SHARE_TO, mShareToId);
                intent.putExtra(Share.EXTRA_SHARE_RESULT, ShareResult.SUCCESS);
                intent.putExtra(Share.EXTRA_SHARE_INFO, new HashMap<>());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                finish();
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(context, R.string.share_result_failed, Toast.LENGTH_SHORT).show();

                // share result feedback
                Intent intent = new Intent(Share.buildAction(context));
                intent.putExtra(Share.EXTRA_SHARE_TO, mShareToId);
                intent.putExtra(Share.EXTRA_SHARE_RESULT, ShareResult.FAILED);
                intent.putExtra(Share.EXTRA_SHARE_INFO, new HashMap<>());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, R.string.share_result_canceled, Toast.LENGTH_SHORT).show();

                // share result feedback
                Intent intent = new Intent(Share.buildAction(context));
                intent.putExtra(Share.EXTRA_SHARE_TO, mShareToId);
                intent.putExtra(Share.EXTRA_SHARE_RESULT, ShareResult.CANCELED);
                intent.putExtra(Share.EXTRA_SHARE_INFO, new HashMap<>());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                finish();
            }
        };

        if (mShareToId == QQ.ID) {
            tencent.shareToQQ(this, mBundle, mShareListener);
        } else if (mShareToId == QZone.ID) {
            tencent.shareToQzone(this, mBundle, mShareListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(EXTRA_BUNDLE, mBundle);
        outState.putInt(EXTRA_SHARE_TO_ID, mShareToId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener);
    }
}
