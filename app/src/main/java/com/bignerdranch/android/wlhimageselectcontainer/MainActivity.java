package com.bignerdranch.android.wlhimageselectcontainer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mSelectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelectButton = (Button) findViewById(R.id.select_picture);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this 表示对当前对象的引用
                startActivity(new Intent(MainActivity.this , ImageSelectActivity.class));
            }
        });
    }
}
