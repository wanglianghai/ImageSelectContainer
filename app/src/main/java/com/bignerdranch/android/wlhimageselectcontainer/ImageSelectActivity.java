package com.bignerdranch.android.wlhimageselectcontainer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bignerdranch.android.wlhimageselectcontainer.adapter.MyAdapter;
import com.bignerdranch.android.wlhimageselectcontainer.adapter.SpaceItemDecoration;
import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageSelectActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyThread mThread;

    /**
     * 所有的图片
     */
    private List<ImageBean> mImages = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            //绑定数据
            setData();
            if (mThread != null && !mThread.isInterrupted()) {
                mThread.interrupt();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        getImageList();
    }

    private void setData() {
        mRecyclerView.setAdapter(new MyAdapter(this, mImages));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(50));
    }

    private void getImageList() {
        mThread = new MyThread();
        mThread.start();
    }

    //异步线程下载图片
    private class MyThread extends Thread{
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
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }
                    //获取到文件地址
                    String dirPath = parentFile.getAbsolutePath();

                    ImageBean imageBean = new ImageBean();
                    imageBean.setPath(path);
                    mImages.add(imageBean);

                }
            }

            mHandler.sendEmptyMessage(0x110);
        }
    }
}
