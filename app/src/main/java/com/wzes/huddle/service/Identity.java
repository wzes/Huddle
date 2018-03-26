package com.wzes.huddle.service;

import com.google.gson.Gson;
import com.wzes.huddle.bean.User;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;

public class Identity {
    private static final OkHttpClient client = new OkHttpClient();

    public static boolean Login(String username, String password) {
        User user = getUser(username);
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            return true;
        }
        return isTongjiUser(username, password);
    }

    public static User getUser(String user_id) {
        try {
            String result = client.newCall(new Builder().url("http://59.110.136.134:88/huddle/user.php?user_id=" + user_id).get().build()).execute().body().string();
            if (result == null || result.equals("null")) {
                return new User();
            }
            return new Gson().fromJson(result, User.class);
        } catch (Exception e) {
            return new User();
        }
    }

    public static boolean isTongjiUser(String username, String password) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("Ecom_User_ID", username).add("Ecom_Password", password);
        try {
            if (client.newCall(new Builder().url("https://ids.tongji.edu.cn:8443/nidp/app?sid=2").post(builder.build()).build()).execute().body().string().contains("top.location.href")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
