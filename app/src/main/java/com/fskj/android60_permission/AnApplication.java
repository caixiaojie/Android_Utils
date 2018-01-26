package com.fskj.android60_permission;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;

import com.fskj.android60_permission.utils.LaStorageFile;

/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class AnApplication extends Application {
    private static AnApplication instance;
    public static SharedPreferences sp;


    public static AnApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            sp = LaStorageFile.INSTANCE.getSharedPreferences(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sp = LaStorageFile.INSTANCE.getDefaultSharedPreferences(this);
        }
    }
}
