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
import com.wzes.huddle.C0479R;
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

public class TeamHotFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static boolean FirstLoad = true;
    private static List<Team> list;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TeamInfoAdapter teamInfoAdapter;

    class C04882 implements Runnable {
        C04882() {
        }

        public void run() {
            TeamHotFragment.this.initData();
        }
    }

    class C09131 implements OnRefreshListener {
        C09131() {
        }

        public void onRefresh() {
            TeamHotFragment.this.refreshData();
        }
    }

    class C09143 implements Observer<List<Team>> {
        C09143() {
        }

        public void onCompleted() {
            TeamHotFragment.this.teamInfoAdapter = new TeamInfoAdapter(TeamHotFragment.this, TeamHotFragment.list);
            TeamHotFragment.this.recyclerView.setAdapter(TeamHotFragment.this.teamInfoAdapter);
            TeamHotFragment.this.refreshLayout.setRefreshing(false);
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Team> teams) {
            TeamHotFragment.list = teams;
        }
    }

    class C09154 implements Observer<List<Team>> {
        C09154() {
        }

        public void onCompleted() {
            TeamHotFragment.this.refreshLayout.setRefreshing(false);
            TeamHotFragment.this.teamInfoAdapter = new TeamInfoAdapter(TeamHotFragment.this, TeamHotFragment.list);
            TeamHotFragment.this.recyclerView.setAdapter(TeamHotFragment.this.teamInfoAdapter);
            TeamHotFragment.this.recyclerView.setHasFixedSize(true);
            TeamHotFragment.this.recyclerView.setLayoutManager(new LinearLayoutManager(TeamHotFragment.this.getActivity()));
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Team> teams) {
            TeamHotFragment.list = teams;
        }
    }

    public static TeamHotFragment newInstance(String param1, String param2) {
        TeamHotFragment fragment = new TeamHotFragment();
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
        View view = inflater.inflate(C0479R.layout.fragment_team_item_hot, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(C0479R.id.team_hot_recyclerView);
        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(C0479R.id.team_hot_refreshLayout);
        this.refreshLayout.setColorSchemeResources(C0479R.color.colorPrimary);
        this.refreshLayout.setOnRefreshListener(new C09131());
        if (list == null) {
            this.refreshLayout.setRefreshing(true);
            new Thread(new C04882()).start();
        } else {
            this.teamInfoAdapter = new TeamInfoAdapter(this, list);
            this.recyclerView.setAdapter(this.teamInfoAdapter);
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return view;
    }

    public void refreshData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getTeamList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09143());
    }

    public void initData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getTeamList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09154());
    }
}
