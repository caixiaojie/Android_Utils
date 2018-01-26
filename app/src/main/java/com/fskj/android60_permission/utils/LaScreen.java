package com.fskj.android60_permission.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class LaScreen {
    public static int height;
    public static int width;
    private Context context;

    private static LaScreen instance;

    private LaScreen(Context context) {
        this.context = context;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    public static LaScreen getInstance(Context context) {
        if (instance == null) {
            instance = new LaScreen(context);
        }
        return instance;
    }


    /**
     * 得到手机屏幕的宽度, pix单位
     */
    public int getScreenWidth() {
        return width;
    }

    /**
     * 得到手机屏幕的宽度, pix单位
     */
    public int getScreenHeight() {
        return height;
    }
}
