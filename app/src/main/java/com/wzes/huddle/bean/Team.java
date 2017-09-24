package com.wzes.huddle.bean;

import java.util.List;

public class Team {
    private String category;
    private String content;
    public String image;
    private List<Image> images;

    public List<teamuser> getTeamusers() {
        return teamusers;
    }

    public void setTeamusers(List<teamuser> teamusers) {
        this.teamusers = teamusers;
    }

    private List<teamuser> teamusers;

    private String info;
    private int join_acount;
    private int join_people;
    private String locationlatitude;
    private String locationlongitude;
    private String locationname;
    public String name;
    private long release_date;
    private long start_date;
    private String status;
    private int team_id;
    private String title;
    public String user_id;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String distance;

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

    public int getTeam_id() {
        return this.team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getStart_date() {
        return this.start_date;
    }

    public void setStart_date(long start_date) {
        this.start_date = start_date;
    }

    public long getRelease_date() {
        return this.release_date;
    }

    public void setRelease_date(long release_date) {
        this.release_date = release_date;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getJoin_acount() {
        return this.join_acount;
    }

    public void setJoin_acount(int join_acount) {
        this.join_acount = join_acount;
    }

    public int getJoin_people() {
        return this.join_people;
    }

    public void setJoin_people(int join_people) {
        this.join_people = join_people;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLocationname() {
        return this.locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

    public String getLocationlatitude() {
        return this.locationlatitude;
    }

    public void setLocationlatitude(String locationlatitude) {
        this.locationlatitude = locationlatitude;
    }

    public String getLocationlongitude() {
        return this.locationlongitude;
    }

    public void setLocationlongitude(String locationlongitude) {
        this.locationlongitude = locationlongitude;
    }

    public List<Image> getImages() {
        return this.images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
