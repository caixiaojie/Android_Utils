package com.fskj.android60_permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class UploadAvatarUtil {
    public static final int IMAGE_REQUEST_CODE = 0;  //选择本地图片
    public static final int CAMERA_REQUEST_CODE = 1; //拍照
    public static final int CLIP_REQUEST_CODE = 2;   //裁剪

    private static final int DEFAULT_TEXT_SIZE = 32;//默认的字体大小

    /**
     * 检查是否存在sdcard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建文件目录
     *
     * @param filePath 文件路径
     */
    public static File makeFileDir(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 获取文件对象
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return
     */
    public static File getFile(String filePath, String fileName) {
        File file = null;
        //创建文件目录
        makeFileDir(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 保存图片到本地
     *
     * @param imgUrl        图片路径
     * @param imageFileName 图片名称
     * @param bitmap
     */
    public static void saveLocalImage(String imgUrl, String imageFileName, Bitmap bitmap) {
        try {
            File f = getFile(imgUrl, imageFileName);
            FileOutputStream fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地图片
     *
     * @param imgUrl        图片路径
     * @param imageFileName 图片名称
     */
    public static void deleteLocalImage(String imgUrl, String imageFileName) {
        File f = getFile(imgUrl, imageFileName);
        if (f.isFile() && f.exists()) {
            f.delete();
        }
    }

    /**
     * 绘制圆角矩形图片
     * 圆形 x=120,y=120,outerRadiusRat=60
     *
     * @param x
     * @param y
     * @param image
     * @param outerRadiusRat
     * @return
     */
    public static Bitmap createFramedPhoto(int x, int y, Bitmap image,
                                           float outerRadiusRat) {
        // 根据源文件新建一个darwable对象
        Drawable imageDrawable = new BitmapDrawable(image);

        // 新建一个新的输出图片
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // 新建一个矩形
        RectF outerRect = new RectF(0, 0, x, y);

        // 产生一个白色的圆角矩形
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

        // 将源图片绘制到这个圆角矩形上
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();

        return output;
    }

    /**
     * 选择本地图片
     */
    public static void pickPhoto(Context context) {
        Intent intentFromGallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromGallery.setType("image/*"); // 设置文件类型
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) context).startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
    }

    /**
     * 拍照
     */
    public static void takePhoto(Context context, String imgUrl, String imageFileNameTemp) {
        // 打开相机
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用,存储缓存图片
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(getFile(imgUrl, imageFileNameTemp)));
        }
        ((Activity) context).startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    public static void startPhotoZoom(Context context, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra("return-data", false);//设置为不返回数据
        ((Activity) context).startActivityForResult(intent, CLIP_REQUEST_CODE);
    }

    /**
     * 创建指定样式、指定动画的Dialog
     *
     * @param context
     * @param view        Dialog的布局
     * @param styleId     Dialog的样式
     * @param animStyleId Dialog的动画样式
     * @return
     */
    public static Dialog createDialog(Context context, View view, int styleId, int animStyleId) {
        Dialog mSelectPhotoDialog = new Dialog(context, styleId);
        mSelectPhotoDialog.setContentView(view);
        Window window = mSelectPhotoDialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(animStyleId);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        mSelectPhotoDialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        mSelectPhotoDialog.setCanceledOnTouchOutside(true);
        mSelectPhotoDialog.show();

        return mSelectPhotoDialog;
    }

    /**
     * 第1种方法：图片上绘制文字
     *
     * @param context
     * @param drawableId
     * @param text
     * @param color
     * @return
     */
    public static Bitmap drawTextAtBitmap(Context context, int drawableId, String text, int color, float density) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);

        int[] size = getBitmapSize(context, drawableId);
//        Log.e("density", density + "");
        int x = size[0];
        int y = size[1];
//        int x = (int) (size[0] * density);
//        int y = (int) (size[1] * density);
//        Log.e("x", x + "");
//        Log.e("y", y + "");
//        bitmap = getScaleBitmap(context,bitmap);

        // 创建一个和原图同样大小的位图
        Bitmap newbit = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newbit);
        Paint paint = new Paint();
        // 贴图 在原始位置0，0插入原图
        canvas.drawBitmap(bitmap, 0, 0, paint);
        paint.setColor(color);
        int textSize = DEFAULT_TEXT_SIZE;
        if (density >= 3) {
            textSize *= (density + 1);
        } else if (density >= 2) {
            textSize *= (density + 0.1);
        }
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        // 在原图指定位置写上字
//        canvas.drawText(text, x / 2 - 10 * density, y / 2, paint);
        canvas.drawText(text, x / 2, y / 2 + 20 * (density-1) + 2, paint);

        canvas.save(Canvas.ALL_SAVE_FLAG);

        // 存储
        canvas.restore();
        return newbit;
    }

    /**
     * 图片叠加
     *
     * @param bitmap1
     * @param bitmap2
     * @return
     */
    public static Bitmap layerBitmap(Bitmap bitmap1, Bitmap bitmap2) {

        // copy 防止出现Immutable bitmap passed to Canvas constructor错误
        bitmap1 = bitmap1.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap newBitmap = null;

        newBitmap = Bitmap.createBitmap(bitmap1);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();

        int w = bitmap1.getWidth();
        int h = bitmap1.getHeight();

        int w_2 = bitmap2.getWidth();
        int h_2 = bitmap2.getHeight();

        paint.setColor(Color.GRAY);
        paint.setAlpha(125);
        canvas.drawRect(0, 0, bitmap1.getWidth(), bitmap1.getHeight(), paint);

        paint = new Paint();
        canvas.drawBitmap(bitmap2, Math.abs(w - w_2) / 2,
                Math.abs(h - h_2) / 2, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储新合成的图片
        canvas.restore();

        return newBitmap;


    }

    /**
     * 图片叠加
     *
     * @param bitmap1
     * @param bitmap2
     * @return
     */
    public static LayerDrawable layerDrawable(Bitmap bitmap1, Bitmap bitmap2) {
        Drawable[] array = new Drawable[2];
        array[0] = new BitmapDrawable(bitmap1);
        array[1] = new BitmapDrawable(bitmap2);
        LayerDrawable la = new LayerDrawable(array);
        // 其中第一个参数为层的索引号，后面的四个参数分别为left、top、right和bottom
        la.setLayerInset(0, 0, 0, 0, 0);
        la.setLayerInset(1, 10, 10, 10, 10);

        return la;
    }

    /**
     * 获取Bitmap的宽高
     *
     * @param context
     * @param drawableId 图片id
     * @return
     */
    private static int[] getBitmapSize(Context context, int drawableId) {
        int[] size = new int[2];
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), drawableId, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        size[0] = width;
        size[1] = height;
        return size;
    }


    /**
     * 等比例缩放
     *
     * @param context
     * @param bitmap
     * @return
     */
   /* public static Bitmap getScaleBitmap(Context context, Bitmap bitmap) {
        int[] screen = ScreenPixelsUtil.getScreenPixels(context);
        Matrix matrix = new Matrix();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.e("Width", width + "");
        Log.e("Height", height + "");
        //屏幕宽度/图片宽度
        float w = screen[0] / width;
        float h = screen[1] / height;
        matrix.postScale(w, h);// 获取缩放比例
        // 根据缩放比例获取新的位图
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }*/
}
