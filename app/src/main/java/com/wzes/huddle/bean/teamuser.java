package com.wzes.huddle.bean;

/**
 * Created by xuantang on 17-9-9.
 */

public class teamuser {
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    private String user_id;
    private String image;
    private String name;
}
