package com.yeyclude.pullrefreshlayout;

import android.content.Context;
import android.content.SharedPreferences;

import com.yeyclude.pullrefreshlayout.R;

import java.util.Map;

/**
 * 数据处理
 * Created by clude on 17/10/2.
 */
public class StaticClass {

    /**
     * 获取保存的数据
     * @param context 上下文
     * @param key 键
     * @param defValue 缺省值
     * @return 值
     */
    public static Object getSPData(Context context, String key, Object defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        Map <String, ?> map = sharedPreferences.getAll();
        Object value = map.get(key);
        if (null != value){
            return value;
        }
        return defValue;
    }

    /**
     * 保存数据
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void saveSPData(Context context, String key, Object value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Long){
            editor.putLong(key, (Long) value);
        }
        else if (value instanceof String){
            editor.putString(key, (String) value);
        }
        else if (value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        }
        else if (value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }
        else if (value instanceof Float){
            editor.putFloat(key, (Float) value);
        }
        editor.apply();
    }

}
