package com.wzes.huddle.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.GsonBuilder;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.ChatAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Chat;
import com.wzes.huddle.service.RetrofitService;
import java.util.List;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatFragment extends Fragment {
    private static ChatAdapter chatAdapter;
    private static List<Chat> list;
    private static RecyclerView recyclerView;
    private static SwipeRefreshLayout refreshLayout;
    public static class Receiver extends BroadcastReceiver {

        class C09021 implements Observer<List<Chat>> {
            C09021() {
            }

            public void onCompleted() {
                ChatFragment.chatAdapter.notifyDataSetChanged();
            }

            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onNext(List<Chat> chats) {
                ChatFragment.list.clear();
                for (Chat e : chats) {
                    ChatFragment.list.add(e);
                }
            }
        }

        public void onReceive(Context context, Intent intent) {
            Log.i("TTTT", "onReceive: 新消息");
            ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getChatListByID(Preferences.getUserAccount()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09021());
        }
    }

    class C09001 implements Observer<List<Chat>> {
        C09001() {
        }

        public void onCompleted() {
            ChatFragment.refreshLayout.setRefreshing(false);
            ChatFragment.chatAdapter = new ChatAdapter(ChatFragment.this, ChatFragment.list);
            ChatFragment.recyclerView.setAdapter(ChatFragment.chatAdapter);
            ChatFragment.recyclerView.setHasFixedSize(true);
            ChatFragment.recyclerView.setLayoutManager(new LinearLayoutManager(ChatFragment.this.getActivity()));
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Chat> events) {
            ChatFragment.list = events;
        }
    }

    class C09012 implements Observer<List<Chat>> {
        C09012() {
        }

        public void onCompleted() {
            ChatFragment.refreshLayout.setRefreshing(false);
            ChatFragment.chatAdapter = new ChatAdapter(ChatFragment.this, ChatFragment.list);
            ChatFragment.recyclerView.setAdapter(ChatFragment.chatAdapter);
            ChatFragment.recyclerView.setHasFixedSize(true);
            ChatFragment.recyclerView.setLayoutManager(new LinearLayoutManager(ChatFragment.this.getActivity()));
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Chat> events) {
            ChatFragment.list = events;
        }
    }

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_recyclerView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.chat_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        if (list == null || !Preferences.getLastUserAccount().equals(Preferences.getUserAccount())) {
            refreshLayout.setRefreshing(true);
            initData();
        } else {
            chatAdapter = new ChatAdapter(this, list);
            recyclerView.setAdapter(chatAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        refreshLayout.setOnRefreshListener(() -> {

        });
        return view;
    }

    private /* synthetic */ void lambda$onCreateView$0() {
        refreshData();
    }

    public void initData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getChatListByID(Preferences.getUserAccount()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09001());
    }

    public void refreshData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getChatListByID(Preferences.getUserAccount()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09012());
    }
}
