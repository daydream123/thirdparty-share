package com.feizhang.share.sample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.feizhang.share.OnShareListener;
import com.feizhang.share.Share;
import com.feizhang.share.sharecontent.WebUrl;
import com.feizhang.share.shareto.QQ;
import com.feizhang.share.shareto.ShareTo;
import com.feizhang.share.shareto.WeChat;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            WebUrl webUrl = new WebUrl("https://www.baidu.com", "百度首页");
            WeChat weChat = new WeChat(webUrl);
            QQ qq = new QQ(webUrl);
            Share.with(MainActivity.this).setOnShareListener(new OnShareListener() {
                @Override
                public void onShareStart(Context context, ShareTo shareTo) {
                    super.onShareStart(context, shareTo);
                }

                @Override
                public void onShareResult(int shareFrom, int shareResult, Map<String, String> resultInfo) {
                    super.onShareResult(shareFrom, shareResult, resultInfo);
                }
            }).share(weChat, qq);
        });
    }
}
