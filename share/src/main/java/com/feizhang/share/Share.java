package com.feizhang.share;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.feizhang.share.sharecontent.ShareContent;
import com.feizhang.share.shareto.QQ;
import com.feizhang.share.shareto.QZone;
import com.feizhang.share.shareto.ShareTo;
import com.feizhang.share.shareto.Timeline;
import com.feizhang.share.shareto.WeChat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Share {
    public static final String TAG = "Share";
    public static final String EXTRA_SHARE_FROM = "shareFrom";
    public static final String EXTRA_SHARE_RESULT = "shareResult";
    public static final String EXTRA_SHARE_INFO = "shareInfo";

    private Context mContext;
    private OnShareListener mOnShareListener;
    private Permissions mPermissions;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mOnShareListener != null) {
                String action = intent.getAction();
                if (TextUtils.equals(action, buildAction(context))) {
                    int shareFrom = intent.getIntExtra(EXTRA_SHARE_FROM, -1);
                    int shareResult = intent.getIntExtra(EXTRA_SHARE_RESULT, -1);
                    Map<String, String> shareInfo = (Map<String, String>) intent.getSerializableExtra(EXTRA_SHARE_INFO);
                    mOnShareListener.onShareResult(shareFrom, shareResult, shareInfo);
                    LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                }
            }
        }
    };

    private Share(FragmentActivity activity) {
        mContext = activity;
        mPermissions = new Permissions(activity);
        registerReceiver(activity.getApplicationContext(), activity.getLifecycle());
    }

    private Share(Fragment fragment){
        mContext = fragment.getActivity();
        mPermissions = new Permissions(fragment);
        registerReceiver(fragment.getContext(), fragment.getLifecycle());
    }

    public static Share with(AppCompatActivity context) {
        return new Share(context);
    }

    public static Share with(Fragment fragment) {
        return new Share(fragment);
    }

    private void registerReceiver(Context context, Lifecycle lifecycle){
        lifecycle.addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            public void onCreate(){
                IntentFilter filter = new IntentFilter();
                filter.addAction(buildAction(context));
                LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, filter);
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroy(){
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mReceiver);
            }
        });
    }

    public Share setOnShareListener(OnShareListener listener) {
        mOnShareListener = listener;
        return this;
    }

    public void shareAll(@NonNull ShareContent shareContent) {
        WeChat weChat = new WeChat(shareContent);
        Timeline timeline = new Timeline(shareContent);
        QQ qq = new QQ(shareContent);
        QZone qZone = new QZone(shareContent);
        share(weChat, timeline, qq, qZone);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void share(@NonNull final ShareTo... shareTos) {
        mPermissions.request(new OnGrantResult() {
            @Override
            public void onGrant() {
                doShare(shareTos);
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void share(@NonNull final List<ShareTo> shareTos) {
        mPermissions.request(new OnGrantResult() {
            @Override
            public void onGrant() {
                doShare(shareTos.toArray(new ShareTo[0]));
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void doShare(@NonNull final ShareTo... shareTos){
        if (shareTos.length == 0) {
            return;
        }

        // open share panel
        @SuppressLint("InflateParams")
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_share, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(contentView);
        View bottomSheet = dialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        }
        ShareView shareView = contentView.findViewById(R.id.share_view);
        ShareView.ShareAdapter adapter = shareView.setShareContents(Arrays.asList(shareTos), new OnShareListener() {
            @Override
            public void onShareStart(Context context, ShareTo shareTo) {
                if (mOnShareListener != null) {
                    mOnShareListener.onShareStart(context, shareTo);
                }

                dialog.dismiss();
            }
        });

        // cancel button
        View cancelBtn = contentView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        if (adapter.allowShow()) {
            dialog.show();
        }
    }

    public static String buildAction(Context context){
        return context.getPackageName() + ".ACTION_SHARE_RESULT";
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(input, output);
            return output.toByteArray();
        }
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        int readCount;
        byte[] buffer = new byte[1024 * 4];
        while ((readCount = input.read(buffer)) != -1) {
            output.write(buffer, 0, readCount);
        }
        output.flush();
    }

    public static byte[] toBytes(final Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] scaleToLimitedSize(byte[] bytes, int maxSize) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int inSampleSize = 1;
        int scaledSize = bytes.length;
        Bitmap bitmap = null;

        while (scaledSize > maxSize) {
            inSampleSize++;

            // BEGIN_INCLUDE (read_bitmap_dimensions)
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            // Calculate inSampleSize
            options.inSampleSize = inSampleSize;
            // END_INCLUDE (read_bitmap_dimensions)

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            // Computer scaled size
            outputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            scaledSize = outputStream.toByteArray().length;
            bitmap.recycle();
        }

        if (bitmap == null) {
            return bytes;
        }

        return outputStream.toByteArray();
    }
}
