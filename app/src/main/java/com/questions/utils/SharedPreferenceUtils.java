package com.questions.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class SharedPreferenceUtils {

    /**
     * 项目properties文件名
     */
    private static final String PROPERTIES_FILE = "Questions";

    /**
     * 保存用户信息
     *
     * @param paramsName 字段名
     * @param value      参数值
     */
    public static void saveParams (Context context, String paramsName, String value) {
        if (TextUtils.isEmpty (value)) return;
        SharedPreferences preferences = context.getSharedPreferences (
                PROPERTIES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        String idString = getParams (paramsName, context);
        if (idString != null && !"".equals (idString)) {
            editor.putString (paramsName, "");
        }
        editor.putString (paramsName, value);
        editor.apply ();
    }

    public static void clearSP (Context context) {
        SharedPreferences preferences = context.getSharedPreferences (PROPERTIES_FILE, Context.MODE_APPEND);
        SharedPreferences.Editor edit = preferences.edit ();
        edit.clear ().apply ();
    }

    /**
     * 保存用户信息
     *  @param paramsName 字段名
     * @param value      参数值
     */
    public static boolean saveBooleanValue (Context context, String paramsName, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences (
                PROPERTIES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putBoolean (paramsName, value);
        editor.apply ();
        return value;
    }

    /**
     * 保存用户信息
     *
     * @param paramsName 字段名
     * @param value      参数值
     */
    public static void saveIntValue (Context context, String paramsName, int value) {
        if (value == 0) return;
        SharedPreferences preferences = context.getSharedPreferences (
                PROPERTIES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putInt (paramsName, value);
        editor.apply ();
    }

    /**
     * 保存用户信息
     *
     * @param paramsName 字段名
     * @param value      参数值
     */
    public static void saveLongValue (Context context, String paramsName, long value) {
        if (value == 0) return;
        SharedPreferences preferences = context.getSharedPreferences (
                PROPERTIES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putLong (paramsName, value);
        editor.apply ();
    }

    /**
     * 保存用户信息
     *
     * @param paramsName 字段名
     * @param value      参数值
     */
    public static void saveFolatValue (Context context, String paramsName, float value) {
        if (value == 0) return;
        SharedPreferences preferences = context.getSharedPreferences (
                PROPERTIES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putFloat (paramsName, value);
        editor.apply ();
    }

    /**
     * 保存用户信息
     *
     * @param paramsName 字段名
     * @param value      参数值
     */
    public static void saveStringValue (Context context, String paramsName, String value) {
        if (TextUtils.isEmpty (value)) return;
        SharedPreferences preferences = context.getSharedPreferences (
                PROPERTIES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        editor.putString (paramsName, value);
        editor.apply ();
    }

    /**
     * 获取用户的ID
     *
     * @param paramsName 字段名
     * @param context
     * @return 返回用户的ID
     */
    public static String getParams (String paramsName, Context context) {
        SharedPreferences preferences = context.getSharedPreferences (PROPERTIES_FILE, Context.MODE_PRIVATE);
        if (preferences.contains (paramsName)) {
            return preferences.getString (paramsName, "");
        }
        return null;
    }

    /**
     * 获取对应字段的值
     *
     * @param context
     * @param paramsName 字段名
     * @return
     */
    public static String getStringValue (Context context, String paramsName) {
        SharedPreferences preferences = context.getSharedPreferences (PROPERTIES_FILE, Context.MODE_PRIVATE);
        String value = null;
        if (preferences.contains (paramsName)) {
            value = preferences.getString (paramsName, "");
        }
        return value;
    }

    /**
     * 获取对应字段的值
     *
     * @param context
     * @param paramsName 字段名
     * @return
     */
    public static int getIntValue (Context context, String paramsName) {
        SharedPreferences preferences = context.getSharedPreferences (PROPERTIES_FILE, Context.MODE_PRIVATE);
        int value = 0;
        if (preferences.contains (paramsName)) {
            value = preferences.getInt (paramsName, 0);
        }
        return value;
    }

    /**
     * 获取对应字段的值
     *
     * @param context
     * @param paramsName 字段名
     * @return
     */
    public static long getLongValue (Context context, String paramsName) {
        SharedPreferences preferences = context.getSharedPreferences (PROPERTIES_FILE, Context.MODE_PRIVATE);
        long value = 0;
        if (preferences.contains (paramsName)) {
            value = preferences.getLong (paramsName, 0);
        }
        return value;
    }

    /**
     * 获取对应字段的值
     *
     * @param context
     * @param paramsName 字段名
     * @return
     */
    public static float getFloatValue (Context context, String paramsName) {
        SharedPreferences preferences = context.getSharedPreferences (PROPERTIES_FILE, Context.MODE_PRIVATE);
        float value = 0;
        if (preferences.contains (paramsName)) {
            value = preferences.getFloat (paramsName, 0);
        }
        return value;
    }

    /**
     * 获取对应字段的值
     *
     * @param context
     * @param paramsName 字段名
     * @return
     */
    public static boolean getBooleanValue (Context context, String paramsName) {
        SharedPreferences preferences = context.getSharedPreferences (PROPERTIES_FILE, Context.MODE_PRIVATE);
        boolean value = true;

        if (preferences.contains (paramsName)) {
            value = preferences.getBoolean (paramsName, true);
        }
        return value;
    }

    /**
     * 保存用户信息
     *
     * @param paramsNameAndValue
     */
    public static void saveMessage (Context context, String paramsNameAndValue[][]) {
        SharedPreferences preferences = context.getSharedPreferences (
                PROPERTIES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit ();
        for (String[] aParamsNameAndValue : paramsNameAndValue) {
            String data = getParams (aParamsNameAndValue[0], context);
            if (data != null && !"".equals (data)) {
                editor.putString (aParamsNameAndValue[1], "");
            }
            editor.putString (aParamsNameAndValue[0], aParamsNameAndValue[0]);
        }
        editor.apply ();
    }


}
