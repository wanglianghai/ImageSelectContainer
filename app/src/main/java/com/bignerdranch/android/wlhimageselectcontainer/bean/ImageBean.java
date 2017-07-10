package com.bignerdranch.android.wlhimageselectcontainer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/8/008.
 */

//进程间通信数据传递的最好方式,共享内存空间
public class ImageBean implements Parcelable  {
    private String path;
    private boolean isSelect = false;


    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel source) {
            ImageBean bean = new ImageBean();
            bean.path = source.readString();
            //1: true  0:false
            bean.isSelect = source.readByte() != 0;
            return null;
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
