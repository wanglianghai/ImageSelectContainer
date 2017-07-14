package com.bignerdranch.android.wlhimageselectcontainer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;
import com.bignerdranch.android.wlhimageselectcontainer.weight.NineImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private NineImageView mNineImageView;
    private Button mSelectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MPermissionUtils.requestPermissionsResult(MainActivity.this, 1,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE});

        mNineImageView = (NineImageView) findViewById(R.id.nine_view_group);

        mSelectButton = (Button) findViewById(R.id.select_picture);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNineImageView.removeAllViews();
                //this 表示对当前对象的引用
                Intent intent = new Intent(MainActivity.this , ImageSelectActivity.class);
                //启动两次activity按钮要按两次
                //startActivity(intent);
                //intent放回的结果
                startActivityForResult(intent, 0);
            }
        });
    }

    //放回消息的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //结果是Ok处理
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 0:
                    //bundle n:束，捆 从intent中得到数据
                    Bundle bundle = data.getExtras();
                    //从bundle中具体化需要的数据
                    ArrayList<ImageBean> imageBeen = bundle.getParcelableArrayList("selectImages");
                    for (ImageBean i: imageBeen) {
                        //视图填充在那个view group中
                        ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.nine_image, mNineImageView, false);
                        //抓取图片
                        Glide.with(MainActivity.this).load(i.getPath()).into(imageView);
                        //view group中放入图片
                        mNineImageView.addView(imageView);
                    }
                    break;
            }
        }
    }
}
