package com.bignerdranch.android.wlhimageselectcontainer.click;

/**
 * Created by Administrator on 2017/7/11/011.
 */

public interface OnChangeListener {
    //方法名字改一下就知道在哪裏用了
    //監聽MyAdapter 里 viewHolder 中的 check box，在ImageSelectActivity中控制更新MyAdapter
    void onChangeListener(int position, boolean isCheck);
}
