package com.bignerdranch.android.wlhimageselectcontainer.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bignerdranch.android.wlhimageselectcontainer.R;
import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageDirBean;
import com.bignerdranch.android.wlhimageselectcontainer.click.OnDirSelectListener;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21/021.
 */

public class ImageDirSelectAdapter extends BaseQuickAdapter<ImageDirBean, BaseViewHolder> {
    public ImageDirSelectAdapter(@Nullable List<ImageDirBean> data) {
        super(R.layout.dir_path_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageDirBean item) {
        helper.setText(R.id.picture_dir, item.getFileName())
              .setText(R.id.text_view_item_num, item.getImageSize() + "");
        Glide.with(mContext).load(item.getFirstImagePath()).into((ImageView) helper.getView(R.id.image_view_item));
    }
}
