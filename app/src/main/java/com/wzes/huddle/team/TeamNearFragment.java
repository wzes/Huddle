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

public class TeamNearFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static List<Team> list;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TeamInfoAdapter teamInfoAdapter;

    class C04892 implements Runnable {
        C04892() {
        }

        public void run() {
            TeamNearFragment.this.initData();
        }
    }

    class C09161 implements OnRefreshListener {
        C09161() {
        }

        public void onRefresh() {
            TeamNearFragment.this.refreshData();
        }
    }

    class C09173 implements Observer<List<Team>> {
        C09173() {
        }

        public void onCompleted() {
            TeamNearFragment.this.teamInfoAdapter = new TeamInfoAdapter(TeamNearFragment.this, TeamNearFragment.list);
            TeamNearFragment.this.recyclerView.setAdapter(TeamNearFragment.this.teamInfoAdapter);
            TeamNearFragment.this.refreshLayout.setRefreshing(false);
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Team> teams) {
            TeamNearFragment.list = teams;
        }
    }

    class C09184 implements Observer<List<Team>> {
        C09184() {
        }

        public void onCompleted() {
            TeamNearFragment.this.refreshLayout.setRefreshing(false);
            TeamNearFragment.this.teamInfoAdapter = new TeamInfoAdapter(TeamNearFragment.this, TeamNearFragment.list);
            TeamNearFragment.this.recyclerView.setAdapter(TeamNearFragment.this.teamInfoAdapter);
            TeamNearFragment.this.recyclerView.setHasFixedSize(true);
            TeamNearFragment.this.recyclerView.setLayoutManager(new LinearLayoutManager(TeamNearFragment.this.getActivity()));
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Team> teams) {
            TeamNearFragment.list = teams;
        }
    }

    public static TeamNearFragment newInstance(String param1, String param2) {
        TeamNearFragment fragment = new TeamNearFragment();
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
        View view = inflater.inflate(R.layout.fragment_team_item_near, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.team_near_recyclerView);
        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.team_near_refreshLayout);
        this.refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        this.refreshLayout.setOnRefreshListener(new C09161());
        if (list == null) {
            this.refreshLayout.setRefreshing(true);
            new Thread(new C04892()).start();
        } else {
            this.teamInfoAdapter = new TeamInfoAdapter(this, list);
            this.recyclerView.setAdapter(this.teamInfoAdapter);
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return view;
    }

    public void refreshData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getTeamList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09173());
    }

    public void initData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getTeamList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09184());
    }
}
