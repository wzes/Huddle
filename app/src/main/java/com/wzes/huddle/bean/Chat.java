package com.wzes.huddle.bean;

public class Chat {
    public String content;
    public String image;
    public String name;
    public String send_date;
    public String user_id;

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSend_date() {
        return this.send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }
}
