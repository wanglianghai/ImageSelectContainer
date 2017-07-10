package com.bignerdranch.android.wlhimageselectcontainer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bignerdranch.android.wlhimageselectcontainer.R;
import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10/010.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ImageBean> mImageBeen;
    private float mScreenWith;
    private int CAMERA_TYPE = 0;
    private int LAYOUT_TYPE = 1;

    public MyAdapter(Context context, List<ImageBean> imageBeen) {
        mContext = context;
        mImageBeen = imageBeen;

        //context通过获取屏幕宽度
        //1.得到窗口的管理对象
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //2.显示指标对象
        DisplayMetrics dm = new DisplayMetrics();
        //3.用窗口管理设置显示指标
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWith = dm.widthPixels;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CAMERA_TYPE;
        }
        return LAYOUT_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CAMERA_TYPE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.camera_item, parent, false);
            return new CameraHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_select_item, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == LAYOUT_TYPE) {
           Glide.with(mContext).load(mImageBeen.get(position).getPath()).into(((MyHolder) holder).mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mImageBeen.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;

        private MyHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.item_image);
            //适配imageView，正方形，宽和高都是屏幕宽度的1/3
            //1.得到所在视图层的参数
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
            //2.设置视图层参数
            params.width = (int) (mScreenWith / 3);
            params.height = (int) (mScreenWith / 3);
            //3.用视图层的子组件设置参数
            mImageView.setLayoutParams(params);
        }
    }

    private class CameraHolder extends RecyclerView.ViewHolder {

        public CameraHolder(View itemView) {
            super(itemView);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.image_item);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.width = (int) (mScreenWith / 3);
            params.height = (int) (mScreenWith / 3);
            imageView.setLayoutParams(params);
        }
    }
}
