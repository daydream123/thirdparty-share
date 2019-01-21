# thirdparty-share

```java
// 弹出分享框并显示指定shareContent所支持的所有分享图标
WebUrl webUrl = new WebUrl("https://www.qq.com", "百度首页");
Share.with(Activity.this|Fragment.this).shareAll(webUrl);

// 弹出分享框并显示指定数量的ShareTo对应的分享图标
MiniProgram miniProgram = new MiniProgram("https://m.chebada.com", userName, path, thumbnail));
miniProgram.setTitle(resBody.grabShareItem.shareDescription);
miniProgram.setSummary(context.getString(R.string.train_detail_share_des));
WeChat weChat = new WeChat(miniProgram);

QQ qq = new QQ(webUrl);
Share.with(Activity.this|Fragment.this).share(weChat, qq);

// 类似share(ShareTo... shareTos)
Share.with(Activity.this|Fragment.this).share(List<ShareTo> shareTos);

// 直接唤醒第三方分享（无分享框显示）
WeChat weChat = new WeChat(webUrl);
webChat.share(context);
```
