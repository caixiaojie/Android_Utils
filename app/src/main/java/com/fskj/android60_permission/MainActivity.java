package com.fskj.android60_permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int PERMISSION_REQUEST_CALL_PHONE = 1;
    private Intent intent;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.img);
    }

    public void photo(View view) {

            MaterialDialog.Builder dialog = new MaterialDialog.Builder(MainActivity.this);
            dialog.positiveText("从相册选择")
                    .title("上传图片")
                    .negativeText("拍照")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, CameraGallaryUtil.PHOTO_REQUEST_GALLERY);
                    }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                            // 利用系统自带的相机应用:拍照
                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null
                            // 如果此处指定，则后来的data为null
                            // 只有指定路径才能获取原图
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, CameraGallaryUtil.fileUri);
                            startActivityForResult(intent, CameraGallaryUtil.PHOTO_REQUEST_TAKEPHOTO);
                        }
                    })
                    .canceledOnTouchOutside(false)
                    .build();
            dialog.show();

        }

    public void call(View view) {
        checkPhonePermission();
    }

    private void checkPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //如果没有打电话权限就申请
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CALL_PHONE);
        }else {
            //有权限就直接打电话
            callPhone();
        }
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:"+"10086");
        intent.setData(data);
        try {
            startActivity(intent);
        }catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * 申请权限的回调
     * @param requestCode
     * @param permissions
     * @param paramArrayOfInt
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSION_REQUEST_CALL_PHONE) {
            if (paramArrayOfInt[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            }else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("该功能需要访问电话权限，不开启将无法正常工作！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).create();
                    dialog.show();
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap=CameraGallaryUtil.getBitmapFromCG(this,requestCode,resultCode,data);
        imageView.setImageBitmap(bitmap);
    }
}
