package com.yihuitek.mytts.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private static SharedPreferences sp;

    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("SpUtil", Context.MODE_APPEND);
        }
        return sp;
    }
    /**
     * 保存int值
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp     = getSp(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    /**
     * 保存int值
     *
     * @param context 上下文
     * @param key     键
     * @param defValue   值
     */
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = getSp(context);
        return sp.getInt(key, defValue);
    }
}
