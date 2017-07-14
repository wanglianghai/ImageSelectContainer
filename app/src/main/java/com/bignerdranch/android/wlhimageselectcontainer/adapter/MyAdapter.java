package com.bignerdranch.android.wlhimageselectcontainer.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bignerdranch.android.wlhimageselectcontainer.R;
import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;
import com.bignerdranch.android.wlhimageselectcontainer.click.OnChangeListener;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10/010.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MyAdapter";
    private Context mContext;
    private List<ImageBean> mImageBeen;
    private OnChangeListener mOnChangeListener;

    private boolean notifyChange = false;
    private float mScreenWith;
    private int CAMERA_TYPE = 0;
    private int LAYOUT_TYPE = 1;

    public MyAdapter(Context context, List<ImageBean> imageBeen, OnChangeListener onChangeListener) {
        mContext = context;
        mImageBeen = imageBeen;
        mOnChangeListener = onChangeListener;

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
            return new MyHolder(view, mOnChangeListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //第一个子类类型不一样不能在外面赋值
        //MyHolder itemView = (MyHolder) holder;
        if (getItemViewType(position) == LAYOUT_TYPE) {
            MyHolder itemView = (MyHolder) holder;
            Glide.with(mContext).load(mImageBeen.get(position).getPath()).into(itemView.mImageView);

            //设置check box的和点了之后的视图
            itemView.mCheckBox.setVisibility(View.VISIBLE);
            //被选中的设置视图是能选择的
            if (mImageBeen.get(position).isSelect()) {
                itemView.mCheckBox.setChecked(true);
                itemView.canSelect();
            } else{
                //满了要改变改变
                if (notifyChange) {
                    itemView.cannotSelect();
                    itemView.mCheckBox.setVisibility(View.GONE);
                } else {
                    itemView.canSelect();
                }
                //check box 用checked，视图回收重用
                itemView.mCheckBox.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mImageBeen.size();
    }

    //接受传入的值
    //notifyDataSetChanged();耗时会崩app
    public void  notifyData(boolean notifyChange) {
        this.notifyChange = notifyChange;
        Log.i(TAG, "run: out" + Thread.currentThread().getName());
        //还是在主线程运行,但是放消息对列中，能尝试什么时候可以运行不崩
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private class MyHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        private ImageView mImageView;
        private OnChangeListener mOnChangeListener;
        private CheckBox mCheckBox;

        private MyHolder(View itemView, OnChangeListener onChangeListener) {
            super(itemView);
            mOnChangeListener = onChangeListener;
            mImageView = (ImageView) itemView.findViewById(R.id.item_image);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.item_check_box);
            mCheckBox.setOnCheckedChangeListener(this);
            //适配imageView，正方形，宽和高都是屏幕宽度的1/3
            //1.得到所在视图层的参数
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
            //2.设置视图层参数
            params.width = (int) (mScreenWith / 3);
            params.height = (int) (mScreenWith / 3);
            //3.用视图层的子组件设置参数
            mImageView.setLayoutParams(params);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mOnChangeListener.onChangeListener(getAdapterPosition(), isChecked);
        }

        //check box 对应click able
        public void canSelect() {
            mImageView.setAlpha(1.0f);
            mCheckBox.setClickable(true);
        }

        public void cannotSelect() {
            mImageView.setAlpha(0.3f);
            mCheckBox.setClickable(false);
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
