package com.fskj.android60_permission.utils;

/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class AnConstants {
    public static String AnDIR = "an";//主目录文件名

    public static String AnDataBase = "an.db";//数据库文件名
    public static String AnMovie = "movie";//视频文件目录名
    public static String AnPicture = "picture";//存放图片文件名
    public static String AnMusic = "melody";//存放音乐文件名
    public static String AnDownload = "download";//下载文件目录名
    public static String AnShps = "shps";//SharedPreferences's name
    public static String AnPublic = "public";//公共文件夹名
    public static String AnApk = "apk";//存放apk文件的目录名
    public static String AnLog = "log";//公共log文件目录名
    public static String AnLog_ALL = "--qydq--";//过滤所有日志的标志

    /**
     * 这些应该叫AnConfigData吧。
     */
    public static String UTF8_ENCODE = "UTF-8";//UTF8编码
    public static int time = 5;//请求超时时间
    public static int time_ad = 10;//下载广告请求超时时间
    public static int time_download = 10;//下载文件请求超时时间
    public static int delay_time = 10;//通用请求超时超时时间

    /*
    *
    * 网络请求地址*/
    public static final class URL {
        public static String BASE_URL = "https://github.com/qydq/Integrate/";
        public static String Extra_requestURl = "requestURl";
        public static String Extra_requestfileName = "fileName";
        public static String Extra_requestdirpath = "apkDirPath";
        public static final String MICROPHONE = "android.permission-group.MICROPHONE";
        public static final String PHONE = "android.permission-group.PHONE";

    }

    /*
*
* 统一管理存储键值对的键*/
    public static final class KEY {
        public static String BASE_KEY = "https://github.com/qydq/Integrate/";
        public static String Extra_daynight = "day_night_mode";

    }

    public static String suffixPNG = ".PNG";
    public static String suffixJPEG = ".JPEG";
    public static String suffixJPG = ".jpg";

}
