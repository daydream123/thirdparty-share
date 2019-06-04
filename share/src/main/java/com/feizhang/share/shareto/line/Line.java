package com.feizhang.share.shareto.line;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.feizhang.share.R;
import com.feizhang.share.sharecontent.ImagePath;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.sharecontent.Text;
import com.feizhang.share.shareto.ShareTo;

public class Line extends ShareTo {
    public static final int ID = 6;

    public Line(ShareContent shareContent){
        mShareContent = shareContent;
    }

    public Line(){
        super();
    }

    @Override
    public int getShareLogo() {
        return R.drawable.logo_line;
    }

    @Override
    public int getShareName() {
        return R.string.share_line;
    }

    @Override
    public int getSortId() {
        return ID;
    }

    @Override
    public String getPackageName(Context context) {
        return "jp.naver.line.android";
    }

    @Override
    public boolean isSupportToShare() {
        return mShareContent instanceof Text
                || mShareContent instanceof ImagePath;
    }

    @Override
    public void share(Context context) {
        if (mShareContent instanceof Text) {
            Text text = (Text) mShareContent;
            String shareText = text.getText();
            if (text.getText().length() > 2000) {
                shareText = shareText.substring(0, 2000);
            }

            String scheme = "line://msg/text/" + shareText;
            Uri uri = Uri.parse(scheme);
            context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } else if (mShareContent instanceof ImagePath) {
            ImagePath imagePath = (ImagePath) mShareContent;
            String scheme = "line://msg/image" + imagePath.getImagePath();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(scheme)));
        }
    }
}
