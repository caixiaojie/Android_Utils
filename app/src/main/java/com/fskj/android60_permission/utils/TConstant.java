package com.fskj.android60_permission.utils;

import android.content.Context;
/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class TConstant {
    /**
     * request Code 裁剪照片
     **/
    public final static int RC_CROP = 1001;
    /**
     * request Code 从相机获取照片并裁剪
     **/
    public final static int RC_PICK_PICTURE_FROM_CAPTURE_CROP = 1002;
    /**
     * request Code 从相机获取照片不裁剪
     **/
    public final static int RC_PICK_PICTURE_FROM_CAPTURE = 1003;
    /**
     * request Code 从文件中选择照片
     **/
    public final static int RC_PICK_PICTURE_FROM_DOCUMENTS_ORIGINAL = 1004;
    /**
     * request Code 从文件中选择照片并裁剪
     **/
    public final static int RC_PICK_PICTURE_FROM_DOCUMENTS_CROP = 1005;
    /**
     * request Code 从相册选择照
     **/
    public final static int RC_PICK_PICTURE_FROM_GALLERY_ORIGINAL = 1006;
    /**
     * request Code 从相册选择照片并裁剪
     **/
    public final static int RC_PICK_PICTURE_FROM_GALLERY_CROP = 1007;
    /**
     * request Code 选择多张照片
     **/
    public final static int RC_PICK_MULTIPLE = 1008;


    /**
     * requestCode 请求权限
     **/
    public final static int PERMISSION_REQUEST_TAKE_PHOTO = 2000;

    public final static String getFileProviderName(Context context){
        return context.getPackageName()+".fileprovider";
    }
}
