package com.bignerdranch.android.wlhimageselectcontainer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.wlhimageselectcontainer.R;
import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageDirBean;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21/021.
 */

public class ImageDirSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ImageDirBean> mDirBeen;
    private Context mContext;

    public ImageDirSelectAdapter(List<ImageDirBean> dirBeen, Context context) {
        mDirBeen = dirBeen;
        mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dir_path_item, parent, false);
        return new ImageDirSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageDirSelectViewHolder dirSelectViewHolder = (ImageDirSelectViewHolder) holder;
        dirSelectViewHolder.bind(mDirBeen.get(position));
    }

    @Override
    public int getItemCount() {
        return mDirBeen.size();
    }

    private class ImageDirSelectViewHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "ImageDirSelectViewHolder";
        private ImageView mImageView;
        private TextView mTextViewTitle;
        private TextView mTextViewNum;

        public ImageDirSelectViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view_item);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.picture_dir);
            mTextViewNum = (TextView) itemView.findViewById(R.id.text_view_item_num);
        }

        public void bind(ImageDirBean dirBean) {
            Glide.with(mContext).load(dirBean.getFirstImagePath()).into(mImageView);
            mTextViewTitle.setText(dirBean.getFileName());
            //不能用int的类型
            mTextViewNum.setText(dirBean.getImageSize() + "");
        }
    }
}
