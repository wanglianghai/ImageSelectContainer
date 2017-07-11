package com.bignerdranch.android.wlhimageselectcontainer;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bignerdranch.android.wlhimageselectcontainer.bean.ImageBean;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button mSelectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MPermissionUtils.requestPermissionsResult(MainActivity.this, 1,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE});

        mSelectButton = (Button) findViewById(R.id.select_picture);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        switch (requestCode) {
            case 0:
                //bundle n:束，捆 从intent中得到数据
                Bundle bundle = data.getExtras();
                //从bundle中具体化需要的数据
                ArrayList<ImageBean> imageBeen = bundle.getParcelableArrayList("selectImages");
                Log.i(TAG, "onActivityResult: " + imageBeen.size());
                break;
        }
    }
}
