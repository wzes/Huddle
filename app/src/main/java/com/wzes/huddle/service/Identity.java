package com.wzes.huddle.service;

import android.util.Log;
import com.google.gson.Gson;
import com.wzes.huddle.bean.User;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;

public class Identity {
    private static final OkHttpClient client = new OkHttpClient();

    public static boolean Login(String username, String password) {
        if (getUser(username).getPassword().equals(password)) {
            return true;
        }
        return isTongjiUser(username, password);
    }

    public static User getUser(String user_id) {
        try {
            String result = client.newCall(new Builder().url("http://59.110.136.134/huddle/user.php?user_id=" + user_id).get().build()).execute().body().string();
            if (result == null || result.equals("null")) {
                return null;
            }
            return (User) new Gson().fromJson(result, User.class);
        } catch (Exception e) {
            return null;
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
            Log.i("TTTT", "isTongjiUser: " + e.toString());
        }
        return false;
    }
}
