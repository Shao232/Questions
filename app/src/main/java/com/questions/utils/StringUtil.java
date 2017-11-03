package com.questions.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 处理空字符串
     *
     * @param str
     *
     * @return String
     */
    public static String doEmpty(String str) {
        return doEmpty(str, "");
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @param defaultValue
     *
     * @return String
     */
    public static String doEmpty(String str, String defaultValue) {
        if (str == null || str.equalsIgnoreCase("null")
                || str.trim().equals("") || str.trim().equals("－请选择－")) {
            str = defaultValue;
        } else if (str.startsWith("null")) {
            str = str.substring(4, str.length());
        }
        return str.trim();
    }

    /**
     * 请选择
     */
    final static String PLEASE_SELECT = "请选择...";

    public static int strLength(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        return str.length();
    }

    /**
     * 判断不为空
     *
     * @param o
     *
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return o != null && !"".equals(o.toString().trim())
                && !"null".equalsIgnoreCase(o.toString().trim())
                && !"undefined".equalsIgnoreCase(o.toString().trim())
                && !PLEASE_SELECT.equals(o.toString().trim())
                && !"\"\"".equals(o.toString().trim());
    }

    /**
     * 判断为空
     *
     * @param o
     *
     * @return
     */
    public static boolean isEmpty(Object o) {
        return o == null || "".equals(o.toString().trim())
                || "null".equalsIgnoreCase(o.toString().trim())
                || "undefined".equalsIgnoreCase(o.toString().trim())
                || PLEASE_SELECT.equals(o.toString().trim())
                || "\"\"".equals(o.toString().trim());
    }

    /**
     * 判断大于0
     *
     * @param o
     *
     * @return
     */
    public static boolean num(Object o) {
        int n = 0;
        try {
            n = Integer.parseInt(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    /**
     * 判断大于0.0
     *
     * @param o
     *
     * @return
     */
    public static boolean decimal(Object o) {
        double n = 0;
        try {
            n = Double.parseDouble(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n > 0.0;
    }

    /**
     * 给JID返回用户名
     *
     * @param Jid
     *
     * @return
     */
    public static String getUserNameByJid(String Jid) {
        if (isEmpty(Jid)) {
            return null;
        }
        if (!Jid.contains("@")) {
            return Jid;
        }
        return Jid.split("@")[0];
    }

    /**
     * 给用户名返回JID
     *
     * @param jidFor   域名//如ahic.com.cn
     * @param userName
     *
     * @return
     */
    public static String getJidByName(String userName, String jidFor) {
        if (isEmpty(jidFor) || isEmpty(jidFor)) {
            return null;
        }
        return userName + "@" + jidFor;
    }

    /**
     * 给用户名返回JID,使用默认域名ahic.com.cn
     *
     * @param userName
     *
     * @return
     */
    public static String getJidByName(String userName) {
        String jidFor = "getString";
        return getJidByName(userName, jidFor);
    }

    /**
     * 根据给定的时间字符串，返回月 日 时 分 秒
     *
     * @param allDate like "yyyy-MM-dd hh:mm:ss SSS"
     *
     * @return
     */
    public static String getMonthTomTime(String allDate) {
        return allDate.substring(5, 19);
    }

    /**
     * 根据给定的时间字符串，返回月 日 时 分 月到分钟
     *
     * @param allDate like "yyyy-MM-dd hh:mm:ss SSS"
     *
     * @return
     */
    public static String getMonthTime(String allDate) {
        return allDate.substring(5, 16);
    }


    /**
     * 判断不相等
     *
     * @param a
     * @param b
     *
     * @return
     */
    public static boolean isNotEqual(Object a, Object b) {
        return !isEqual(a, b);
    }

    /**
     * 判断相等
     *
     * @param a
     * @param b
     *
     * @return
     */
    public static boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a != null && b != null)
            return a.toString().trim().equals(b.toString().trim());
        return false;
    }

    /**
     * 字符串比较
     *
     * @param a
     * @param b
     *
     * @return
     */
    public static boolean isLower(Object a, Object b) {
        if (a == null && b == null)
            return false;
        if (a != null && b != null)
            return a.toString().compareTo(b.toString()) < 0;
        return false;
    }

    public static boolean isBigger(Object a, Object b) {
        if (a == null && b == null)
            return false;
        if (a != null && b != null)
            return a.toString().compareTo(b.toString()) > 0;
        return false;
    }

    /**
     * 根据正则表达式判断字符串的合法性
     *
     * @param o
     * @param reg
     *
     * @return
     */
    public static boolean matches(Object o, String reg) {
        if (isEmpty(o)) {
            return false;
        }
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(o.toString());
        return m.matches();
    }

    /**
     * 设置字符串中 n = end - start 位为*
     *
     * @param start
     * @param end
     * @param mobiles
     *
     * @return
     */
    public static String invisibleInfo(int start, int end, String mobiles) {
        char[] chars = mobiles.toCharArray();
        for (int i = start; i < end; i++) {
            chars[i] = '*';
        }
        return String.valueOf(chars);
    }

    /**
     * 是否是数字
     *
     * @param txt
     *
     * @return
     */
    public static boolean isDigit(String txt) {
        return matches(txt, "[0-9]*");
    }

    /**
     * 是否是字母
     *
     * @param txt
     *
     * @return
     */
    public static boolean isLetter(String txt) {
        return matches(txt, "[a-zA-Z]");
    }

    /**
     * 是否是汉字
     *
     * @param txt
     *
     * @return
     */
    public static boolean isChinese(String txt) {
        return matches(txt, "^[\\u4e00-\\u9fa5]+$");
    }

    public static boolean isLetterAndChinese(String txt) {
        return matches(txt, "^[\\u4e00-\\u9fa5a-zA-Z]+$");
    }

    /**
     * 匹配手机号
     *
     * @param phone
     *
     * @return
     */
    public static boolean isPhoneNum(String phone) {
        return matches(phone, "(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}");
    }

    /**
     * 匹配身份证号
     *
     * @param cardId
     *
     * @return
     */
    public static boolean isCardId(String cardId) {
        return matches(cardId, "\\d{15}|\\d{17}[0-9Xx]");
    }

    /**
     * 匹配邮箱
     *
     * @param email
     *
     * @return
     */
    public static boolean isEmail(String email) {
        return matches(email, "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}");
    }

}
