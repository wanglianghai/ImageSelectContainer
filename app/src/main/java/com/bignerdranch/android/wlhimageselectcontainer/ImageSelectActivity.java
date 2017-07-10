package com.bignerdranch.android.wlhimageselectcontainer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bignerdranch.android.wlhimageselectcontainer.Bean.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageSelectActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    /**
     * 所有的图片
     */
    private List<ImageBean> mImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        getImageList();
    }

    private void getImageList() {
        new MyThread().start();
    }

    //异步线程下载图片
    class MyThread extends Thread{
        private static final String TAG = "MyThread";
        @Override
        public void run() {
            super.run();
            //第一张图片
            String fistImage = null;
            //获取内存卡路径
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            //通过内容解析器解析出png和jpeg格式的图片
            ContentResolver mContentResolver = ImageSelectActivity.this
                    .getContentResolver();
            Cursor mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            +MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/png", "image/jpeg"},
                    MediaStore.Images.Media.DATE_MODIFIED);
            //判断是否存在图片
            if (mCursor.getCount() > 0) {
                //第一个放自己的图片
                mImages.add(new ImageBean());
                while (mCursor.moveToNext()) {
                    // 获取该图片的文件名
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.i(TAG, "run: path:" + path);
                    //抽象路径： 路径转换为分隔符
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }
                    Log.i(TAG, "run: parentFile:" + parentFile);
                    //获取到文件地址
                    String dirPath = parentFile.getAbsolutePath();
                    Log.i(TAG, "run: dirPath:" + dirPath);
                    ImageBean imageBean = new ImageBean();
                    imageBean.setPath(path);
                    Log.i(TAG, "run: ");
                }
            }
        }
    }
}
