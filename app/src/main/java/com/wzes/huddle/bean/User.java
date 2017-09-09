package com.wzes.huddle.bean;

public class User {
    private String dob;
    private String grade;
    private String image;
    private String info;
    private String major;
    private String motto;
    private String name;
    private String password;
    private String sex;
    private String telephone;
    private String user_id;



    private int follow_account;

    public int getFollow_account() {
        return follow_account;
    }

    public void setFollow_account(int follow_account) {
        this.follow_account = follow_account;
    }

    public int getBefollow_account() {
        return befollow_account;
    }

    public void setBefollow_account(int befollow_account) {
        this.befollow_account = befollow_account;
    }

    public int getTeam_group_account() {
        return team_group_account;
    }

    public void setTeam_group_account(int team_group_account) {
        this.team_group_account = team_group_account;
    }

    public int getTeam_sign_account() {
        return team_sign_account;
    }

    public void setTeam_sign_account(int team_sign_account) {
        this.team_sign_account = team_sign_account;
    }

    public int getEvent_account() {
        return event_account;
    }

    public void setEvent_account(int event_account) {
        this.event_account = event_account;
    }

    public int getFollow_event_account() {
        return follow_event_account;
    }

    public void setFollow_event_account(int follow_event_account) {
        this.follow_event_account = follow_event_account;
    }

    public int getFollow_team_account() {
        return follow_team_account;
    }

    public void setFollow_team_account(int follow_team_account) {
        this.follow_team_account = follow_team_account;
    }

    private int befollow_account;
    private int team_group_account;
    private int team_sign_account;
    private int event_account;
    private int follow_event_account;
    private int follow_team_account;


    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMajor() {
        return this.major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDob() {
        return this.dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotto() {
        return this.motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
