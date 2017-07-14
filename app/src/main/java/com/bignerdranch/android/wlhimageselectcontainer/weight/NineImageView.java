package com.bignerdranch.android.wlhimageselectcontainer.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/12/012.
 */

public class NineImageView extends ViewGroup {

    public NineImageView(Context context) {
        super(context);
    }

    public NineImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int exactDivision(int number) {
        if (number % 3 == 0) {
            return 0;
        } else {
            return 1;
        }
    }
    //每一个子view高度要测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View view;
        int preWidth, preHeight, totalWidth, totalHeight, childNum;
        view = getChildAt(0);
        if (view == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        //children复数
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        preWidth = view.getMeasuredWidth();
        preHeight = view.getMeasuredHeight();
        childNum = getChildCount();
        //除要判断是否能整除
        totalHeight = (childNum / 3 + exactDivision(childNum)) * preHeight;
        if (childNum <=3) {
            totalWidth = preWidth * childNum;
        } else if (childNum == 4) {
            totalWidth = preWidth * 2;
            totalHeight = preHeight * 2;
        }  else {
            totalWidth = preWidth * 3;
        }

        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View v;
        int childNum, left, top, parentWidth, preWidth, preHeight;
        v = getChildAt(0);
        if (v == null) {
            return;
        }
        childNum = getChildCount();
        left = getPaddingLeft();
        top = getPaddingTop();
        parentWidth = r - l;

        for (int i = 0; i < childNum; i++) {
            v = getChildAt(i);
            preWidth = v.getMeasuredWidth();
            preHeight = v.getMeasuredHeight();
            if (left + preWidth > parentWidth) {
                top += preHeight;
                left = getPaddingLeft();
            }
            v.layout(left, top, left += preWidth, top + preHeight);
        }
    }

}
