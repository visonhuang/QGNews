package com.qg.qgnews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.model.Manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by 小吉哥哥 on 2017/7/26.
 */

public class Tool {
    private static Toast mToast;
    private static Object mObject;

    /**
     * @param o 需要toast对象
     */
    public static void toast(final Object o) {
        App.getActivityStack().lastElement().runOnUiThread(new Runnable() {
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

    /**
     * 在主线程中奔跑
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        App.getActivityStack().lastElement().runOnUiThread(runnable);
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

    public static void setCurrentManager(Manager manager) {
        SharedPreferences.Editor editor = App.context.getSharedPreferences("Manager", Context.MODE_PRIVATE).edit();
        editor.putInt("managerId", manager.getManagerId());
        editor.putInt("managerSuper", manager.getManagerSuper());
        editor.putString("managerAccount", manager.getManagerAccount());
        editor.putString("managerPassword", manager.getManagerPassword());
        editor.putString("managerName", manager.getManagerName());
        editor.apply();
    }

    public static Manager getCurrentManager() {
        SharedPreferences sp = App.context.getSharedPreferences("Manager", Context.MODE_PRIVATE);
        Manager m = new Manager();
        m.setManagerId(sp.getInt("managerId", -1));
        m.setManagerSuper(sp.getInt("managerSuper", -1));
        m.setManagerAccount(sp.getString("managerAccount", ""));
        m.setManagerPassword(sp.getString("managerPassword", ""));
        m.setManagerName(sp.getString("managerName", ""));
        return m;
    }

    public static String getUUID() {
        SharedPreferences sp = App.context.getSharedPreferences("UUID", Context.MODE_PRIVATE);
        String newId = UUID.randomUUID().toString();
        String retrunBack = sp.getString("UUID", newId);

        if (newId.equals(retrunBack)) {
            SharedPreferences.Editor editor = App.context.getSharedPreferences("UUID", Context.MODE_PRIVATE).edit();
            editor.putString("UUID", newId);
            editor.apply();
        }
        return retrunBack;
    }

    public static String saveRandomUUID() {
        SharedPreferences.Editor editor = App.context.getSharedPreferences("UUID", Context.MODE_PRIVATE).edit();
        String newId = UUID.randomUUID().toString();
        editor.putString("UUID", newId);
        editor.apply();
        return newId;
    }

    public static void setFileSavePath(String path) {
        SharedPreferences.Editor editor = App.context.getSharedPreferences("path", Context.MODE_PRIVATE).edit();
        editor.putString("path", path);
        editor.apply();
    }

    public static String getFileSavePath() {
        SharedPreferences sp = App.context.getSharedPreferences("path", Context.MODE_PRIVATE);
        File file = new File(sp.getString("path", Environment.getExternalStorageDirectory().getPath()) + "//" + "QgNewsDownload");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    /**
     * 把Bitmap转ByteArrayInputStream
     */
    public static ByteArrayInputStream Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return bais;
    }

    public static boolean isEmail(String email) {

        return email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    }

    public static int getFileIcon(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file 不能为空");
        }
        if (file.isDirectory()) {
            return R.drawable.ic_folder;
        }
        String[] fileInfo = file.toString().split("\\.");
        if (fileInfo.length == 2 && fileInfo[1].equals("mp3")) {
            return R.drawable.ic_mp3_two;
        } else if (fileInfo.length == 2 && fileInfo[1].equals("txt")) {
            return R.drawable.ic_txt;
        } else if (fileInfo.length == 2 && fileInfo[1].equals("dat")) {
            return R.drawable.ic_dat;
        } else if (fileInfo.length == 2 && fileInfo[1].equals("rmvb")) {
            return R.drawable.ic_rmvb;
        } else if (fileInfo.length == 2 && fileInfo[1].equals("apk")) {
            return R.drawable.ic_apk;
        } else {
            return R.drawable.ic_unknow;
        }
    }

    public static int getFileIcon(String file) {
        if (file == null) {
            throw new IllegalArgumentException("filename 不能为空");
        }

        String[] fileInfo = file.toString().split("\\.");
        if (fileInfo.length == 2 && fileInfo[1].equals("mp3")) {
            return R.drawable.ic_mp3_two;
        } else if (fileInfo.length == 2 && fileInfo[1].equals("txt")) {
            return R.drawable.ic_txt;
        } else if (fileInfo.length == 2 && fileInfo[1].equals("dat")) {
            return R.drawable.ic_dat;
        } else if (fileInfo.length == 2 && fileInfo[1].equals("rmvb")) {
            return R.drawable.ic_rmvb;
        } else if (fileInfo.length == 2 && fileInfo[1].equals("apk")) {
            return R.drawable.ic_apk;
        } else {
            return R.drawable.ic_unknow;
        }
    }

    /**
     * @param pathName  图片路径
     * @param reqWidth  控件的宽
     * @param reqHeight 控件的高
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return src;
    }

    /**
     * @param options   源图片属性
     * @param reqWidth  控件的宽
     * @param reqHeight 控件的高
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //判断是否为图片
    public static boolean isPicture(String imagePath) {
        String[] imageType = {"png", "jpeg", "bmp", "gif"};
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        String type = options.outMimeType.substring(6);
        for (String t : imageType) {
            if (t.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFirstRun() {
        SharedPreferences preferences = App.context.getSharedPreferences("run", Context.MODE_PRIVATE);
        if (preferences.getBoolean("isFirstRun", true)) {
            SharedPreferences.Editor editor = App.context.getSharedPreferences("run", Context.MODE_PRIVATE).edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
            return true;
        } else {
            return false;
        }
    }
}
