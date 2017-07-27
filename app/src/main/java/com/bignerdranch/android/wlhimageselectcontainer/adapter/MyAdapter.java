package com.bignerdranch.android.wlhimageselectcontainer.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/7/22/022.
 */

public class MyAdapter extends BaseQuickAdapter<ImageBean, BaseViewHolder> {

    public MyAdapter(@LayoutRes int layoutResId, @Nullable List<ImageBean> data) {
        super(layoutResId, data);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageBean item) {

    }
}
