package com.wzes.huddle.activities.team;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.TeamInfoAdapter;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.service.MyRetrofit;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TeamHotFragment extends Fragment {
    private static boolean FirstLoad = true;
    private static List<Team> list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TeamInfoAdapter teamInfoAdapter;
    private static TeamHotFragment fragment;


    private TeamHotFragment(){

    }

    public static TeamHotFragment newInstance() {
        if(fragment == null){
            fragment = new TeamHotFragment();
        }
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_item_hot, container, false);
        recyclerView = view.findViewById(R.id.team_hot_recyclerView);
        refreshLayout = view.findViewById(R.id.team_hot_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(() -> refreshData());
        if (list == null) {
            refreshLayout.setRefreshing(true);
            new Thread(this::initData).start();
        } else {
            teamInfoAdapter = new TeamInfoAdapter(this, list);
            recyclerView.setAdapter(teamInfoAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return view;
    }

    public void refreshData() {
        MyRetrofit.getGsonRetrofit().getHotTeamList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Team>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Team> teams) {
                        list = teams;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if(!(list.size() > 0 && list.get(0) != null)){
                            list = new ArrayList<>();
                        }
                        teamInfoAdapter = new TeamInfoAdapter(TeamHotFragment.this, TeamHotFragment.list);
                        recyclerView.setAdapter(teamInfoAdapter);
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    public void initData() {
        MyRetrofit.getGsonRetrofit().getHotTeamList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Team>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Team> teams) {
                        list = teams;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if(!(list.size() > 0 && list.get(0) != null)){
                            list = new ArrayList<>();
                        }
                        refreshLayout.setRefreshing(false);
                        teamInfoAdapter = new TeamInfoAdapter(TeamHotFragment.this, TeamHotFragment.list);
                        recyclerView.setAdapter(teamInfoAdapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });
    }
}
