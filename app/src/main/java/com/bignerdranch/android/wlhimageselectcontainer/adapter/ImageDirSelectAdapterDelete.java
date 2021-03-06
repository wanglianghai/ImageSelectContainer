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
import com.bignerdranch.android.wlhimageselectcontainer.click.OnDirSelectListener;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21/021.
 */

public class ImageDirSelectAdapterDelete extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ImageDirBean> mDirBeen;
    private OnDirSelectListener mDirListener;
    private Context mContext;

    public ImageDirSelectAdapterDelete(List<ImageDirBean> dirBeen, Context context, OnDirSelectListener dirListener) {
        mDirBeen = dirBeen;
        mContext = context;
        mDirListener = dirListener;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDirListener.selectDirPath(mDirBeen.get(getAdapterPosition()));
                }
            });
        }

        public void bind(ImageDirBean dirBean) {
            Glide.with(mContext).load(dirBean.getFirstImagePath()).into(mImageView);
            mTextViewTitle.setText(dirBean.getFileName());
            //不能用int的类型
            mTextViewNum.setText(dirBean.getImageSize() + "");
        }
    }
}
