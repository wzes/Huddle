package com.wzes.huddle.bean;

public class Message {
    private String content;
    private String from_id;
    private String from_img;
    private String message_type;
    private int msg_id;
    private long send_date;
    private String to_id;
    private String to_img;

    public int getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public String getMessage_type() {
        return this.message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom_id() {
        return this.from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return this.to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getFrom_img() {
        return this.from_img;
    }

    public void setFrom_img(String from_img) {
        this.from_img = from_img;
    }

    public String getTo_img() {
        return this.to_img;
    }

    public void setTo_img(String to_img) {
        this.to_img = to_img;
    }

    public long getSend_date() {
        return this.send_date;
    }

    public void setSend_date(long send_date) {
        this.send_date = send_date;
    }
}
