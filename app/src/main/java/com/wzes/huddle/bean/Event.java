package com.wzes.huddle.bean;

public class Event {
    public String content;
    public String enrool_end_date;
    public String enrool_start_date;
    public int event_id;
    public Category event_type;
    public int follow_account;
    public String image;
    public Level level;
    public String match_end_date;
    public String match_start_date;
    public String organizer;
    public int page_view;
    public String title;
    public String user_id;

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getEvent_id() {
        return this.event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getEvent_type() {
        return this.event_type;
    }

    public void setEvent_type(Category event_type) {
        this.event_type = event_type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrganizer() {
        return this.organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getEnrool_start_date() {
        return this.enrool_start_date;
    }

    public void setEnrool_start_date(String enrool_start_date) {
        this.enrool_start_date = enrool_start_date;
    }

    public String getEnrool_end_date() {
        return this.enrool_end_date;
    }

    public void setEnrool_end_date(String enrool_end_date) {
        this.enrool_end_date = enrool_end_date;
    }

    public String getMatch_start_date() {
        return this.match_start_date;
    }

    public void setMatch_start_date(String match_start_date) {
        this.match_start_date = match_start_date;
    }

    public String getMatch_end_date() {
        return this.match_end_date;
    }

    public void setMatch_end_date(String match_end_date) {
        this.match_end_date = match_end_date;
    }

    public int getPage_view() {
        return this.page_view;
    }

    public void setPage_view(int page_view) {
        this.page_view = page_view;
    }

    public int getFollow_account() {
        return this.follow_account;
    }

    public void setFollow_account(int follow_account) {
        this.follow_account = follow_account;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
