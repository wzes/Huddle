package com.wzes.huddle.chatservice;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientMain {
    private Channel channel;
    private String host;
    private int port;
    private boolean stop = false;
    private String user_id;

    public ClientMain(String host, int port, String user_id) {
        this.host = host;
        this.port = port;
        this.user_id = user_id;
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ClientIniter());
        try {
            this.channel = bootstrap.connect(host, port).sync().channel();
            this.channel.writeAndFlush("login" + user_id);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void sendMsg(String message) {
        this.channel.writeAndFlush(message);
    }

    public void stopConn() {
        this.channel.close();
    }
}
