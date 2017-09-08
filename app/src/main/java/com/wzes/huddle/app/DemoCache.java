package com.wzes.huddle.app;

import android.content.Context;

public class DemoCache {
    private static String mAccount;
    private static Context mContext;

    public static void clear() {
        mAccount = null;
    }

    public static String getAccount() {
        return mAccount;
    }

    public static void setAccount(String account) {
        mAccount = account;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context.getApplicationContext();
    }
}
