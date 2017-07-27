package com.bignerdranch.android.wlhimageselectcontainer.uri;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.UUID;

/**
 * Created by Administrator on 2017/7/15/015.
 */
//file provider的输出uri
public class UriUnit {
    private static final String TAG = "UriUnit";
    private static final String AUTHORITIES = "com.bignerdranch.android.wlhimageselectcontainer";
    private static Uri photoFileUri;
    private static File photoFile;

    public static File getFileString() {
        return photoFile;
    }

    public static Uri getPhotoFileUri() {
        return photoFileUri;
    }

    //一个功能一块代码
    public static void setPhotoFileUri(Context context) {
        //得到输出的文件路径
        File dirFile = context.getFilesDir();
        //拼接string类型的加。.toString(),类型一致
        String photoFileName = "IMG_" + UUID.randomUUID().toString() + ".jpg";
        photoFile = new File(dirFile, photoFileName);
        //用输出路径配置file provider公共的uri
        photoFileUri = FileProvider.getUriForFile(context, AUTHORITIES, photoFile);
        Log.i(TAG, "setPhotoFileUri: " + photoFileUri);
    }
}
