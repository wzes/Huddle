package com.wzes.huddle.team;

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
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TeamNewFragment extends Fragment {
    private static boolean FirstLoad = true;
    private static List<Team> list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TeamInfoAdapter teamInfoAdapter;
    private static TeamNewFragment fragment;


    private TeamNewFragment(){

    }

    public static TeamNewFragment newInstance() {
        if(fragment == null){
            fragment = new TeamNewFragment();
        }
        return fragment;
    }
    

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_item_new, container, false);
        recyclerView = view.findViewById(R.id.team_new_recyclerView);
        refreshLayout = view.findViewById(R.id.team_new_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this::refreshData);
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
        MyRetrofit.getGsonRetrofit().getNewTeamList()
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
                        teamInfoAdapter = new TeamInfoAdapter(TeamNewFragment.this, list);
                        recyclerView.setAdapter(teamInfoAdapter);
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    public void initData() {
        MyRetrofit.getGsonRetrofit().getNewTeamList()
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
                        refreshLayout.setRefreshing(false);
                        teamInfoAdapter = new TeamInfoAdapter(TeamNewFragment.this, list);
                        recyclerView.setAdapter(teamInfoAdapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });
    }
}
