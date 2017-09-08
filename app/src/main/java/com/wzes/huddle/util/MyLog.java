package com.wzes.huddle.util;

import android.util.Log;

/**
 * Created by xuantang on 17-9-8.
 */

public class MyLog {
    private static final String TAG = "Huddle";
    public static void i(String msg){
        Log.i(TAG, msg);
    }
    public static void v(String msg){
        Log.v(TAG, msg);
    }
    public static void d(String msg){
        Log.d(TAG, msg);
    }
    public static void e(String msg){
        Log.e(TAG, msg);
    }
}
