package com.wzes.huddle.chatservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import com.wzes.huddle.app.Preferences;

public class ChatService extends Service {
    public static ClientMain clientMain;
    private static String host = "59.110.136.134";
    private static int port = 9999;

    public void onCreate() {
        try {
            clientMain = new ClientMain(host, port, Preferences.getUserAccount());
        } catch (Exception e) {
            Toast.makeText(this, "服务器挂了呢！请稍后再试", 0).show();
        }
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        clientMain.stopConn();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
