package com.wzes.huddle.app;

import android.app.Application;

import com.lzy.ninegrid.NineGridView;
import com.wzes.huddle.util.TeamImageLoader;


public class BaseApplication extends Application {
    public void onCreate() {
        super.onCreate();
        DemoCache.setContext(this);
        NineGridView.setImageLoader(new TeamImageLoader());
    }

}
