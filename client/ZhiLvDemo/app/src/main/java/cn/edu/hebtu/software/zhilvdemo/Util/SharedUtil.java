package cn.edu.hebtu.software.zhilvdemo.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @ProjectName:    MoJi
 * @Description:    配置信息保存到 SharedPreferences 的数据.
 * @Author:         张璐婷
 * @CreateDate:     2019/12/7 10:40
 * @Version:        1.0
 */

public class SharedUtil {

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getDefaultSharedPreferences(String sharedPath, Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(sharedPath, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static void putString(String sharedPath, Context context, String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(sharedPath,context);
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(String sharedPath, Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(sharedPath,context);
        return sharedPreferences.getString(key, "");
    }

    public static void putBoolean(String sharedPath, Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(sharedPath,context);
        Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(String sharedPath, Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(sharedPath,context);
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static Integer getInt(String sharedPath, Context context,String key){
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(sharedPath,context);
        return sharedPreferences.getInt(key, -1);
    }

    public static void putInt(String sharedPath, Context context, String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(sharedPath,context);
        Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public static void remove(String sharedPath, Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(sharedPath,context);
        Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.apply();
    }

    public static void clear(String sharedPath, Context context) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(sharedPath,context);
        Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.apply();
    }

}

