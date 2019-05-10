package com.chebada.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.chebada.R;
import com.feizhang.share.Share;
import com.feizhang.share.ShareConfig;
import com.feizhang.share.ShareResult;
import com.feizhang.share.shareto.WeChat;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI mApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String appId = ShareConfig.getWeChatAppId();
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
            case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
                handleShareResult(resp);
                break;

            case ConstantsAPI.COMMAND_SENDAUTH:
                // wechat login
                break;

            case ConstantsAPI.COMMAND_PAY_BY_WX:
                // wechat payment
                break;
        }
        finish();
    }

    private void handleShareResult(BaseResp resp) {
        int shareFrom, shareResult;
        HashMap<String, String> shareInfo = new HashMap<>();

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Toast.makeText(this, R.string.share_result_completed, Toast.LENGTH_SHORT).show();
                shareFrom = WeChat.ID;
                shareResult = ShareResult.SUCCESS;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, R.string.share_result_canceled, Toast.LENGTH_SHORT).show();
                shareFrom = WeChat.ID;
                shareResult = ShareResult.CANCELED;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, R.string.share_result_auth_denied, Toast.LENGTH_SHORT).show();
                shareFrom = WeChat.ID;
                shareResult = ShareResult.FAILED;
                break;
            default:
                Toast.makeText(this, R.string.share_result_failed, Toast.LENGTH_SHORT).show();
                shareFrom = WeChat.ID;
                shareResult = ShareResult.FAILED;
                break;
        }

        // share result feedback
        Intent intent = new Intent(Share.buildAction(this));
        intent.putExtra(Share.EXTRA_SHARE_FROM, shareFrom);
        intent.putExtra(Share.EXTRA_SHARE_RESULT, shareResult);
        intent.putExtra(Share.EXTRA_SHARE_INFO, shareInfo);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}