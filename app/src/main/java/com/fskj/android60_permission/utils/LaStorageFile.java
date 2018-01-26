package com.fskj.android60_permission.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public enum LaStorageFile {
    INSTANCE;

    /**
     * 获取手机上需要保存的根路径String "/"，优先考虑外置SD卡。
     *
     * @return  如果存在sd卡返回sd卡根路径，如果没有则返回Root根路径,
     * 返回根路径(String)skRootPath
     */
    public String getskRootPath() {
        if (checkExistRom()) {
            return getskStorageDirectory();
        } else {
            return getskRootDirectory();
        }
    }

    /**
     * 获取手机上需要保存的路径String，使用该方法首先要判空
     *
     * @return boolean getEnvironmentPath
     */
    public String getskStorageDirectory() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取手机上需要保存的路径String，该方法不必判空。
     *
     * @return boolean getskRootDirectory
     */
    public String getskRootDirectory() {
        return Environment.getRootDirectory().getPath();
    }

    /**
     * 获取根目录 "/cache-download"目录
     */
    public String getDownloadCacheDirectory() {
        return Environment.getDownloadCacheDirectory().getPath();
    }

    /**
     * 获取应用包名内缓存目录路径
     */
    public String getskCacheDirectory(Context context) {
        return context.getCacheDir().getPath();
    }

/*
* -----------------分割线-------------
* */

    /**
     * getskRootFile
     * 获取手机上需要保存的根路径file，优先考虑外置SD卡。(file会一定存在)
     *
     * @return File 文件，如果存在sd卡返回sd卡文件，如果没有则返回根路径File对象。
     */
    public File getskRootFile() {
        if (checkExistRom()) {
            return getskStorageDirectoryFile();//获取根目录
        } else {
            return getskRootDirectoryFile();
        }
    }

    /**
     * 获取手机上绝对路径需要保存的路径file，(改方法需要判空。)
     *
     * @return "\"跟路径对象
     */
    public File getskStorageDirectoryFile() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * getskRootDirectoryFile
     * 获取手机上绝对路径需要保存的路径file，(该方法一定不会为空的。)
     *
     * @return "\"根路径对象
     */
    public File getskRootDirectoryFile() {
        return Environment.getRootDirectory();
    }

    /**
     * 获取下载文件对象 File。
     */
    public File getDownloadCacheDirectoryFile() {
        return Environment.getDownloadCacheDirectory();
    }

    /**
     * 获取手机上缓存的路径file，(该方法一定不会为空的。)
     * android的缓存存储建议放在该目录下面，该目录的下面不需要权限。
     * <p>
     * @param context 上下文对象
     * @return /data/data/<application package>/cache目录。
     */
    public File getskCacheFile(@NonNull Context context) {
        return context.getCacheDir();
    }

    /**
     * 获取手机上缓存的路径file，(该方法一定不会为空的。)
     * android的存储建议放在该目录下面，该目录的下面不需要权限。
     * Context.getExternalCacheDir()方法,一般存放临时缓存数据
     * <p>
     * @param context 上下文对象
     * @return SDCard/Android/data/你的应用包名/cache/目录.
     */
    public File getskExternalCacheFile(@NonNull Context context) {
        return context.getExternalCacheDir();
    }

    /**
     * 获取手机上缓存的路径file，(该方法一定不会为空的。)
     * android的存储建议放在该目录下面，该目录的下面不需要权限。
     * <p>
     * @param context 上下文对象
     * @return /data/data/<application package>/files目录。
     */
    public File getskFile(@NonNull Context context) {
        return context.getFilesDir();
    }

    /**
     * 获取手机上缓存的路径file，(该方法一定不会为空的。)
     * android的存储建议放在该目录下面，该目录的下面不需要权限。
     * Context.getExternalFilesDir()方法,一般放一些长时间保存的数据
     * <p>
     * @param context 上下文对象
     * @return SDCard/Android/data/你的应用的包名/files/
     */
    public File getskExternalFile(@NonNull Context context) {
        return context.getExternalFilesDir(null);
    }


    /**
     * 获取Integrate系列的SharedPreferences对象.
     * @param context 上下文对象
     * @return INA SharedPreferences
     */
    public SharedPreferences getSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences(AnConstants.AnShps, Context.MODE_PRIVATE);
    }

    /**
     * 获取系统的SharedPreferences对象.
     * @param context 上下文对象
     * @return system SharedPreferences
     * like :com.qy.integrate_sharedpreferences
     */


    public SharedPreferences getDefaultSharedPreferences(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /*
* -----------------分割线-------------
* */

    /**
     * 判断同名方法内置SDcard是否存在（目的是为了兼容以前的版本）
     *
     * @return boolean true存在，false不存在
     */
    public boolean checkExistRom() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断同名方法内置SDcard是否存在，兼容以前版本
     *
     * @return boolean true存在，false不存在
     */
    public boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * linux获取外置SDcard的路径(mount linux的角度去获取，和上面方法有区别)
     * 得到mount外置sd卡的路径（简单参数）
     *
     * @return String 外置SDCARD路径。
     */
    public String getEPathByMount() {
        StringBuilder sdCardStringBuilder = new StringBuilder();
        List<String> sdCardPathList = new ArrayList<String>();
        String sdcardpath = null;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        sdCardPathList.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        if (sdCardPathList != null) {
            for (String path : sdCardPathList) {
                sdCardStringBuilder.append(path);
            }
            sdcardpath = sdCardStringBuilder.toString();
        }
        return sdcardpath;
    }

    /**
     * android系统可通过Environment.getExternalStorageDirectory()获取存储卡的路径，
     * 但是现在有很多手机内置有一个存储空间，同时还支持外置sd卡插入，
     * 这样通过Environment.getExternalStorageDirectory()方法获取到的就是内置存储卡的位置，
     * 需要获取外置存储卡的路径就比较麻烦，这里借鉴网上的代码，稍作修改，
     * 在已有的手机上做了测试，效果还可以，当然也许还有其他的一些奇葩机型没有覆盖到。
     * <p>
     * E ，代表其它，E代表external外部
     *
     * @return String 得到mount外置sd卡的路径（复杂参数）
     */
    public String getEEPathByMount() {
        String sdcard_path = null;
        String sd_default = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (sd_default.endsWith("/")) {
            sd_default = sd_default.substring(0, sd_default.length() - 1);
        }
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                } else if (line.contains("fuse") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sdcard_path;
    }
    /*
* -----------------分割线-------------
* */

    /**
     * 计算SD卡剩余容量和总容量(调用该方法需要判空。)
     * 以$符号区分总容量和剩余容量。
     *
     * @return String 总容量&剩余容量
     */
    public String calculateStorage() {
        String txt = "";
        //判断是否有插入存储卡
        if (checkExistRom()) {
            File path = Environment.getExternalStorageDirectory();
            //取得sdcard文件路径
            StatFs statfs = new StatFs(path.getPath());
            //获取block的SIZE
            long blocSize = statfs.getBlockSize();
            //获取BLOCK数量
            long totalBlocks = statfs.getBlockCount();
            //己使用的Block的数量
            long availaBlock = statfs.getAvailableBlocks();
            String[] total = filesize(totalBlocks * blocSize);
            String[] availale = filesize(availaBlock * blocSize);
            //显示SD卡的容量信息
            txt = total[0] + total[1] + "$" + availale[0] + availale[1];
        }
        return txt;
    }

    /**
     * 计算内存总容量(调用该方法不需要判空。)
     * 以$符号区分总容量和剩余容量。
     *
     * @return String 总容量and剩余容量
     */
    public String calculateRoot() {
        String txt = "";
        //判断是否有插入存储卡,这里严谨些还是判断sk
        if (!checkExistRom()) {
            File path = getskRootFile();
            //取得sdcard文件路径
            StatFs statfs = new StatFs(path.getPath());
            //获取block的SIZE
            long blocSize = statfs.getBlockSize();
            //获取BLOCK数量
            long totalBlocks = statfs.getBlockCount();
            //己使用的Block的数量
            long availaBlock = statfs.getAvailableBlocks();
            String[] total = filesize(totalBlocks * blocSize);
            String[] availale = filesize(availaBlock * blocSize);
            //显示SD卡的容量信息
            txt = total[0] + total[1] + "$" + availale[0] + availale[1];
        }
        return txt;
    }
    /*
* -----------------分割线-------------
* */

    //返回数组，下标1代表大小，下标2代表单位 KB/MB
    private String[] filesize(long size) {
        String str = "";
        if (size >= 1024) {
            str = "KB";
            size /= 1024;
            if (size >= 1024) {
                str = "MB";
                size /= 1024;
            }
        }
        DecimalFormat formatter = new DecimalFormat();
        formatter.setGroupingSize(3);
        String result[] = new String[2];
        result[0] = formatter.format(size);
        result[1] = str;
        return result;
    }

        /*
* -----------------分割线-------------
* */

    /**
     * 根目录下-创建文件目录
     * 在android6.0，需要动态获取操作文件的权限。
     *
     * 说明：『& 两边都会判断』『&& 第一个不满足false，后面不会判断 』
     *      『| 两边都会判断』『|| 第一个满足true，后面不会判断 』
     *
     * @param pwdFilepath pwd 文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @return  创建的文件对象
     */
    public File createDir(@NonNull String pwdFilepath) {
        File fileDirs = new File(getskRootFile(), pwdFilepath);
        if ((fileDirs.isDirectory() & fileDirs.exists()) || fileDirs.mkdirs())
            return fileDirs;
        return fileDirs;
    }

    /**
     * 创建文件目录，
     * 在android6.0，需要动态获取操作文件的权限。
     * 是否在应用包名目录下。返回文件对象。
     * @param context 上下文对象
     * @param pwdFilepath pwd 文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @param innalPackage innalPackage=true /data/data/packagename/file/pwdFile目录下创建文件。
     * =false/sdcard/Android/PackageName/file/目录。
     */
    public File createDir(@NonNull Context context, @NonNull String pwdFilepath, boolean innalPackage) {
        File fileDirs;
        if (innalPackage) {
            fileDirs = new File(getskFile(context), pwdFilepath);
        } else {
            fileDirs = new File(getskExternalFile(context), pwdFilepath);
        }
        if ((fileDirs.isDirectory() & fileDirs.exists()) || fileDirs.mkdirs())
            return fileDirs;
        return fileDirs;
    }

    /**
     * 创建文件目录，
     * 在android6.0，需要动态获取操作文件的权限。
     * 返回文件对象。
     * @param context 上下文对象
     * @param pwdFilepath pwd 文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @param innalPackage innalPackage =true /data/data/packagename/cache/目录
     * innalPackage = fale /Android/data/你的应用包名/cache/目录.
     */
    public File createCacheDir(@NonNull Context context, @NonNull String pwdFilepath, @NonNull boolean innalPackage) {
        File fileDirs;
        if (innalPackage) {
            fileDirs = new File(getskCacheFile(context), pwdFilepath);
        } else {
            fileDirs = new File(getskExternalCacheFile(context), pwdFilepath);
        }
        if ((fileDirs.isDirectory() & fileDirs.exists()) || fileDirs.mkdirs())
            return fileDirs;
        return fileDirs;
    }


    /**
     * 在/ / / 目录下创建文件（文件不一定存在）
     * 在android6.0，需要动态获取操作文件的权限。
     * 如果不对文件输入输出流操作文件不存在
     * @param fileName pwd 文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     */
    public File touchFile(@NonNull String fileName) {
        File rootDir = getskRootFile();
        return new File(rootDir, fileName);
    }

    /**
     * 删除文件再创建文件（文件一定存在）。
     */
    public File fouceTouchFile(@NonNull String fileName) {
        File newFile = new File(getskRootFile(), fileName);
        if (newFile.exists() && newFile.delete())
            try {
                if (newFile.createNewFile()) {}
            } catch (IOException e) {
                e.printStackTrace();
            }
        return newFile;
    }
    /**
     * 创建文件目录，
     * 在android6.0，需要动态获取操作文件的权限。
     * 返回文件对象。
     * @param pwdFilepath pwd 文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @param fileName 创建的文件名
     */
    public File touchFile(@NonNull String pwdFilepath, @NonNull String fileName) {
        File fileDirs = createDir(pwdFilepath);
        return new File(fileDirs, fileName);
    }

    /**
     * 创建文件目录，删除文件再创建文件（文件一定存在）。
     * 在android6.0，需要动态获取操作文件的权限。
     * 返回文件对象。
     * @param pwdFilepath pwd 文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @param fileName 创建的文件名
     */
    public File fouceTouchFile(@NonNull String pwdFilepath, @NonNull String fileName) {
        File newFile = new File(createDir(pwdFilepath), fileName);
        if (newFile.exists() && newFile.delete()) {}
        try {
            if (newFile.createNewFile()) {}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 缓存目录创建文件。
     * 在android6.0，不需要动态获取权限
     * @param context 上下文对象
     * @param pwdFilepath pwd 文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @param fileName 创建的文件名
     * @return 创建文件的对象
     * @param innalPackage = true是/data/data/<application package>/cache目录下创建文件。
     *        innalPackage = false/Android/data/你的应用包名/cache/目录下创建文件。
     */
    public File touchCacheFile(@NonNull Context context, @NonNull String pwdFilepath, @NonNull String fileName, boolean innalPackage) {
        File fileDirs = createCacheDir(context, pwdFilepath, innalPackage);
        return new File(fileDirs, fileName);
    }

    /**
     * 创建文件目录，删除文件再创建文件（文件一定存在）。
     * 在android6.0，不需要动态获取权限。
     * @param context 上下文对象
     * @param pwdFilepath pwd 文件目录(不包含文件名） like this's [建议/an/apk/],[an/apk/],[an/apk]
     * @param fileName 创建的文件名
     * @return 创建文件的对象
     * @param innalPackage = true是/data/data/<application package>/cache目录下创建文件。
     *        innalPackage = false/Android/data/你的应用包名/cache/目录下创建文件。
     */
    public File fouceTouchCacheFile(@NonNull Context context, @NonNull String pwdFilepath, @NonNull String fileName, boolean innalPackage) {
        File newFile = new File(createCacheDir(context, pwdFilepath, innalPackage), fileName);
        if (newFile.exists() && newFile.delete()) {}
        try {
            if (newFile.createNewFile()) {}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 删除/ / / 目录下 的某一个文件
     * http://android.xsoftlab.net/reference/android/content/Context.html#getExternalFilesDir(java.lang.String)
     *
     * @param fileName fileName like this [qy.jpg]
     * @return 返回true文件删除成功
     * 返回false删除失败，或者删除文件不存在。
     */
    public boolean deleteFile(@NonNull String fileName) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File deleteFile = new File(getskRootFile(), fileName);
        if (deleteFile.exists()) {
            return deleteFile.delete();
        }
        return false;
    }

    /**
     * 删除/ / / 目录下 的某一个文件
     * http://android.xsoftlab.net/reference/android/content/Context.html#getExternalFilesDir(java.lang.String)
     *
     * @param fileName fileName like this [qy.jpg]
     * @param pwdFilepath 文件路径 like this[/an/music/love/]
     * @return 返回true文件删除成功
     * 返回false删除失败，或者删除文件不存在。
     */
    public boolean deleteFile(@NonNull String pwdFilepath, @NonNull String fileName) {
        File deleteFile = new File(pwdFilepath + fileName);
        if (deleteFile.exists()) {
            return deleteFile.delete();
        }
        return false;
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的所有文件，
     * 如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory 文件目录
     */
    public void deleteFiles(@NonNull File directory) {
        if (directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

}
