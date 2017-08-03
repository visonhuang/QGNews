package com.qg.qgnews;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.qg.qgnews.controller.adapter.HeartBeatService;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.util.Request;

import java.util.Stack;

/**
 * Created by 小吉哥哥 on 2017/7/26.
 */

public class App extends Application {

    /**
     * 全局活动栈
     */
    private static Stack<Activity> mActivityStack = new Stack<>();
    /**
     * 全局上下文，不能做UI操作
     */
    public static boolean isManager = false;
    public static Context context;
    public static LruCache<Integer, Bitmap> bitmapLruCache;

    public static Stack<Activity> getActivityStack() {
        return mActivityStack;
    }

    /**
     * 活动进站出站
     */
    private void setActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivityStack.push(activity);
                Log.d("==========", activity.toString() + "=================run");
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivityStack.remove(activity);
                Log.d("==========", activity.toString() + "=================dead");
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RequestAdress.HOST = getSharedPreferences("ip", MODE_PRIVATE).getString("ip", "0");
        bitmapLruCache = new LruCache<Integer, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 8) {
            @Override
            protected int sizeOf(Integer key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        context = getApplicationContext();
        setActivityLifecycleCallbacks();
    }
}
