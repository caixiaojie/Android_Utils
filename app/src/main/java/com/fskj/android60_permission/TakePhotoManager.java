package com.fskj.android60_permission;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;


import kr.co.namee.permissiongen.internal.Utils;

/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class TakePhotoManager implements View.OnClickListener/*,GrantPermissionActivity.OnGrantedListener*/{
    /**
     * 权限列表:写权限 读权限 调用摄像头权限
     */
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    /* 上下文 */
    private Activity context;

    /* 对话框 */
    private Dialog mSelectPhotoDialog;

    /* 图片的存储地址-父路径 */
    private String mHeadImageFileParentPath = "";

    /* 照相机拍照后的照片 */
    private String mCameraImageFileName = "";

    /* 头像名称 */
    private String mHeadImageFileName = "";

    private Button mBtnTakePhoto;//拍照
    private Button mBtnPickPhoto;//选择本地图片
    private Button mBtnCancel;//取消

    private Uri imageUri;//原图保存地址


    public TakePhotoManager(Activity context) {
        this.context = context;
        mHeadImageFileParentPath = Environment.getExternalStorageDirectory() + File.separator+ context.getPackageName()+File.separator;
        mCameraImageFileName = "temp_camera_image.jpg";
        mHeadImageFileName = "tmp_head_image.jpg";
    }

    public void setmHeadImageFileParentPath(String mHeadImageFileParentPath) {
        this.mHeadImageFileParentPath = mHeadImageFileParentPath;
    }

    public void setmCameraImageFileName(String mCameraImageFileName) {
        this.mCameraImageFileName = mCameraImageFileName;
    }

    public void setmHeadImageFileName(String mHeadImageFileName) {
        this.mHeadImageFileName = mHeadImageFileName;
    }

    /**
     * 显示头像弹出窗
     */
   /* public void showSelectPhotoDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//23
            //Android6.0及以上，需要动态授予权限
            // 验证所有权限是否都已经授权了
            List<String> deniedPermissions = Utils.findDeniedPermissions(this.context, PERMISSIONS);
            if (deniedPermissions.size() == 0) {
                showDialog();
            } else {
                Intent intent = new Intent(this.context, GrantPermissionActivity.class);
                intent.putExtra(GrantPermissionActivity.PARAM_PERMISSION_NAME_LIST, PERMISSIONS);
                GrantPermissionActivity.mGrantedListener = this;
                this.context.startActivity(intent);
            }
        }else{
            showDialog();
        }
    }*/
/*
    private void showDialog(){
        View view = context.getLayoutInflater().inflate(R.layout.dlg_select_photo,
                null);
        mBtnTakePhoto = (Button) view.findViewById(R.id.btn_take_photo);
        mBtnPickPhoto = (Button) view.findViewById(R.id.btn_pick_photo);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnTakePhoto.setOnClickListener(this);
        mBtnPickPhoto.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mSelectPhotoDialog = UploadAvatarUtil.createDialog(context, view,
                R.style.transparentFrameWindowStyle, R.style.select_photo_dialog_animstyle);
    }*/

    /**
     * 关闭头像弹出窗
     */
    public void hideSelectPhotoDialog() {
        if (mSelectPhotoDialog != null && mSelectPhotoDialog.isShowing()) {
            mSelectPhotoDialog.dismiss();
        }
    }

    /**
     * 拍照
     */
    public void takePhoto(Activity context, String imgUrl, String imageFileNameTemp) {
        File file = UploadAvatarUtil.getFile(imgUrl, imageFileNameTemp);
        // 打开相机
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //FileUriExposedException这个异常只会在Android 7.0 + 出现，当app使用file:// url 共享给其他app时， 会抛出这个异常。
            //因为在android 6.0 + 权限需要 在运行时候检查， 其他app 可能没有读写文件的权限， 所以google在7.0的时候加上了这个限制。官方推荐使用 FileProvider 解决这个问题。
            intentFromCapture.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION ); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            imageUri = FileProvider.getUriForFile(context, "com.hb.weex.accountant.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        context.startActivityForResult(intentFromCapture, UploadAvatarUtil.CAMERA_REQUEST_CODE);
    }

    /**
     * 选择本地图片
     */
    public void pickPhoto(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(intent, UploadAvatarUtil.IMAGE_REQUEST_CODE);
    }

    public Object onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        //关闭头像弹出窗
        hideSelectPhotoDialog();
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case UploadAvatarUtil.IMAGE_REQUEST_CODE:// 选择本地图片返回
                    if (null != data) {//为了取消选取不报空指针用的
                        imageUri = data.getData();
                        startPhotoZoom(context, imageUri , 1);
                    }
                    break;
                case UploadAvatarUtil.CAMERA_REQUEST_CODE:// 拍照返回
                    if (UploadAvatarUtil.hasSdcard()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//24
                            startPhotoZoom(context, imageUri, 0);
                        }else{
                            File tempFile = getFileTmp();
                            startPhotoZoom(context, Uri.fromFile(tempFile), 0);
                        }
                    } else {
                        Toast.makeText(context,"没有找到内存卡",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case UploadAvatarUtil.CLIP_REQUEST_CODE:// 裁剪完成,删除照相机缓存的图片
                    Bitmap imageBitmap = null;
                    //获取头像文件
                    File file = getTmpHeadImage();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){//24
                        try {
                            imageBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else {
                        if (!file.exists()) {
                            return null;
                        }
                        imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    }
                    if (imageBitmap == null) {
                        return null;
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] userImageData = baos.toByteArray();
                    //修改个人头像
                    HashMap<String, Object> result = new HashMap<String, Object>();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        result.put("path", file.getAbsolutePath());
                    }
                    result.put("data",userImageData);
                    return result;
            }
        }
        return null;
    }

    /**
     * 裁剪图片
     *
     * @param uri from 1代表本地照片的裁剪，0代表拍照后的裁剪
     */
    public void startPhotoZoom(Context context, Uri uri, int from) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        String url = convertPath(context, uri);
        /**
         * 当满足：SDK>4.0、选择本地照片操作、裁剪框为圆形（或者说路径与之前版本有异）三个条件时
         * 进行转换操作
         */
        if (Build.VERSION.SDK_INT >= 19 && from == 1 && url != null) {
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        Uri uriPath;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {//小于24
            uriPath = Uri.fromFile(UploadAvatarUtil.getFile(mHeadImageFileParentPath, mHeadImageFileName));
        }else{
            uriPath = imageUri;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        //intent.putExtra("return-data", true);
        //return-data为true时，会直接返回bitmap数据，但是大图裁剪时会出现问题。
        //设置为false比较好，但是，需要指定MediaStore.EXTRA_OUTPUT，来保存裁剪之后的图片
        ((Activity)context).startActivityForResult(intent, UploadAvatarUtil.CLIP_REQUEST_CODE);
    }


    /**
     * 删除缓存的头像
     */
    public void deleteHeadCache() {
        UploadAvatarUtil.deleteLocalImage(mHeadImageFileParentPath, mHeadImageFileName);
    }

    /**
     * 获取文件对象---照相机拍照的
     *
     * @return
     */
    public File getFileTmp() {
        return UploadAvatarUtil.getFile(mHeadImageFileParentPath, mCameraImageFileName);
    }


    /**
     * 获取裁剪之后的图片文件
     */
    public File getTmpHeadImage(){
        return UploadAvatarUtil.getFile(mHeadImageFileParentPath,mHeadImageFileName);
    }

    /**
     * 删除照相机拍照的图片
     */
    public void deleteCameraImage() {
        UploadAvatarUtil.deleteLocalImage(mHeadImageFileParentPath, mCameraImageFileName);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                takePhoto(context, mHeadImageFileParentPath, mCameraImageFileName);
                break;
            case R.id.btn_pick_photo:
                pickPhoto(context);
                break;
            case R.id.btn_cancel:
                hideSelectPhotoDialog();
                break;
        }
    }


    /**
     * 将uri转换成字符串
     * 解决4.4版本以上获取到的uri是图片名称而非图片路径，导致剪裁图片时提示无法加载图片的问题
     * 详细的解决方案，请参考这篇文章
     * 当安卓的版本比较高时（如4.4），选择本地相册可能会返回“无法加载此图片”
     * 原因：正常uri是file：//...而高版本是content：//...
     * 所以需要一个转换操作
     *
     * @param context
     * @param uri
     * @return
     */
    public String convertPath(final Context context, final Uri uri) {
        //代表SDK4.4需要反射的类
        Class getClass = null;
        //代表两个SDK4.4里面反射的方法
        Method isMethod;
        Method getMethod;
        String stringMethod = "";


        //判断当前版本是否大于4.0
        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;
        try {
            /**
             * 获取类和方法
             * 方法传参方式
             * 获取返回值
             */
            getClass = Class.forName("android.provider.DocumentsContract");
            getMethod = getClass.getMethod("getDocumentId", new Class[]{Uri.class});
            isMethod = getClass.getMethod("isDocumentUri", new Class[]{Context.class, Uri.class});
            Object isResult = isMethod.invoke(getClass, new Object[]{context, uri});
            Object getResult = getMethod.invoke(getClass, new Object[]{uri});
            stringMethod = (String) getResult;
            if (!(Boolean) isResult)
                return null;
            //当手机SDK大于4.4且路径类型与以往不同时（isShot为True时往往裁剪框是圆形，即是新类型的路径）
            if (isKitKat) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = stringMethod;
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                } else if (isDownloadsDocument(uri)) {
                    final String id = stringMethod;
                    final Uri contentUri = ContentUris.withAppendedId(
                            //高版本的路径不同于低版本，需要转换
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = stringMethod;
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return null;
    }


    //获取String类型的路径
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

  /*  @Override
    public void onGrantSuccess() {
        showDialog();
    }*/
}
