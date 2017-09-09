package com.wzes.huddle.bean;

import com.google.gson.annotations.SerializedName;

public class ChatList {
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSend_date() {
        return send_date;
    }

    public void setSend_date(long send_date) {
        this.send_date = send_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @SerializedName("image")
    private String image;
    @SerializedName("name")
    private String name;
    @SerializedName("send_date")
    private long send_date;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("content")
    private String content;

}
