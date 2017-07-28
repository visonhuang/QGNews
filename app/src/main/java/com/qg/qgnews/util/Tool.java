package com.qg.qgnews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.qg.qgnews.App;
import com.qg.qgnews.model.Manager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 小吉哥哥 on 2017/7/26.
 */

public class Tool {
    private static Toast mToast;
    private static Object mObject;

    /**
     * @param o 需要toast的对象
     */
    public static void toast(final Object o) {
        App.getActivityStack().firstElement().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mToast || !o.toString().equals(mObject.toString())) {
                    if (null == mToast) {
                        mToast = Toast.makeText(App.context, o.toString(), Toast.LENGTH_SHORT);
                    } else {
                        mToast.setText(o.toString());
                    }
                    mObject = o;
                }
                mToast.show();
            }
        });
    }

    /**
     * 检查 text 中是否按顺序包含 target
     *
     * @param text
     * @param target
     * @return 包含返回true
     */
    public static boolean isOrderContain(String text, String target) {
        if (text == null || target == null) {
            return false;
        }
        char[] targetArray = target.toCharArray();
        String textCopy = new String(text.toCharArray());
        for (int i = 0; i < targetArray.length; i++) {
            if (textCopy.indexOf(targetArray[i]) == -1) {
                return false;
            }
            if ((textCopy = textCopy.substring(textCopy.indexOf(targetArray[i]) + 1)).equals("") && i != targetArray.length - 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return wifi可用返回 true
     */
    public static boolean isWifiOk() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    /**
     * @return 移动网络可用返回 true
     */
    public static boolean isMobileOk() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    /** 在主线程中奔跑
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        App.getActivityStack().firstElement().runOnUiThread(runnable);
    }

    /**
     * 进行MD5加密
     *
     * @param plainText 待加密的字符串
     * @return 加密后的字符串
     */
    public static String encryption(String plainText) {
        byte[] bytes = null;
        try {
            bytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, bytes).toString(16); // 把加密后的数组用16进制表示
    }
    public static void setCurrentManager(Manager manager){
        SharedPreferences.Editor editor = App.context.getSharedPreferences("Manager",Context.MODE_PRIVATE).edit();
        editor.putInt("managerId",manager.getManagerId());
        editor.putInt("managerSuper",manager.getManagerSuper());
        editor.putString("managerAccount",manager.getManagerAccount());
        editor.putString("managerPassword",manager.getManagerPassword());
        editor.putString("managerName",manager.getManagerName());
        editor.apply();
    }
    public static Manager getCurrentManager(){
        SharedPreferences sp = App.context.getSharedPreferences("Manager",Context.MODE_PRIVATE);
        Manager m = new Manager();
        m.setManagerId(sp.getInt("managerId",-1));
        m.setManagerSuper(sp.getInt("managerSuper",-1));
        m.setManagerName(sp.getString("managerAccount",""));
        m.setManagerPassword(sp.getString("managerPassword",""));
        m.setManagerName(sp.getString("managerName",""));
        return m;
    }

}
