package com.feizhang.share.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feizhang.share.Share;
import com.feizhang.share.sharecontent.ImageUrl;
import com.feizhang.share.sharecontent.WebUrl;
import com.feizhang.share.shareto.qq.QQ;
import com.feizhang.share.shareto.wechat.WeChat;

public class MainFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.share_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.shareToAllBtn).setOnClickListener(v -> {
            WebUrl webUrl = new WebUrl("https://www.baidu.com", "百度首页");
            Share.with(MainFragment.this).shareAll(webUrl);
        });

        view.findViewById(R.id.shareToSomeone).setOnClickListener(v -> {
            WebUrl webUrl = new WebUrl("https://www.baidu.com", "百度首页");
            Share.with(MainFragment.this).share(new WeChat(webUrl));
        });

        view.findViewById(R.id.shareWithDifferentContent).setOnClickListener(v -> {
            WeChat weChat = new WeChat(new WebUrl("https://www.baidu.com", "百度首页"));
            QQ qq = new QQ(new ImageUrl("https://imgazure.microsoftstore.com.cn/_ui/desktop/static/img/surfacepro6/sp6_blue_1180.png"));
            Share.with(MainFragment.this).share(weChat, qq);
        });

        view.findViewById(R.id.shareDirectly).setOnClickListener(v -> {
            QQ qq = new QQ(new ImageUrl("https://imgazure.microsoftstore.com.cn/_ui/desktop/static/img/surfacepro6/sp6_blue_1180.png"));
            qq.share(v.getContext());
        });
    }
}
