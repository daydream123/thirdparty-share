package com.feizhang.share;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.feizhang.share.shareto.ShareTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShareView extends LinearLayout {

    public ShareView(Context context) {
        super(context);
        init(context);
    }

    public ShareView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_share_view, this, true);
    }

    public ShareAdapter setShareContents(List<ShareTo> shareTos, OnShareListener listener){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        ShareAdapter adapter = new ShareAdapter(shareTos, listener);
        recyclerView.setAdapter(adapter);
        return adapter;
    }

    public static class ShareAdapter extends RecyclerView.Adapter<ShareViewHolder> {
        private OnShareListener mOnShareListener;
        private List<ShareTo> mShareTos;

        ShareAdapter(List<ShareTo> shareTos, OnShareListener listener){
            mOnShareListener = listener;
            sort(mShareTos = filterInvalid(shareTos));
        }

        @NonNull
        @Override
        public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ShareViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.layout_share_view_item, parent,
                    false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ShareViewHolder holder, int position) {
            final ShareTo shareTo = mShareTos.get(position);

            holder.logoIcon.setImageResource(shareTo.getShareLogo());
            holder.nameText.setText(shareTo.getShareName());

            holder.itemView.setOnClickListener(view -> {
                mOnShareListener.onShare(view.getContext(), shareTo);
                shareTo.share(view.getContext());
            });
        }

        @Override
        public int getItemCount() {
            return mShareTos.size();
        }

        private List<ShareTo> filterInvalid(List<ShareTo> shareTos){
            List<ShareTo> result = new ArrayList<>();
            for (ShareTo shareTo : shareTos){
                if (shareTo.isSupportToShare()){
                    result.add(shareTo);
                }
            }

            return result;
        }

        private void sort(List<ShareTo> shareTos){
            Collections.sort(shareTos, (o1, o2) -> Integer.compare(o1.getSortId(), o2.getSortId()));
        }

        boolean allowShow(){
            return mShareTos.size() > 0;
        }
    }

    private static class ShareViewHolder extends RecyclerView.ViewHolder {
        ImageView logoIcon;
        TextView nameText;

        ShareViewHolder(View itemView) {
            super(itemView);
            logoIcon = itemView.findViewById(R.id.share_imageview);
            nameText = itemView.findViewById(R.id.share_textview);
        }
    }
}
