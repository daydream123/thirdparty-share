package com.feizhang.share.sample.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.feizhang.share.Share;
import com.feizhang.share.ShareResult;
import com.feizhang.share.ShareResultEnum;
import com.feizhang.share.sample.R;
import com.feizhang.share.shareto.ShareTo;
import com.feizhang.share.shareto.WeChat;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI mApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String appId = ShareTo.getPropertyValue(this, "wechat_app_id");
        mApi = WXAPIFactory.createWXAPI(this, appId, false);

        try {
            Intent intent = getIntent();
            mApi.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        // 微信发送请求到第三方应用时，会回调到该方法
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.getType()) {
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX: {
                switch (resp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        Toast.makeText(this, R.string.share_result_completed, Toast.LENGTH_SHORT).show();
                        Share.notifyShareResult(getApplicationContext(), new ShareResult(WeChat.ID, ShareResultEnum.SUCCESS));
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        Toast.makeText(this, R.string.share_result_failed, Toast.LENGTH_SHORT).show();
                        Share.notifyShareResult(getApplicationContext(), new ShareResult(WeChat.ID, ShareResultEnum.CANCELED));
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        Toast.makeText(this, R.string.share_result_auth_denied, Toast.LENGTH_SHORT).show();
                        Share.notifyShareResult(getApplicationContext(), new ShareResult(WeChat.ID, ShareResultEnum.FAILED));
                        break;
                    default:
                        Toast.makeText(this, R.string.share_result_failed, Toast.LENGTH_SHORT).show();
                        Share.notifyShareResult(getApplicationContext(), new ShareResult(WeChat.ID, ShareResultEnum.FAILED));
                        break;
                }
            }
            finish();
        }
    }
}