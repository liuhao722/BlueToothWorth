package com.worth.bluetooth;

import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Author:  LiuHao
 * Email:   114650501@qq.com
 * TIME:    6/6/21 --> 12:44 PM
 * Description: This is MyApplication
 */
public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.openDebug();
        ARouter.openLog();
        ARouter.init(this);
    }
}
