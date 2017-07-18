package com.bignerdranch.android.wlhimageselectcontainer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;
import com.bignerdranch.android.wlhimageselectcontainer.uri.UriUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18/018.
 */

public class BaseActivity extends AppCompatActivity {
    private Intent mCaptureIntent;
    private static final int REQUEST_PHOTO = 1;
    public ArrayList<ImageBean> mSelectImages = new ArrayList<>();

    private Uri photoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    public void offCamera(Activity activityCamera) {
        if (activityCamera instanceof ImageSelectActivity) {
            activityCamera.finish();
        }
        UriUnit.setPhotoFileUri(this);
        photoUri = UriUnit.getPhotoFileUri();
        //intent中放键值对给启动的activity寻找
        mCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        //询问这个intent可以启动的activity
        List<ResolveInfo> captureActivities = BaseActivity.this.getPackageManager()
                .queryIntentActivities(mCaptureIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo activity:captureActivities) {
            BaseActivity.this.grantUriPermission(activity.activityInfo.packageName,
                    photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(mCaptureIntent, REQUEST_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_PHOTO:
                //注销这个uri权限
                this.revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                ImageBean imageBean = new ImageBean();
                imageBean.setPath(UriUnit.getFileString().toString());
                mSelectImages.add(imageBean);
                break;
        }
    }
}
