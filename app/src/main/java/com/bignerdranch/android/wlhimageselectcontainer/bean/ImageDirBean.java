package com.bignerdranch.android.wlhimageselectcontainer.bean;

/**
 * Created by Administrator on 2017/7/20/020.
 */

public class ImageDirBean {
    private String fileName;
    private int imageSize;
    private String firstImagePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    @Override
    public String toString() {
        return "file name: " + fileName + ". image size: " + imageSize + ". first image path: " + firstImagePath;
    }
}
