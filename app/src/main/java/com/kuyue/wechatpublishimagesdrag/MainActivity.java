package com.kuyue.wechatpublishimagesdrag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 主 Activity
 * Created by kuyue on 2017/7/13 上午10:19.
 * 邮箱:595327086@qq.com
 **/
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiImageSelector.create()
                        .showCamera(true) // show camera or not. true by default
                        .count(9) // max select image size, 9 by default. used width #.multi()
                        .multi() // multi mode, default mode;
                        .start(MainActivity.this, REQUEST_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {//文章图片
            PostImagesActivity.startPostActivity(MainActivity.this,
                    data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
        }
    }
}
