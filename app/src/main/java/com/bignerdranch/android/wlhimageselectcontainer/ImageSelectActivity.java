package com.bignerdranch.android.wlhimageselectcontainer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bignerdranch.android.wlhimageselectcontainer.adapter.MyAdapter;
import com.bignerdranch.android.wlhimageselectcontainer.adapter.SpaceItemDecoration;
import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;
import com.bignerdranch.android.wlhimageselectcontainer.click.OnChangeListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageSelectActivity extends BaseActivity implements OnChangeListener {
    private static final String TAG = "ImageSelectActivity";

    private Button mButtonConfirm;

    private RecyclerView mRecyclerView;
    private MyThread mThread;
    private MyAdapter adapter;

    /**
     * 所有相册要显示的的图片
     */
    private List<ImageBean> mImages = new ArrayList<>();
    //监听回调时选择的图片


    private final int MAX_IMAGE = 9;

    //收到异步下载完成信息
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

        mButtonConfirm = (Button) findViewById(R.id.button_confirm);
        mButtonConfirm.setText("确定" + "0/" + MAX_IMAGE);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectImages.size() > 0) {
                    //一个新的intent
                    Intent intent = new Intent();
                    //intent中放入数据
                    intent.putParcelableArrayListExtra("selectImages", mSelectImages);
                    //设置返回结果
                    setResult(Activity.RESULT_OK, intent);
                }
                //activity结束
                finish();
            }
        });

        getImageList();
    }

    private void setData() {
        adapter = new MyAdapter(this, mImages, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(5));
    }

    private void getImageList() {
        mThread = new MyThread();
        mThread.start();
    }

    //监听选择了的图片
    @Override
    public void onChangeListener(int position, boolean isCheck) {
        ImageBean image = mImages.get(position);
        //选中放图片
        if (isCheck) {
            //adapter的重新设置会触发监听事件
            if (!mSelectImages.contains(image)) {
                image.setSelect(true);
                mSelectImages.add(image);
                mButtonConfirm.setText("确定" + mSelectImages.size() + "/" + MAX_IMAGE);
                //必须放里面不然会一直调用
                if (mSelectImages.size() == MAX_IMAGE) {
                    adapter.notifyData(true);
                }
            }
        } else {
            if (mSelectImages.contains(image)) {
                mSelectImages.remove(image);
                mButtonConfirm.setText("确定" + mSelectImages.size() + "/" + MAX_IMAGE);
                image.setSelect(false);
                //必须放里面不然会一直调用浪费资源
                if (mSelectImages.size() == MAX_IMAGE - 1) {
                    adapter.notifyData(false);
                }
            }
        }
    }

    //展示图片的dialog
    @Override
    public void showImageDialog(String msg) {
        ImageDialogFragment f = ImageDialogFragment.newInstance(msg);
        f.show(getFragmentManager(), "f");
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
