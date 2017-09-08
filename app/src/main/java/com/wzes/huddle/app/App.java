package com.wzes.huddle.app;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

public class App extends Application {
    public void onCreate() {
        super.onCreate();
        if (getSharedPreferences("user_settings", 0).getInt("theme", 0) == 0) {
            AppCompatDelegate.setDefaultNightMode(1);
        } else {
            AppCompatDelegate.setDefaultNightMode(2);
        }
    }
}
