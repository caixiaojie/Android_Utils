package com.fskj.android60_permission.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import com.fskj.android60_permission.AnApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ConcurrentModificationException;
/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public class LaLog {
    private static final String TAG = "AnLog";

    //如果保存日志，则用&&区分，方便查找
    private static final String LOGPRE_SPLIT = "&&";
    private static final String LOGPRE_APPVER = AnConstants.AnLog_ALL;

    /**
     * 每次打印字符串的最大长度
     */
    private static final int MAX_LENTH = 4000;

    /**
     * 每个log文件最大1M
     */
    private static int MAX_SIZE = 1024 * 1024;

    /**
     * 最大log文件数量
     */
    private static int MAC_ACCOUNT = 5;


    /**
     * 缓存大小 128k字节
     */
    private static final long MAX_BUFFER_CACHE = 128 * 1024;

    /* 6sec */
    private static final long LOGFILE_DELAY = 6 * 1000;
    /* 5mins */
    private static final long DELAY_TIME = 5 * 60 * 1000;

    /* 处理消息 */
    private static final int MSG_SAVE_LOG = 1;
    private static final int MSG_SAVE_EXIT = 2;

    private static StringBuilder anLogBuffer;
    private static Handler anLogHandler;


    /**
     * debug编译下打印信息Log
     *
     * @param tag TAG 提示一般为类名
     * @param msg 打印信息
     */
    public static void d(@NonNull String tag, Object... msg) {
        d(true, tag, msg);
    }

    /**
     * debug编译下打印信息Log
     *
     * @param tag       TAG 提示一般为类名
     * @param isSaveLog 是否保存
     * @param msg       打印信息
     */
    public static void d(boolean isSaveLog, @NonNull String tag, Object... msg) {
        String text = buildMessage(msg, LOGPRE_APPVER);
        if (!TextUtils.isEmpty(text)) {
            int len = text.length();
            if (len <= MAX_LENTH) {
                Log.d(tag, text);
            } else {
                do {
                    Log.d(tag, text.substring(0, MAX_LENTH));
                    text = text.substring(MAX_LENTH);
                    len = text.length();
                } while (len > MAX_LENTH);
                if (len > 0) {
                    Log.d(tag, text);
                }
            }
        }

        if (isSaveLog) {
            saveLog(tag, msg);
        }
    }

    /**
     * 打印warn级别的log
     *
     * @param tag TAG 提示一般为类名
     * @param msg 打印信息
     */
    public static void w(String tag, Object... msg) {
        String text = buildMessage(msg, LOGPRE_APPVER);
        int len = text.length();
        if (len <= MAX_LENTH) {
            Log.w(tag, text);
        } else {
            do {
                Log.w(tag, text.substring(0, MAX_LENTH));
                text = text.substring(MAX_LENTH);
                len = text.length();
            } while (len > MAX_LENTH);
            if (len > 0) {
                Log.w(tag, text);
            }
        }
    }

    /**
     * 打印error级别的log
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, Object... msg) {
        e(true, tag, msg);
    }

    /**
     * 打印error级别的log
     *
     * @param tag
     * @param msg
     */
    public static void e(boolean isSaveLog, String tag, Object... msg) {
        String text = buildMessage(msg, LOGPRE_APPVER);
        int len = text.length();
        if (len <= MAX_LENTH) {
            Log.e(tag, text);
        } else {
            do {
                Log.e(tag, text.substring(0, MAX_LENTH));
                text = text.substring(MAX_LENTH);
                len = text.length();
            } while (len > MAX_LENTH);
            if (len > 0) {
                Log.e(tag, text);
            }
        }

        if (isSaveLog) {
            saveLog(tag, msg);
        }
    }

    /**
     * 打印敏感信息，用户信息级别的log
     *
     * @param tag 标签
     * @param msg 日志内容
     */
    public static void s(boolean isSaveLog, @NonNull String tag, @NonNull Object... msg) {
        d(isSaveLog, tag, msg);
    }

    /**
     * 保存log到文件
     *
     * @param tag TAG
     * @param msg 消息
     */
    public static void saveLog(@NonNull String tag, @NonNull Object... msg) {
        String timePrefix = DataService.INSTANCE.getTimePrefix();
        String text = buildMessage(msg, timePrefix, tag + LOGPRE_SPLIT, LOGPRE_APPVER);
        writeAnlogToDisk(text);
    }

    private static String buildMessage(@NonNull Object[] msg, @NonNull String... prefix) {
        StringBuilder sb = new StringBuilder();
        try {
            for (String p : prefix) {
                sb.append(p);
            }
            for (Object m : msg) {
                sb.append(m != null ? m : "");
            }
        } catch (ConcurrentModificationException e) {
//            Log.e(TAG, e.getMessage());
            return "";
        }
        return sb.toString();
    }


    /**
     * 向文件中写文本内容
     *
     * @param file
     * @param content
     * @param append
     */
    public static void writeFile(File file, String content, boolean append) {
        if (null == file || null == content) {
            return;
        }
        byte[] bytes = null;
        try {
            bytes = content.getBytes(AnConstants.UTF8_ENCODE);
        } catch (UnsupportedEncodingException e) {
            w(TAG, "write file failed: ", e.getMessage());
        }
        if (bytes != null) {
            writeFile(file, bytes, append);
        }
    }

    /**
     * 向文件中写二进制内容
     *
     * @param file
     * @param content
     * @param append
     */
    public static void writeFile(File file, byte[] content, boolean append) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, append);
            out.write(content);
            out.flush();
        } catch (IOException e) {
            w(TAG, "write file failed: ", e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e(false, TAG, "close failed: ", e.getMessage());
                }
            }
        }
    }

    private static File getLogFile() {
        if (isDebug(AnApplication.getInstance())) {
            MAX_SIZE = 10 * 1024 * 1024;
            MAC_ACCOUNT = 10 * 5;
        }
        String fileDir = File.separator + AnConstants.AnDIR + File.separator + AnConstants.AnLog + File.separator;

        File resultFile = LaStorageFile.INSTANCE.fouceTouchFile(fileDir,"0.log");
        if (resultFile.exists() && resultFile.length() > MAX_SIZE) {
            File tmp = new File(fileDir + (MAC_ACCOUNT - 1) + ".log");
            if (!(tmp.exists() & tmp.delete())) {
                Log.e(TAG, "delete log file failed");
            }
            for (int i = MAC_ACCOUNT - 2; i >= 0; i--) {
                tmp = new File(fileDir + i + ".log");
                if (tmp.exists()) {
                    if (!tmp.renameTo(new File(fileDir + (i + 1) + ".log"))) {
                        Log.e(TAG, "rename log file failed");
                        return null;
                    }
                }
            }
        }
        return resultFile;
    }


    private static void writeAnlogToDisk(String text) {
        synchronized (LaLog.class) {
            if (anLogBuffer == null) {
                anLogBuffer = new StringBuilder(text);
            } else {
                anLogBuffer.append(text);
            }
            anLogBuffer.append("\n");

            boolean saveNow = anLogBuffer.length() >= MAX_BUFFER_CACHE;
            // schedule log file buffer saving
            if (anLogHandler == null) {
                HandlerThread thread = new HandlerThread("anLogfile_thread");
                thread.start();
                anLogHandler = new AnLogHandler(thread.getLooper());
                anLogHandler.sendEmptyMessageDelayed(MSG_SAVE_LOG, saveNow ? 0 : LOGFILE_DELAY);
            } else {
                anLogHandler.removeMessages(MSG_SAVE_EXIT);
                if (saveNow) {
                    anLogHandler.removeMessages(MSG_SAVE_LOG);
                    anLogHandler.sendEmptyMessage(MSG_SAVE_LOG);
                } else if (!anLogHandler.hasMessages(MSG_SAVE_LOG)) {
                    anLogHandler.sendEmptyMessageDelayed(MSG_SAVE_LOG, LOGFILE_DELAY);
                }
            }
        }
    }

    private static void flushAnlog() {
        synchronized (LaLog.class) {
            if (anLogBuffer == null) {
                return;
            }

            String text = anLogBuffer.toString();
            anLogBuffer = null;
            File file = getLogFile();
            if (file != null) {
                writeFile(file, text, true);
            } else {
                w(TAG, "get log file failed.");
            }
        }
    }

    private static boolean isDebug(@NonNull Context context) {
        if (context.getApplicationInfo() != null) {
            return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        return false;
    }

    private static class AnLogHandler extends Handler {

        public AnLogHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == msg) {
                return;
            }
            switch (msg.what) {
                case MSG_SAVE_LOG:
                    synchronized (LaLog.class) {
                        anLogHandler.removeMessages(MSG_SAVE_LOG);
                        flushAnlog();
                        anLogHandler.sendEmptyMessageDelayed(MSG_SAVE_EXIT, DELAY_TIME);
                    }
                    break;

                case MSG_SAVE_EXIT:
                    synchronized (LaLog.class) {
                        if (!anLogHandler.hasMessages(MSG_SAVE_LOG)) {
                            anLogHandler.getLooper().quit();
                            anLogHandler = null;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
