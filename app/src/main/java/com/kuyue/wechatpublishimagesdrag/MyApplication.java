package com.kuyue.wechatpublishimagesdrag;

import android.app.Application;
import android.content.Context;

/**
 * 自定义 Application
 * Created by kuyue on 2017/7/12 上午10:26.
 * 邮箱:595327086@qq.com
 */

public class MyApplication extends Application {

    private static MyApplication app;
    private Context mContext;

    public static MyApplication getInstance() {
        return app;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mContext = getApplicationContext();
    }
}
