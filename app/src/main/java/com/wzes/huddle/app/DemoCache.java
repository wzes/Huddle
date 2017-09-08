package com.wzes.huddle.app;

import android.content.Context;

public class DemoCache {
    private static String account;
    private static Context context;

    public static void clear() {
        account = null;
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        account = account;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        context = context.getApplicationContext();
    }
}
