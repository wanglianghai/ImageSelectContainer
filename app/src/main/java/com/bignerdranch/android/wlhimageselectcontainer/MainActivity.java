package com.bignerdranch.android.wlhimageselectcontainer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;
import com.bignerdranch.android.wlhimageselectcontainer.uri.UriUnit;
import com.bignerdranch.android.wlhimageselectcontainer.weight.NineImageView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //写这里不然忘记request code写几
    private static final int REQUEST_SELECT = 0;
    private static final int REQUEST_PHOTO = 1;

    public static MainActivity sMainActivity;
    private NineImageView mNineImageView;
    private Button mSelectButton;
    private ImageButton mCameraButton;
    private Intent mCaptureIntent;

    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sMainActivity = this;
        MPermissionUtils.requestPermissionsResult(MainActivity.this, 1,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE});

        mNineImageView = (NineImageView) findViewById(R.id.nine_view_group);

        mCameraButton = (ImageButton) findViewById(R.id.button_camera);
        //一个隐式的抓取图片的intent capture抓取
        mCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offCamera();
            }
        });

        mSelectButton = (Button) findViewById(R.id.select_picture);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this 表示对当前对象的引用
                Intent intent = new Intent(MainActivity.this , ImageSelectActivity.class);
                //启动两次activity按钮要按两次
                //startActivity(intent);
                //intent放回的结果
                startActivityForResult(intent, REQUEST_SELECT);
            }
        });
    }

    public void offCamera() {
        UriUnit.setPhotoFileUri(this);
        photoUri = UriUnit.getPhotoFileUri();
        //intent中放键值对给启动的activity寻找
        mCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        //询问这个intent可以启动的activity
        List<ResolveInfo> captureActivities = MainActivity.this.getPackageManager()
                .queryIntentActivities(mCaptureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo activity:captureActivities) {
            MainActivity.this.grantUriPermission(activity.activityInfo.packageName,
                    photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(mCaptureIntent, REQUEST_PHOTO);
    }

    //放回消息的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //结果是Ok处理
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT:
                    mNineImageView.removeAllViews();
                    //bundle n:束，捆 从intent中得到数据
                    Bundle bundle = data.getExtras();
                    //从bundle中具体化需要的数据
                    ArrayList<ImageBean> imageBeen = bundle.getParcelableArrayList("selectImages");
                    for (ImageBean i: imageBeen) {
                        //视图填充在那个view group中
                        ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.nine_image, mNineImageView, false);
                        //抓取图片
                        Glide.with(MainActivity.this).load(i.getPath()).into(imageView);
                        //view group中放入图片,要清空不然会一直加进来
                        mNineImageView.addView(imageView);
                    }
                    break;

                case REQUEST_PHOTO:
                    //注销这个uri权限
                    this.revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            }
        }
    }
}
