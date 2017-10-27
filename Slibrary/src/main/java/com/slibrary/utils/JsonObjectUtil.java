package com.slibrary.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by yangweirong on 14/12/17.
 */
public class JsonObjectUtil {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static String serialize(JsonElement je) {
        return gson.toJson(je);
    }

    public static Gson getGson() {
        return gson;
    }

    public static <S> S toClass(JsonElement je, Type type) {
        return gson.fromJson(je, type);
    }

    public static <S> S toClass(JsonElement je, Class<S> cls) {
        return gson.fromJson(je, cls);
    }

    public static <S> S toClass(String je, Type type) {
        return gson.fromJson(je, type);
    }

    public static <S> S toClass(String je, Class<S> cls) {
        return gson.fromJson(je, cls);
    }

    public static JsonObject parse(String str) {
        try {
            return gson.fromJson(str, JsonObject.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonArray paresJsonArray(String str) {
        try {
            return gson.fromJson(str, JsonArray.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonElement getElement(JsonElement jo, String... params) {
        JsonElement je = jo;
        for (String p : params) {
            if (isNull(je))
                return null;
            je = je.getAsJsonObject().get(p);
            if (isNull(je)) {
                return null;
            }
        }
        return je;
    }

    public static int getInt(JsonElement jo, String... params) {
        return getInt(jo, 0, params);
    }

    public static int getInt(JsonElement jo, int dft, String... params) {
        if (jo == null) {
            return dft;
        }
        JsonElement je = getElement(jo, params);
        return isNull(je) ? dft : je.getAsInt();
    }

    public static short getShort(JsonElement jo, String... params) {
        return getShort(jo, (short) 0, params);
    }

    public static short getShort(JsonElement jo, short dft, String... params) {
        if (jo == null) {
            return dft;
        }
        JsonElement je = getElement(jo, params);
        return isNull(je) ? dft : je.getAsShort();
    }

    public static long getLong(JsonElement jo, String... params) {
        return getLong(jo, 0L, params);
    }

    public static long getLong(JsonElement jo, long dft, String... params) {
        if (jo == null) {
            return dft;
        }
        JsonElement je = getElement(jo, params);
        return isNull(je) ? dft : je.getAsLong();
    }

    public static float getFloat(JsonElement jo, String... params) {
        return getFloat(jo, 0f, params);
    }

    public static float getFloat(JsonElement jo, float dft, String... params) {
        if (jo == null) {
            return dft;
        }

        JsonElement je = getElement(jo, params);
        return isNull(je) ? dft : je.getAsFloat();
    }

    public static double getDouble(JsonElement jo, String... params) {
        return getDouble(jo, 0f, params);
    }

    public static double getDouble(JsonElement jo, float dft, String... params) {
        if (jo == null) {
            return dft;
        }

        JsonElement je = getElement(jo, params);
        return isNull(je) ? dft : je.getAsDouble();
    }


    public static boolean getBoolean(JsonElement jo, String... params) {
        return getBoolean(jo, false, params);
    }

    public static boolean getBoolean(JsonElement jo, boolean dft, String... params) {
        if (jo == null) {
            return dft;
        }

        JsonElement je = getElement(jo, params);
        return isNull(je) ? dft : je.getAsBoolean();
    }

    public static String getString(JsonElement jo, String... params) {
        if (jo == null) {
            return null;
        }
        JsonElement je = getElement(jo, params);
        return isNull(je) ? null : je.getAsString();
    }

    /**
     * 该函数从jsonObject中提取string或者返回默认值
     *
     * @return
     */
    public static String getStringOrDefault(JsonElement jo, String attr) {
        String ret = JsonObjectUtil.getString(jo, attr);
        if (ret == null || ret == "") {
            ret = getDefaultStr();
        }
        return ret;
    }

    public static String getDefaultStr() {
        return "（未知）";
    }


    public static JsonArray getArray(JsonElement jo, String... params) {
        if (jo == null)
            return new JsonArray();
        JsonElement je = getElement(jo.getAsJsonObject(), params);
        return isNull(je) ? null : je.getAsJsonArray();
    }

    public static boolean isNull(JsonElement je) {
        return je == null || StringUtil.isEmpty(je) || je instanceof JsonNull;
    }

    public static JsonObject getObject(JsonElement jo, String... params) {
        if (jo == null) {
            return null;
        }
        JsonElement je = getElement(jo, params);
        try {
            return isNull(je) ? null : je.getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }

    public static int size(JsonArray arr) {
        return arr == null ? 0 : arr.size();
    }

    public static boolean isEmpty(JsonArray arr) {
        return arr == null || arr.size() == 0;
    }

    public static boolean isNotEmpty(JsonArray array) {
        return !isEmpty(array);
    }

    public static String dumpObject(Object obj) {
        if (obj == null) {
            return "None";
        }
        return gson.toJson(obj);
    }

    public static JsonElement isJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        try {
            return new JsonParser().parse(json);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

}
