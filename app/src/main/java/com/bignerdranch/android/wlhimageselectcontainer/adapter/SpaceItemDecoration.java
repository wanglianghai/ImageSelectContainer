package com.bignerdranch.android.wlhimageselectcontainer.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/7/10/010.
 */

//重写item装饰
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    //重写抵消设置
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = space;
        outRect.left = space;
        outRect.right = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) < 3 ) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }
}
