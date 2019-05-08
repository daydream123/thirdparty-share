package com.feizhang.share.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.feizhang.share.Share;
import com.feizhang.share.sharecontent.WebUrl;
import com.feizhang.share.shareto.WeChat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            WebUrl webUrl = new WebUrl("https://www.baidu.com", "百度首页");
            WeChat weChat = new WeChat(webUrl);
            Share.with(MainActivity.this).share(weChat);
        });
    }
}
