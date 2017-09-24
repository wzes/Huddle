package com.wzes.huddle.service;

import com.wzes.huddle.bean.ChatList;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.bean.Message;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.bean.User;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("huddle/addeventfollow.php")
    Observable<ResponseBody> addEventFollow(@Query("event_id") String str, @Query("user_id") String str2);

    @GET("huddle/addeventview.php")
    Observable<ResponseBody> addEventView(@Query("event_id") String str, @Query("user_id") String str2);

    @GET("huddle/messagelist.php")
    Observable<List<ChatList>> getChatListByID(@Query("user_id") String str);

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
    Observable<List<Team>> getUserTeamList(@Query("user_id") String user_id);

    @GET("huddle/newteams.php")
    Observable<List<Team>> getNewTeamList();

    @GET("huddle/hotteams.php")
    Observable<List<Team>> getHotTeamList();

    @GET("huddle/recteams.php")
    Observable<List<Team>> getUserRecList(@Query("user_id") String user_id);

    @GET("huddle/nearteams.php")
    Observable<List<Team>> getNearTeamList(@Query("latitude") String str, @Query("longitude") String str2);

    @GET("huddle/user.php")
    Observable<User> getUserByUername(@Query("user_id") String str);

    @GET("huddle/users.php")
    Observable<List<User>> getUsersByTeamID(@Query("team_id") String str);


    @POST("huddle/addteam.php")
    @Multipart
    Observable<ResponseBody> addTeam(@Part("user_id") RequestBody user_id, @Part("title") RequestBody title,
            @Part("category") RequestBody category, @Part("content") RequestBody content, @Part("start_date") RequestBody start_date,
            @Part("release_date") RequestBody release_date, @Part("join_acount") RequestBody join_acount, @Part("level") RequestBody level,
            @Part("locationname") RequestBody locationname, @Part("locationlatitude") RequestBody locationlatitude,
            @Part("locationlongitude") RequestBody locationlongitude);

    @POST("huddle/uploadteamimage.php")
    @Multipart
    Observable<ResponseBody> uploadTeamimage(@Part("team_id") RequestBody requestBody, @Part("index") RequestBody index,
                                             @Part MultipartBody.Part part);

    @POST("huddle/uploadimage.php")
    @Multipart
    Observable<ResponseBody> upLoad(@Part("user_id") RequestBody requestBody, @Part MultipartBody.Part part);

    @POST("huddle/uploadmessageimage.php")
    @Multipart
    Observable<ResponseBody> uploadMessageimage(@Part("message_id") RequestBody requestBody, @Part MultipartBody.Part part);
}
