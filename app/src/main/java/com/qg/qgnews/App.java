package com.qg.qgnews;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

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
    public static Context context;

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
                mActivityStack.pop();
            }
        });
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        setActivityLifecycleCallbacks();
    }
}
