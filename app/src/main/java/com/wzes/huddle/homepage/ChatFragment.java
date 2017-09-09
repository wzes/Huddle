package com.wzes.huddle.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.GsonBuilder;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.ChatAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.ChatList;
import com.wzes.huddle.service.MyRetrofit;

import com.wzes.huddle.service.RetrofitService;
import com.wzes.huddle.util.MyLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChatFragment extends Fragment {
    private static ChatFragment chatFragment;
    private static ChatAdapter chatAdapter;
    private static List<ChatList> list;

    @BindView(R.id.chat_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.chat_refreshLayout) SwipeRefreshLayout refreshLayout;
    Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public static class Receiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            MyRetrofit.getGsonRetrofit()
                    .getChatListByID(Preferences.getUserAccount())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<ChatList>>() {
                        @Override
                        public void onComplete() {
                            chatAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(List<ChatList> chatLists) {
                            ChatFragment.list.clear();
                            for (ChatList e : chatLists) {
                                list.add(e);
                            }
                        }
                    });
        }
    }

    private ChatFragment(){
    }

    public static ChatFragment newInstance() {
        if(chatFragment == null){
            chatFragment = new ChatFragment();
        }
        return chatFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (list == null) {
            refreshLayout.setRefreshing(true);
            new Thread(this::initData).start();
        } else {
            chatAdapter = new ChatAdapter(this, list);
            recyclerView.setAdapter(chatAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        refreshLayout.setOnRefreshListener(this::refreshData);
        return view;
    }


    public void initData() {
        MyLog.i("Preferences.getUserAccount(): " + Preferences.getUserAccount());
        MyRetrofit.getGsonRetrofit()
                .getChatListByID(Preferences.getUserAccount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ChatList>>() {
                   @Override
                   public void onError(Throwable e) {
                   }

                   @Override
                   public void onSubscribe(@NonNull Disposable d) {

                   }

                   @Override
                   public void onNext(List<ChatList> chatLists) {
                       list = chatLists;
                   }

                   @Override
                   public void onComplete() {
                       MyLog.i("list.size() : " + list.size());
                       refreshLayout.setRefreshing(false);
                       chatAdapter = new ChatAdapter(ChatFragment.this, list);
                       recyclerView.setHasFixedSize(true);
                       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                       recyclerView.setAdapter(chatAdapter);
                   }
               });
    }

    public void refreshData() {
        MyRetrofit.getGsonRetrofit()
                .getChatListByID(Preferences.getUserAccount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ChatList>>() {
                    @Override
                    public void onComplete() {
                        refreshLayout.setRefreshing(false);
                        chatAdapter = new ChatAdapter(ChatFragment.this, list);
                        recyclerView.setAdapter(ChatFragment.chatAdapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatFragment.this.getActivity()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(List<ChatList> chatLists) {
                        list = chatLists;
                    }
                });
    }
}
