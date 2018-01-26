package com.fskj.android60_permission.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public enum DataService {
    INSTANCE;
    /**
     * 默认falsec'g'g'g'g'g'g'g'g'g'g'g'g'g'g'g'g'g'g
     */
    private boolean isok = false;
    public static final String TIME_FORMAT_EE = "yyyyMMdd-HH:mm:ss:SSS";
    public static final String TIME_FORMAT_E = "yyyy-MM-dd hh:mm:ss";


    /**
     * 检查是否为有效IP地址
     *
     * @param ipAddress IP地址
     * @return isOk
     */
    public boolean regexCheckIp(@NonNull String ipAddress) {
        if (!TextUtils.isEmpty(ipAddress)) {
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            // 判断ip地址是否与正则表达式匹配
            if (ipAddress.matches(regex)) {
                isok = true;
            } else {
                isok = false;
            }
        }
        return isok;
    }

    /**
     * 检查是否为有效网络地址
     *
     * @param httpURL 请求URL
     * @return isok
     */
    public boolean regexCheckUrl(@NonNull String httpURL) {
        if (!TextUtils.isEmpty(httpURL)) {
            String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
            if (httpURL.matches(regex)) {
                isok = true;
            } else {
                isok = false;
            }
        }
        return isok;
    }

    /**
     * 验证是否为电话号码
     *
     * @param mobiles 电话号码
     * @return true || false
     */
    public boolean regexCheckMobiles(@NonNull String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[6-8])|(18[0-9])|(14[5,7]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证是否为数字
     *
     * @param numeric numeric字符串
     * @return isok
     */
    public boolean regexCheckNumeric(@NonNull String numeric) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(numeric);
        if (matcher.matches()) {
            isok = true;
        } else {
            isok = false;
        }
        return isok;
    }

    /**
     * 验证是否为数字
     *
     * @param numeric numeric字符串
     * @return true || false
     */
    public boolean checkNumeric(@NonNull String numeric) {
        for (int i = 0; i < numeric.length(); i++) {
            System.out.println(numeric.charAt(i));
            if (!Character.isDigit(numeric.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 字符串 MD5加密
     *
     * @param str 待加密字符串
     * @return MD5加密字符串
     */
    public String md5utf8(@NonNull String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(str.getBytes("utf-8"));
            return hexToString(result);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * byte[]转化为String
     *
     * @param bytes
     */
    public String hexToString(@NonNull byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }

    /**
     * 字符串 MD5加密
     *
     * @param serviceStr 服务器待加密字符串
     * @return MD5加密字符串
     */
    public String md5service(@NonNull String serviceStr) {
        String resultString = null;
        try {
            resultString = new String(serviceStr);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    /**
     * md5加密字符串。
     *
     * @param text 要加密的字符。
     * @return 加密后的字符。
     */
    public String md5(@NonNull String text) {
        try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = text.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 检查字符串是否合法，合法返回true，不合法返回false
     *
     * @param txt 验证的字符串。
     * @return isok。true合法，false
     */
    public boolean regexCheckLegalInput(@NonNull String txt) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(txt);
        if (m.find()) {
            isok = false;
        } else {
            isok = true;
        }
        return isok;
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public byte[] byteArrayToHex(@NonNull String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 这个函数用于将字节数组换成成16进制的字符串
     *
     * @param byteArray
     * @return 16进制字符串
     */
    public String byteArrayToHex(@NonNull byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    /**
     * 得到当前时间
     *
     * @return 当前时间
     */
    public String getCurrentTimeByDate() {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
        return sdf.format(date);
    }

    public String getTimePrefix() {
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT_EE, Locale.ENGLISH);
        return df.format(System.currentTimeMillis());
    }


    /**
     * TODO 获取今天时间
     *
     * @return Date    返回类型
     */
    public Date getCurrentTimeByCalendar() {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day);
        Date nowDay = c.getTime();
        return nowDay;
    }

    /**
     * 得到当前短时间
     *
     * @return 年月日时分
     */
    public String getShotDateTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * 得到当前短时间
     *
     * @return 年月日时分秒
     */
    public String getLongDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.  HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 通过字段来移除某个list中的数据。
     *
     * @param lists     集合元素
     * @param valueName 集合中的元素
     * @return 移出数据后的集合
     */
    public List<String> removeItemByName(@NonNull List<String> lists,
                                         @NonNull String valueName) {
        List<String> resLists = new ArrayList<>();
        if (lists.size() == 0) {
        } else {
            for (int i = 0; i < lists.size(); i++) {
                if (valueName.equals(lists.get(i))) {
                    lists.remove(valueName);
                }
            }
            resLists = lists;
        }
        return resLists;
    }

    /**
     * 通过集合中的位置来移除某个list中的数据。
     *
     * @param lists    集合元素
     * @param position 位置
     * @return 移除某个位置后的lists
     */
    public List<String> removeItemByPosition(@NonNull List<String> lists,
                                             @NonNull int position) {
        lists.remove(position);
        return lists;
    }

//    //模糊查询
//    public List likeString(List lists, String likename) {
//        List results = new ArrayList();
//        Pattern pattern = Pattern.compile(name);
//        for (int i = 0; i < lists.size(); i++) {
//            Matcher matcher = pattern.matcher(((Employee) lists.get(i)).getName());
//            if (matcher.find()) {
//                results.add(list.get(i));
//            }
//        }
//        return results;
//    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return　Date
     */

    public Date parse(@NonNull String strDate,
                      @NonNull String pattern) {

        if (TextUtils.isEmpty(strDate)) {
            return null;
        }
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return　String转换后数据
     */

    public String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }
}
