package com.wzes.huddle.chatservice;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import com.wzes.huddle.R;
import com.wzes.huddle.app.DemoCache;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
    private  NotificationManager notifyManager;

    private void sendNotification() {
        long[] vibrates = new long[]{0, 500, 1000, 0};
        notifyManager = (NotificationManager) DemoCache.getContext().getSystemService("notification");
        notifyManager.notify(1, new Builder(DemoCache.getContext())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("来自抱团的新消息")
                .setVibrate(vibrates)
                .build());
    }

    protected void channelRead0(ChannelHandlerContext arg0, String arg1) throws Exception {
        try {
            Intent intent = new Intent();
            intent.setAction("com.wzes.huddle.list");
            intent.putExtra("msg", arg1);
            DemoCache.getContext().sendBroadcast(intent);

            Intent uIntent = new Intent();
            uIntent.setAction("com.wzes.huddle.user");
            uIntent.putExtra("msg", arg1);
            DemoCache.getContext().sendBroadcast(uIntent);

        } catch (Exception e) {

        }
    }
}
