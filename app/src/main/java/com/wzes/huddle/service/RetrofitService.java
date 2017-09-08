package com.wzes.huddle.service;

import com.wzes.huddle.bean.Chat;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.bean.Message;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.bean.User;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitService {
    @GET("huddle/addeventfollow.php")
    Observable<ResponseBody> addEventFollow(@Query("event_id") String str, @Query("user_id") String str2);

    @GET("huddle/addeventview.php")
    Observable<ResponseBody> addEventView(@Query("event_id") String str, @Query("user_id") String str2);

    @GET("huddle/messagelist.php")
    Observable<List<Chat>> getChatListByID(@Query("user_id") String str);

    @GET("huddle/event.php")
    Observable<Event> getEventById(@Query("event_id") String str);

    @GET("huddle/events.php")
    Observable<List<Event>> getEventList();

    @GET("huddle/hotevents.php")
    Observable<List<Event>> getHotEventList();

    @GET("huddle/messages.php")
    Observable<List<Message>> getMessageListByID(@Query("from_id") String str, @Query("to_id") String str2);

    @GET("huddle/team.php")
    Observable<Team> getTeam(@Query("team_id") String str);

    @GET("huddle/teams.php")
    Observable<List<Team>> getTeamList();

    @GET("huddle/user.php")
    Observable<User> getUserByUername(@Query("user_id") String str);

    @GET("huddle/users.php")
    Observable<List<User>> getUsersByTeamID(@Query("team_id") String str);

    @POST("huddle/uploadimage.php")
    @Multipart
    Observable<ResponseBody> upLoad(@Part("user_id") RequestBody requestBody, @Part MultipartBody.Part part);
}
