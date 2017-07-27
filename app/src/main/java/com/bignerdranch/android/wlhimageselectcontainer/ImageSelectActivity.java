package com.bignerdranch.android.wlhimageselectcontainer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bignerdranch.android.wlhimageselectcontainer.adapter.ImageDirSelectAdapter;
import com.bignerdranch.android.wlhimageselectcontainer.adapter.MyAdapterDelete;
import com.bignerdranch.android.wlhimageselectcontainer.adapter.SpaceItemDecoration;
import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;
import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageDirBean;
import com.bignerdranch.android.wlhimageselectcontainer.click.OnChangeListener;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ImageSelectActivity extends BaseActivity implements OnChangeListener{
    private static final String TAG = "ImageSelectActivity";

    private Button mButtonConfirm;
    private TextView mTextViewDir;
    private PopupWindow dirPopupWindows;

    private RecyclerView mRecyclerView;
    private MyThread mThread;
    private MyAdapterDelete adapter;
    private ImageDirSelectAdapter dirAdapter;

    /**
     * 所有相册要显示的的图片
     */
    private List<ImageBean> mImages = new ArrayList<>();
    private List<ImageDirBean> mDirBeen = new ArrayList<>();
    //防止重复进入，hash效率很高
    private HashSet<String> mDirHashSet = new HashSet<>();


    private final int MAX_IMAGE = 9;

    //收到异步下载完成信息
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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

        mTextViewDir = (TextView) findViewById(R.id.text_image_dir);
        mTextViewDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindows();
            }
        });

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

    private void showPopupWindows() {
        //设置显示的视图
        View view = LayoutInflater.from(this).inflate(R.layout.image_dir_list, null);
        setPopupWindowsView(view);
        setPopupWindows(view);
    }

    private void setPopupWindowsView(View view) {
        RecyclerView r = (RecyclerView) view.findViewById(R.id.recycler_view_dir_list);
        dirAdapter = new ImageDirSelectAdapter(mDirBeen);
        r.setAdapter(dirAdapter);
        r.setLayoutManager(new LinearLayoutManager(this));
        dirAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        dirAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectDirPath(mDirBeen.get(position));
            }
        });
    }

    //设置弹出的popup windows
    private void setPopupWindows(View view) {
        int screenWidth;
        int screenHeight;
        //获取屏幕的宽和高来设置popWindow的宽和高
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        //设置popWindow的宽和高
        dirPopupWindows = new PopupWindow(this);
        dirPopupWindows.setWidth(screenWidth);
        dirPopupWindows.setHeight((int) (screenHeight * 0.7));
        //设置popWindow的视图
        dirPopupWindows.setContentView(view);
        //设置点击屏幕外的popWindow会dismiss（消失）
        dirPopupWindows.setOutsideTouchable(true);
        dirPopupWindows.setFocusable(true);
        //设置消失后的行为
        dirPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowsAlpha(1.0f);
            }
        });
        dirPopupWindows.setAnimationStyle(R.style.contextMenuAnim);
        dirPopupWindows.showAsDropDown(mTextViewDir);

        setWindowsAlpha(0.5f);
    }

    //设置窗口的透明度属性值
    private void setWindowsAlpha(float f) {
        WindowManager.LayoutParams lp;
        lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    private void setData() {
        adapter = new MyAdapterDelete(this, mImages, this);
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
        Log.i(TAG, "onChangeListener: " + image);
        //选中放图片
        if (isCheck) {
            //adapter的重新设置会触发监听事件
            //if (!mSelectImages.contains(image)) {序列化后里面的东西不一样
            if (!contains(mSelectImages, image)){
                Log.i(TAG, "onChangeListener: in " + image);
                image.setSelect(true);
                mSelectImages.add(image);
                mButtonConfirm.setText("确定" + mSelectImages.size() + "/" + MAX_IMAGE);
                //必须放里面不然会一直调用
                if (mSelectImages.size() == MAX_IMAGE) {
                    adapter.notifyData(true);
                }
            }
        } else {
            //if (!mSelectImages.contains(image)) {序列化后里面的东西不一样
            if (!contains(mSelectImages, image)) {
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

    private boolean contains(List<ImageBean> imageBeen, ImageBean bean) {
        for (ImageBean i: imageBeen) {
            if (i.getPath().equals(bean.getPath())) {
                return true;
            }
        }

        return false;
    }

    //展示图片的dialog
    @Override
    public void showImageDialog(String msg) {
        ImageDialogFragment f = ImageDialogFragment.newInstance(msg);
        f.show(getFragmentManager(), "f");
    }

    private Cursor getCursor() {
        //获取内存卡路径Uri
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //通过内容解析器解析出png和jpeg格式的图片第3个参数是where第四个死where后的值
        ContentResolver mContentResolver = getContentResolver();
        return mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        +MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/png", "image/jpeg"},
                MediaStore.Images.Media.DATE_ADDED);
    }

    private void inImages(String path) {
        ImageBean imageBean = new ImageBean();
        imageBean.setPath(path);
        mImages.add(imageBean);
    }

    private void inDirPath(String path) {
        File parentAbstractFile = new File(path).getParentFile();
        String parentAbsoluteFile = parentAbstractFile.getAbsolutePath();
        if (mDirHashSet.contains(parentAbsoluteFile)) {
            return;
        }
        ImageDirBean dirBean = new ImageDirBean();
        dirBean.setParentFile(parentAbsoluteFile);
        mDirHashSet.add(parentAbsoluteFile);
        //分隔得到图片文件夹的名字
        String[] pathSplit = parentAbsoluteFile.split("/");
        parentAbsoluteFile = pathSplit[pathSplit.length - 1];

        dirBean.setFirstImagePath(path);
        dirBean.setFileName(parentAbsoluteFile);
        int pictureSize = parentAbstractFile.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")) {
                    return true;
                }
                return false;
            }
        }).length;
        dirBean.setImageSize(pictureSize);
        mDirBeen.add(dirBean);
    }

    //监听选择
    public void selectDirPath(ImageDirBean dirBean) {
        mImages.clear();
        mImages.add(null);
        String parentPath = dirBean.getParentFile();
        File fileParent = new File(parentPath);
        //解析出每一个的子文件名字
        List<String> imagePath = Arrays.asList(fileParent.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")) {
                    return true;
                }
                return false;
            }
        }));
        for (String pathName: imagePath) {
            ImageBean image = new ImageBean();
            image.setPath(parentPath + "/" + pathName);
            image.setSelect(false);
            for (ImageBean selectBean : mSelectImages) {
                if (image.getPath().equals(selectBean.getPath())) {
                    image.setSelect(true);
                }
            }
            mImages.add(image);
        }
        adapter.notifyDataSetChanged();
        mTextViewDir.setText(dirBean.getFileName());
        dirPopupWindows.dismiss();
    }

    //异步线程寻找图片位置
    private class MyThread extends Thread{
        @Override
        public void run() {
            super.run();

            //  用cursor读app外部的图片地址
            Cursor mCursor = getCursor();
            //判断是否存在图片
            if (mCursor.getCount() > 0) {
                //第一个放自己的图片
                mImages.add(new ImageBean());
                //移动cursor
                while (mCursor.moveToNext()) {
                    // 获取该图片的文件名
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    inImages(path);
                    inDirPath(path);
                }
            }
            mCursor.close();

            mHandler.sendEmptyMessage(0x111);
        }
    }
}
