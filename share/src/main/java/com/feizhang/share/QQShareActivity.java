package com.feizhang.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.feizhang.share.shareto.QQ;
import com.feizhang.share.shareto.QZone;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by gj21798 on 2016/8/19.
 * QQ分享
 */
public class QQShareActivity extends Activity {
    private static final String EXTRA_BUNDLE = "bundle";
    private static final String EXTRA_APP_ID = "appId";
    private static final String EXTRA_SHARE_TO_ID = "shareToId";

    private String mAppId;
    private Bundle mBundle;
    private int mShareToId;
    private IUiListener mShareListener;

    public static void startActivity(@NonNull Context context, Bundle bundle, String appId, int shareToID) {
        Intent intent = new Intent(context, QQShareActivity.class);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_APP_ID, appId);
        intent.putExtra(EXTRA_SHARE_TO_ID, shareToID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle(EXTRA_BUNDLE);
            mAppId = savedInstanceState.getString(EXTRA_APP_ID);
            mShareToId = savedInstanceState.getInt(EXTRA_SHARE_TO_ID, QQ.ID);
        } else {
            mBundle = getIntent().getBundleExtra(EXTRA_BUNDLE);
            mAppId = getIntent().getStringExtra(EXTRA_APP_ID);
            mShareToId = getIntent().getIntExtra(EXTRA_SHARE_TO_ID, QQ.ID);
        }

        Tencent tencent = Tencent.createInstance(mAppId, this);

        mShareListener = new IUiListener() {

            @Override
            public void onComplete(Object o) {
                ShareResult result = new ShareResult(mShareToId, ShareResultEnum.SUCCESS);
                Share.notifyShareResult(getApplicationContext(), result);
                Toast.makeText(getApplicationContext(), R.string.share_result_completed, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(UiError uiError) {
                Toast.makeText(getApplicationContext(), R.string.share_result_failed, Toast.LENGTH_SHORT).show();
                Share.notifyShareResult(getApplicationContext(), new ShareResult(mShareToId, ShareResultEnum.FAILED));
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.share_result_canceled, Toast.LENGTH_SHORT).show();
                Share.notifyShareResult(getApplicationContext(), new ShareResult(mShareToId, ShareResultEnum.CANCELED));
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
        outState.putString(EXTRA_APP_ID, mAppId);
        outState.putInt(EXTRA_SHARE_TO_ID, mShareToId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mShareListener);
    }
}
