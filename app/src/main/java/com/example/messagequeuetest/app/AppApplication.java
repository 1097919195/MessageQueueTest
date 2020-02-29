package com.example.messagequeuetest.app;


import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class AppApplication extends Application {

    private static AppApplication baseApplication;

    @Override
    public void onCreate() {
        //程序创建的时候执行
        super.onCreate();
        baseApplication = this;
    }

    public static Context getAppContext() {
        return baseApplication;
    }
    public static Resources getAppResources() {
        return baseApplication.getResources();
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行,onLowMemory()低内存的时候执行,onTrimMemory(int level)程序在内存清理的时候执行
        Log.e("onTerminate======", "onTerminate");
        super.onTerminate();
    }

    /**
     * 分包（自动）
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }
}


