package com.wzes.huddle.homepage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.google.gson.GsonBuilder;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.EventAdapter;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.service.RetrofitService;
import com.youth.banner.Banner;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventFragment extends Fragment {
    private static EventFragment eventFragment;
    private static boolean FirstLoad = true;
    private static List<Event> hotList;
    private static List<Event> list;

    @BindView(R.id.event_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.event_refreshLayout) SwipeRefreshLayout refreshLayout;

    private EventAdapter eventAdapter;

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    private EventFragment(){
        eventFragment = new EventFragment();
    }

    public static EventFragment newInstance() {
        if(eventFragment == null){
            eventFragment = new EventFragment();
        }
        return eventFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setOnRefreshListener(this::refreshData);

        if (FirstLoad) {
            refreshLayout.setRefreshing(true);
            new Thread(this::initData).start();
            FirstLoad = false;
        } else {
            eventAdapter = new EventAdapter(this, list, hotList);
            recyclerView.setAdapter(eventAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        setHasOptionsMenu(true);
        return view;
    }
    public void initData() {
        new Builder().baseUrl("http://59.110.136.134/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(RetrofitService.class).getEventList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {
                        new Builder().baseUrl("http://59.110.136.134/")
                                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                .build().create(RetrofitService.class).getHotEventList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<Event>>() {
                                    @Override
                                    public void onCompleted() {
                                        refreshLayout.setRefreshing(false);
                                        eventAdapter = new EventAdapter(EventFragment.this, EventFragment.list, EventFragment.hotList);
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(EventFragment.this.getActivity()));
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(List<Event> events) {
                                        hotList = events;
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Event> events) {
                        list = events;
                    }
                });
    }

    public void refreshData() {
        new Builder().baseUrl("http://59.110.136.134/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(RetrofitService.class).getEventList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Event>>(){

                    @Override
                    public void onCompleted() {
                        eventAdapter = new EventAdapter(EventFragment.this, list, hotList);
                        recyclerView.setAdapter(eventAdapter);
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Event> events) {
                        list = events;
                    }
                });
    }
}
