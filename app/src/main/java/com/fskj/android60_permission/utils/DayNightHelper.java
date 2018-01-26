package com.fskj.android60_permission.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import com.fskj.android60_permission.R;

/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class DayNightHelper {
    public final static String MODE = AnConstants.KEY.Extra_daynight;

    private SharedPreferences mSharedPreferences;

    public DayNightHelper(SharedPreferences sp) {
        this.mSharedPreferences = sp;
    }

    /**
     * 保存模式设置
     *
     * @param mode
     * @return
     */
    public boolean setMode(ResponseDayNightModel mode) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(MODE, mode.getName());
        return editor.commit();
    }

    /**
     * 夜间模式
     *
     * @return
     */
    public boolean isNight() {
        String mode = mSharedPreferences.getString(MODE, ResponseDayNightModel.DAY.getName());
        if (ResponseDayNightModel.NIGHT.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 日间模式
     *
     * @return
     */
    public boolean isDay() {
        String mode = mSharedPreferences.getString(MODE, ResponseDayNightModel.DAY.getName());
        if (ResponseDayNightModel.DAY.getName().equals(mode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 刷新 StatusBar
     */
    public static void refreshStatusBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = activity.getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(typedValue.resourceId));
        }
    }
    /**
     * 设置夜间模式
     */
    public static void setNightTheme(@NonNull Activity activity) {
        activity.setTheme(R.style.NightTheme);
    }
    /**
     * 设置白天模式
     */
    public static void setDayTheme(@NonNull Activity activity) {
        activity.setTheme(R.style.DayTheme);
    }
}
