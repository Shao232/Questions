package com.questions.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 工具类
 */
public class MyUtils {

    public static MyUtils utils;

    private MyUtils() {

    }

    public synchronized static MyUtils getInstance() {
        if (utils == null)
            utils = new MyUtils();
        return utils;
    }




    private String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
               + "." + (i >> 24 & 0xFF);
    }

    /**
     * SHA1加密
     *
     * @param info
     * @return
     */
    public String SHA1(String info) {
        byte[] digesta = null;
        String rs = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(info.getBytes());
            digesta = alga.digest();
            rs = bytesToHex(digesta);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * MD5加密，32位
     *
     * @param url 需加密的字符串
     * @return 加密后的字符串
     */
    public String MD5(String url) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                            'E', 'F'};
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // e.printStackTrace();
            return getFile(url);
        }
        md5.update(url.getBytes());
        byte[] md5Bytes = md5.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md5Bytes.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md5Bytes[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 获取文件的MD5值
     *
     * @param file
     * @return
     */
    public String getFileMD5(File file) {
        if (file == null || !file.isFile()) {
            return null;
        }
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 将URL转成能够识别的目录
     */
    public String getFile(String url) {
        String path = url;
        if (path.contains("?")) {
            path = path.replace("?", "_");
        }
        if (path.contains("/")) {
            path = path.replace("/", "_");
        }
        if (path.contains(".")) {
            path = path.replace(".", "_");
        }
        return path;
    }

    /**
     * 获取外部缓存目录中的自定义文件路径
     *
     * @param context  上下文
     * @param path     二级目录，三级目录中间用"/"分隔，前后不需要加"/"
     * @param fileName 文件名，自动转成md5的名字
     * @param isMD5    文件名是否加密
     * @return File
     */
    public File getCache(Context context, String path, String fileName,
                         boolean isMD5) {
        File file = null;
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                file = new File(context.getExternalCacheDir().getPath() + "/"
                                + path);
            } else {
                file = new File(context.getCacheDir() + "/" + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null)
            return null;
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        if (isMD5) {
            String hz = "";
            int a = fileName.lastIndexOf(".");
            if (a > 0) {
                hz = fileName.substring(a, fileName.length());
            }
            file = new File(file.getPath() + "/" + MD5(fileName) + hz);
        } else
            file = new File(file.getPath() + "/" + fileName);
        return file;
    }

    /**
     * 获取外部缓存目录中的自定义目录
     *
     * @param context 上下文
     * @param path    自定义目录
     * @return File
     */
    public File getCache(Context context, String path) {
        File file = null;
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                file = new File(context.getExternalCacheDir().getPath() + "/"
                                + path);
            } else {
                file = new File(context.getCacheDir() + "/" + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null)
            return null;
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return file;
    }

    /**
     * 获取内部缓存目录中的自定义目录
     *
     * @param context 上下文
     * @param path    自定义目录
     * @return File
     */
    public File getPrivateCache(Context context, String path) {
        File file = null;
        try {
            file = new File(context.getCacheDir() + "/" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null)
            return null;
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        return file;
    }

    /**
     * 获取内部缓存目录中的自定义文件路径
     *
     * @param context  上下文
     * @param path     二级目录，三级目录中间用"/"分隔，前后不需要加"/"
     * @param fileName 文件名，自动转成md5的名字
     * @param isMD5    文件名是否加密
     * @return File
     */
    public File getPrivateCache(Context context, String path, String fileName,
                                boolean isMD5) {
        File file = null;
        try {
            file = new File(context.getCacheDir() + "/" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file == null)
            return null;
        if (!file.exists()) {
            if (!file.mkdirs()) {
                return null;
            }
        }
        if (isMD5) {
            String hz = "";
            int a = fileName.lastIndexOf(".");
            if (a > 0) {
                hz = fileName.substring(a, fileName.length());
            }
            file = new File(file.getPath() + "/" + MD5(fileName) + hz);
        } else
            file = new File(file.getPath() + "/" + fileName);
        return file;
    }

    /**
     * 获取内部版本号
     *
     * @param context 上下文
     * @return String
     */
    public int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            versionCode = pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取外部版本名称
     *
     * @param context 上下文
     * @return String 版本名称
     */
    public String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            versionName = pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 获取GUID
     *
     * @return String
     */
    public String getGUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public String getSystemVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 转换时间格式
     *
     * @param format 传入的time的格式，例："yyyy/MM/dd HH:mm:ss"
     * @param time   时间
     * @param isTime true为返回时:分:秒，false为返回年-月-日
     * @return String
     */
    @SuppressLint("DefaultLocale")
    public String parseTime(String format, String time, boolean isTime) {
        if (time != null && !"".equals(time)) {
            Date date = stringToDate(format, time);
            if (date == null)
                return time;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int years = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int ss = calendar.get(Calendar.SECOND);

            if (isTime) {
                return String.format("%02d:%02d:%02d", hour, min, ss);
            }
            return String.format("%d-%02d-%02d", years, month, day);
        }
        return time;
    }

    /**
     * 转换时间格式
     *
     * @param format 传入的time的格式，例："yyyy/MM/dd HH:mm:ss"
     * @param time   时间
     * @return String
     * 返回年-月-日 时:分
     */
    @SuppressLint("DefaultLocale")
    public String parseTime(String format, String time) {
        if (!"".equals(time)) {
            Date date = stringToDate(format, time);
            if (date == null)
                return time;
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int years = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int ss = calendar.get(Calendar.SECOND);

            return String.format("%d-%02d-%02d %02d:%02d", years, month, day, hour, min);
        }
        return time;
    }

    /**
     * 24小时制转12小时制
     *
     * @param time   24小时制的时间
     * @param format 时间时与分的间隔符号
     * @return 返回hh:mm Am/Pm.没有秒
     */
    public String parseTime24To12(String time, String format) {
        if (time == null || time.length() < 5
            || format == null || format.length() == 0 ||
            (!time.contains(format))) {
            return time;
        }
        time = time.substring(0, 5);
        String time12 = null;
        String[] times = time.split(format);
        if (times == null || times.length < 2) {
            return time;
        }
        int h = Parse.getInstance().parseInt(times[0]);
        int m = Parse.getInstance().parseInt(times[1]);
        String p = "Am";
        if (h == 0 || h == 24) {
            h = 12;
        } else if (h >= 12) {
            p = "Pm";
            if (h > 12)
                h -= 12;
        }
        time12 = String.format("%02d:%02d ", h, m) + p;
        return time12;
    }

    /**
     * String类型的时间转Date
     *
     * @param format 传入的time的格式，例："yyyy/MM/dd HH:mm:ss"
     * @param date   时间
     * @return
     */
    public Date stringToDate(String format, String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否存在虚拟按键
     *
     * @param context
     * @return
     */
    public boolean isDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs
                .getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class
                    .forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass,
                    "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasNavigationBar = false;
        }

        return hasNavigationBar;
    }

    /**
     * 获取虚拟按键高度
     *
     * @param context
     * @return
     */
    public int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && isDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @param mContext
     * @return
     */
    public int getStatusBarHeight(Context mContext) {
        int statusBarHeight = 0;
        Class<?> c = null;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 时间戳转换成时间字符窜
     *
     * @param format 时间格式
     * @param time   时间戳
     * @return
     */
    public String date2String(String format, long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(d);
    }

    /**
     * 将时间字符串转为时间戳
     *
     * @param format 时间格式
     * @param time   时间
     * @return
     */
    public long string2Date(String format, String time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        try {
            date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断是否月份中的第一周
     *
     * @param format
     * @param date
     * @return
     */
    public boolean dayOfWeekInMonth(String format, String date) {
        Date d = stringToDate(format, date);
        if (d == null)
            return false;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        if (calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        if (serviceName == null) {
            throw new NullPointerException("serviceName不可为Null");
        }
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            String packageName = myList.get(i).service.getPackageName();
            if (mContext.getPackageName().equals(packageName)) {
                if (serviceName.equals(mName)) {
                    isWork = true;
                    break;
                }
            }
        }
        return isWork;
    }

    /**
     * 获取24小时格式的当前系统时间
     *
     * @param format time的格式，例："yyyy/MM/dd HH:mm:ss"
     * @param locale 时区，例：Locale.CHINA（代表中国时区）
     */
    public String getTime24(String format, Locale locale) {
        if (format.contains("h")) {
            format = format.replace("h", "H");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(new Date());
    }

    /**
     * 获取12小时格式的当前系统时间
     *
     * @param format time的格式，例："yyyy/MM/dd hh:mm:ss"
     * @param locale 时区，例：Locale.CHINA（代表中国时区）
     */
    public String getTime12(String format, Locale locale) {
        if (format.contains("H")) {
            format = format.replace("H", "h");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(new Date());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue dp单位
     * @return px（像素）单位
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int px = (int) (dpValue * scale + 0.5f);
        if (px == 0) {
            if (dpValue > 0) {
                px = 1;
            } else if (dpValue < 0) {
                px = -1;
            }
        }
        return px;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue 像素单位
     * @return dp单位
     */
    public int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 键盘隐藏并把焦点置为false
     *
     * @param context   上下文
     * @param editTexts EditText数组
     */
    public void setKeyBoardFocusable(Context context, EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setFocusable(false);
            editTexts[i].setFocusableInTouchMode(false);
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTexts[i].getWindowToken(), 0);
        }
    }

    /**
     * 键盘隐藏
     *
     * @param context   上下文
     * @param editTexts EditText数组
     */
    public void setKeyBoardGone(Context context, EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTexts[i].getWindowToken(), 0);
        }
    }

    /**
     * 输入法在窗口上已经显示，则隐藏，反之则显示
     *
     * @param context
     */
    public void setKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示软键盘
     *
     * @param context
     */
    public void setKeyBoardVisibility(Context context, EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editTexts[i], InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 拆分比较
     *
     * @param base  基数
     * @param alive 与基数进行比较的字符
     * @param split 两个相比较的间隔符
     * @return alive大于等于base返回fals，alive小于base返回true
     */
    public boolean isMin(String base, String alive, String split) {
        boolean isMin = false;
        if (base == null) {
            throw new NullPointerException("比较基数不可为Null");
        }
        if (alive == null) {
            throw new NullPointerException("比较数不可为Null");
        }
        if (split == null || split.length() == 0) {
            if (Parse.getInstance().parseInt(alive) < Parse.getInstance().parseInt(base)) {
                isMin = true;
            } else {
                isMin = false;
            }
            return isMin;
        }
        if (!base.contains(split) && !alive.contains(split)) {
            if (Parse.getInstance().parseInt(alive) < Parse.getInstance().parseInt(base)) {
                isMin = true;
            } else {
                isMin = false;
            }
            return isMin;
        }
        String[] bases = null;
        String[] alives = null;
        if (base.contains(split)) {
            String splitCopy = split;
            if (".".equals(split)) {
                splitCopy = "\\.";
            }
            bases = base.split(splitCopy);
        } else {
            bases = new String[1];
            bases[0] = base;
        }

        if (alive.contains(split)) {
            String splitCopy = split;
            if (".".equals(split)) {
                splitCopy = "\\.";
            }
            alives = alive.split(splitCopy);
        } else {
            alives = new String[1];
            alives[0] = alive;
        }

        int[] baseS;
        int[] aliveS;
        if (bases.length > alives.length) {
            baseS = new int[bases.length];
            aliveS = new int[bases.length];
        } else {
            baseS = new int[alives.length];
            aliveS = new int[alives.length];
        }

        for (int i = 0; i < baseS.length; i++) {
            if (i < bases.length) {
                baseS[i] = Parse.getInstance().parseInt(bases[i]);
            } else {
                baseS[i] = 0;
            }
            if (i < alives.length) {
                aliveS[i] = Parse.getInstance().parseInt(alives[i]);
            } else {
                aliveS[i] = 0;
            }
        }

        for (int i = 0; i < baseS.length; i++) {
            if (aliveS[i] < baseS[i]) {
                isMin = true;
                break;
            } else if (aliveS[i] > baseS[i]) {
                isMin = false;
                break;
            } else {
                continue;
            }
        }
        return isMin;
    }

    /**
     * 拆分比较
     *
     * @param base  基数
     * @param alive 与base进行比较的字符
     * @param split 两个相比较的间隔符
     * @return alive小于等于base返回fals，alive大于base返回true
     */
    public boolean isMax(String base, String alive, String split) {
        boolean isMax = false;
        if (base == null) {
            throw new NullPointerException("比较基数不可为Null");
        }
        if (alive == null) {
            throw new NullPointerException("比较数不可为Null");
        }
        if (split == null || split.length() == 0) {
            if (Parse.getInstance().parseInt(alive) > Parse.getInstance().parseInt(base)) {
                isMax = true;
            } else {
                isMax = false;
            }
            return isMax;
        }
        if (!base.contains(split) && !alive.contains(split)) {
            if (Parse.getInstance().parseInt(alive) > Parse.getInstance().parseInt(base)) {
                isMax = true;
            } else {
                isMax = false;
            }
            return isMax;
        }
        String[] bases = null;
        String[] alives = null;
        if (base.contains(split)) {
            String splitCopy = split;
            if (".".equals(split)) {
                splitCopy = "\\.";
            }
            bases = base.split(splitCopy);
        } else {
            bases = new String[1];
            bases[0] = base;
        }

        if (alive.contains(split)) {
            String splitCopy = split;
            if (".".equals(split)) {
                splitCopy = "\\.";
            }
            alives = alive.split(splitCopy);
        } else {
            alives = new String[1];
            alives[0] = alive;
        }

        int[] baseS;
        int[] aliveS;
        if (bases.length > alives.length) {
            baseS = new int[bases.length];
            aliveS = new int[bases.length];
        } else {
            baseS = new int[alives.length];
            aliveS = new int[alives.length];
        }

        for (int i = 0; i < baseS.length; i++) {
            if (i < bases.length) {
                baseS[i] = Parse.getInstance().parseInt(bases[i]);
            } else {
                baseS[i] = 0;
            }
            if (i < alives.length) {
                aliveS[i] = Parse.getInstance().parseInt(alives[i]);
            } else {
                aliveS[i] = 0;
            }
        }

        for (int i = 0; i < baseS.length; i++) {
            if (aliveS[i] > baseS[i]) {
                isMax = true;
                break;
            } else if (aliveS[i] < baseS[i]) {
                isMax = false;
                break;
            } else {
                continue;
            }
        }
        return isMax;
    }

    /**
     * 设置输入框焦点
     *
     * @param isFocusable 焦点布尔值
     * @param editTexts   EditText数组
     */
    public void setFocusable(boolean isFocusable, EditText... editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            editTexts[i].setFocusable(isFocusable);
            editTexts[i].setFocusableInTouchMode(isFocusable);
        }
    }

    /**
     * 米转换成千米（保留两位小数）
     *
     * @param m 米
     * @return 如果不够位数转返回stringm，否则返回stringkm
     */
    public String m2km(String m) {
        if (m == null || m.equals("")) {
            m = "0m";
        }
        double db = Parse.getInstance().parseDouble(m);
        if (db >= 1000) {
            m = String.format("%.2fkm", db / 1000.0f);
        } else {
            m = String.format("%.0fm", db);
        }
        return m;
    }

    /**
     * 压缩图片
     *
     * @param newW 新图片宽度
     * @param newH 新图片高度
     * @param path 本地图片地址
     * @return
     */
    public Options reduceBitmap(int newW, int newH, String path) {
        if (path == null || path.length() == 0) {
            return null;
        }
        Options op = new Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, op);
        int oldW = op.outWidth;
        int oldH = op.outHeight;
        int size = 1;// 默认不缩放
        if (oldW > oldH) {// 宽大于高的情况(横版相机)
            if (newW > newH) {
                if (oldW > newW) {
                    size = oldW / newW;
                }
            } else {
                if (oldW > newH) {
                    size = oldW / newH;
                }
            }
        } else {// 高大于宽的情况(竖版相机)
            if (newH > newW) {
                if (oldH > newH) {
                    size = oldH / newH;
                }
            } else {
                if (oldH > newW) {
                    size = oldH / newW;
                }
            }
        }
        Options op1 = new Options();
        op1.inJustDecodeBounds = false;
        op1.inSampleSize = size;
        return op1;
    }






    /**
     * 获取图片方向
     *
     * @param filePath 图片地址
     * @return
     */
    public int getExifOrientation(String filePath) {
        int degree = 0;
        if (filePath == null)
            return degree;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException ex) {
            // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;

                    default:
                        break;
                }
            }
        }

        return degree;
    }

    /**
     * 获取星期几
     *
     * @param format
     * @param date
     * @return 0, 1, 2, 3, 4, 5, 6
     */
    public int getWeek(String format, String date) {
        if ("".equals(date)) {
            return 0;
        }
        Date mDate = stringToDate(format, date);
        if (mDate == null)
            return 0;
        int week;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return week;
    }

    /**
     * 查看密码
     *
     * @param mEditText
     * @param isView    是否查看,true查看,false隐藏
     */
    public void viewPassword(EditText mEditText, boolean isView) {
        if (mEditText == null) {
            throw new NullPointerException();
        } else {
            int cursorPosition = mEditText.getSelectionStart();
            if (isView) {
                mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mEditText.setSelection(cursorPosition);
            } else {
                mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mEditText.setSelection(cursorPosition);
            }
        }
    }

    /**
     * 根据传进来的日期时间计算年龄
     *
     * @param format
     * @param date
     * @return 年龄
     */
    public int getAge(String format, String date) {
        date = parseTime(format, date, false);
        if (date == null || date.length() < 10
            || !date.contains("-")) {
            return 0;
        }
        date = date.substring(0, 10);
        String[] dates = date.split("-");
        if (dates == null || dates.length < 3) {
            return 0;
        }
        String today = utils.date2String("yyyy-MM-dd", System.currentTimeMillis());
        int year = Parse.getInstance().parseInt(today.substring(0, 4));
        int month = Parse.getInstance().parseInt(today.substring(5, 7));
        int day = Parse.getInstance().parseInt(today.substring(8, 10));

        int yearOld = Parse.getInstance().parseInt(dates[0]);
        int monthOld = Parse.getInstance().parseInt(dates[1]);
        int dayOld = Parse.getInstance().parseInt(dates[2]);

        int age = year - yearOld;
        if (month - monthOld < 0) {
            age -= 1;
        } else if (month - monthOld == 0) {
            if (day - dayOld < 0) {
                age -= 1;
            }
        }
        if (age < 0)
            age = 0;
        return age;
    }

    /**
     * 是否正确手机号
     *
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }


    /**
     * 字节转换16进制字符串
     *
     * @param data
     * @return
     */
    public String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * 顶部
     *
     * @param context 上下文
     * @param msg     提示文字
     */
    public void showToastTop(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    /**
     * 顶部（自定义位置）
     *
     * @param context 上下文
     * @param msg     提示文字
     * @param x       横向位置
     * @param y       纵向位置
     */
    public void showToastTop(Context context, String msg, int x, int y) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, x, y);
        toast.show();
    }

    /**
     * 中间
     *
     * @param context 上下文
     * @param msg     提示文字
     */
    public void showToastCenter(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 中间（自定义位置）
     *
     * @param context 上下文
     * @param msg     提示文字
     * @param x       横向位置
     * @param y       纵向位置
     */
    public void showToastCenter(Context context, String msg, int x, int y) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }

    /**
     * 底部
     *
     * @param context 上下文
     * @param msg     提示文字
     */
    public void showToastBottom(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    /**
     * 底部（自定义位置）
     *
     * @param context 上下文
     * @param msg     提示文字
     * @param x       横向位置
     * @param y       纵向位置
     */
    public void showToastBottom(Context context, String msg, int x, int y) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, x, y);
        toast.show();
    }

    /**
     * 默认Toast
     *
     * @param context 上下文
     * @param msg     提示文字
     */

    public void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public String dateFormat(String format, long t) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(t);
    }

    /**
     * date转String
     *
     * @param format
     * @param date
     * @return
     */
    public String dateToString(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(date);
    }



}
