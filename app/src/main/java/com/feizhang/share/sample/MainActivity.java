package com.feizhang.share.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.feizhang.share.Share;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.WebUrl;
import com.feizhang.share.shareto.qq.QQ;
import com.feizhang.share.shareto.wechat.WeChat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.shareToAllBtn).setOnClickListener(v -> {
            WebUrl webUrl = new WebUrl("https://www.baidu.com", "百度首页");
            Share.with(MainActivity.this).shareAll(webUrl);
        });

        findViewById(R.id.shareToSomeone).setOnClickListener(v -> {
            WebUrl webUrl = new WebUrl("https://www.baidu.com", "百度首页");
            Share.with(MainActivity.this).share(new WeChat(webUrl));
        });

        findViewById(R.id.shareWithDifferentContent).setOnClickListener(v -> {
            WeChat weChat = new WeChat(new WebUrl("https://www.baidu.com", "百度首页"));
            QQ qq = new QQ(new ImageUrl("https://imgazure.microsoftstore.com.cn/_ui/desktop/static/img/surfacepro6/sp6_blue_1180.png"));
            Share.with(MainActivity.this).share(weChat, qq);
        });

        findViewById(R.id.shareDirectly).setOnClickListener(v -> {
            QQ qq = new QQ(new ImageUrl("https://imgazure.microsoftstore.com.cn/_ui/desktop/static/img/surfacepro6/sp6_blue_1180.png"));
            qq.share(v.getContext());
        });

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, new MainFragment())
                .commitAllowingStateLoss();
    }
}
