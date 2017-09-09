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

public class TeamHotFragment extends Fragment {
    private static boolean FirstLoad = true;
    private static List<Team> list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TeamInfoAdapter teamInfoAdapter;




    public static TeamHotFragment newInstance(String param1, String param2) {
        TeamHotFragment fragment = new TeamHotFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_item_hot, container, false);
        this.recyclerView = view.findViewById(R.id.team_hot_recyclerView);
        this.refreshLayout = view.findViewById(R.id.team_hot_refreshLayout);
        this.refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.refreshLayout.setOnRefreshListener(() -> refreshData());
        if (list == null) {
            this.refreshLayout.setRefreshing(true);
            new Thread(this::initData).start();
        } else {
            this.teamInfoAdapter = new TeamInfoAdapter(this, list);
            this.recyclerView.setAdapter(this.teamInfoAdapter);
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return view;
    }

    public void refreshData() {
        MyRetrofit.getGsonRetrofit().getTeamList()
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
                        TeamHotFragment.this.teamInfoAdapter = new TeamInfoAdapter(TeamHotFragment.this, TeamHotFragment.list);
                        TeamHotFragment.this.recyclerView.setAdapter(TeamHotFragment.this.teamInfoAdapter);
                        TeamHotFragment.this.refreshLayout.setRefreshing(false);
                    }
                });
    }

    public void initData() {
        MyRetrofit.getGsonRetrofit().getTeamList()
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
                        TeamHotFragment.this.refreshLayout.setRefreshing(false);
                        TeamHotFragment.this.teamInfoAdapter = new TeamInfoAdapter(TeamHotFragment.this, TeamHotFragment.list);
                        TeamHotFragment.this.recyclerView.setAdapter(TeamHotFragment.this.teamInfoAdapter);
                        TeamHotFragment.this.recyclerView.setHasFixedSize(true);
                        TeamHotFragment.this.recyclerView.setLayoutManager(new LinearLayoutManager(TeamHotFragment.this.getActivity()));
                    }
                });
    }
}
