package com.wzes.huddle.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wzes.huddle.R;
import com.wzes.huddle.activities.search.SearchEventActivity;
import com.wzes.huddle.adapter.EventAdapter;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.service.MyRetrofit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EventFragment extends Fragment {
    private static EventFragment eventFragment;
    private static boolean FirstLoad = true;
    @BindView(R.id.event_search_layout)
    FrameLayout eventSearchLayout;
    private List<Event> hotList;
    private List<Event> list;

    @BindView(R.id.event_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.event_refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private EventAdapter eventAdapter;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public EventFragment() {
    }

    public static EventFragment newInstance() {
        if (eventFragment == null) {
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
        MyRetrofit.getGsonRetrofit().getEventList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Event>>() {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        MyRetrofit.getGsonRetrofit().getHotEventList()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<Event>>() {
                                    @Override
                                    public void onComplete() {
                                        if (!(list.size() > 0 && list.get(0) != null)) {
                                            list = new ArrayList<>();
                                        }
                                        refreshLayout.setRefreshing(false);
                                        eventAdapter = new EventAdapter(EventFragment.this, list, hotList);
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(EventFragment.this.getActivity()));
                                        recyclerView.setAdapter(eventAdapter);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(List<Event> events) {
                                        hotList = events;
                                    }
                                });
                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(List<Event> events) {
                        list = events;
                    }
                });
    }

    public void refreshData() {
        MyRetrofit.getGsonRetrofit().getEventList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Event>>() {

                    @Override
                    public void onComplete() {
                        if (!(list.size() > 0 && list.get(0) != null)) {
                            list = new ArrayList<>();
                        }
                        eventAdapter = new EventAdapter(EventFragment.this, list, hotList);
                        recyclerView.setAdapter(eventAdapter);
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(List<Event> events) {
                        list = events;
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.event_search_layout)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), SearchEventActivity.class));
    }
}
