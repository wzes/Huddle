package com.wzes.huddle.team;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.GsonBuilder;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.TeamInfoAdapter;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.service.RetrofitService;
import java.util.List;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TeamRecFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static List<Team> list;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TeamInfoAdapter teamInfoAdapter;

    class C04912 implements Runnable {
        C04912() {
        }

        public void run() {
            TeamRecFragment.this.initData();
        }
    }

    class C09221 implements OnRefreshListener {
        C09221() {
        }

        public void onRefresh() {
            TeamRecFragment.this.refreshData();
        }
    }

    class C09233 implements Observer<List<Team>> {
        C09233() {
        }

        public void onCompleted() {
            TeamRecFragment.this.teamInfoAdapter = new TeamInfoAdapter(TeamRecFragment.this, TeamRecFragment.list);
            TeamRecFragment.this.recyclerView.setAdapter(TeamRecFragment.this.teamInfoAdapter);
            TeamRecFragment.this.refreshLayout.setRefreshing(false);
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Team> teams) {
            TeamRecFragment.list = teams;
        }
    }

    class C09244 implements Observer<List<Team>> {
        C09244() {
        }

        public void onCompleted() {
            TeamRecFragment.this.refreshLayout.setRefreshing(false);
            TeamRecFragment.this.teamInfoAdapter = new TeamInfoAdapter(TeamRecFragment.this, TeamRecFragment.list);
            TeamRecFragment.this.recyclerView.setAdapter(TeamRecFragment.this.teamInfoAdapter);
            TeamRecFragment.this.recyclerView.setHasFixedSize(true);
            TeamRecFragment.this.recyclerView.setLayoutManager(new LinearLayoutManager(TeamRecFragment.this.getActivity()));
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Team> teams) {
            TeamRecFragment.list = teams;
        }
    }

    public static TeamRecFragment newInstance(String param1, String param2) {
        TeamRecFragment fragment = new TeamRecFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_item_rec, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.team_rec_recyclerView);
        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.team_rec_refreshLayout);
        this.refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.refreshLayout.setOnRefreshListener(new C09221());
        if (list == null) {
            this.refreshLayout.setRefreshing(true);
            new Thread(new C04912()).start();
        } else {
            this.teamInfoAdapter = new TeamInfoAdapter(this, list);
            this.recyclerView.setAdapter(this.teamInfoAdapter);
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return view;
    }

    public void refreshData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getTeamList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09233());
    }

    public void initData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getTeamList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09244());
    }
}