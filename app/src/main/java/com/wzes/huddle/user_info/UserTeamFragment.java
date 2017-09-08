package com.wzes.huddle.user_info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class UserTeamFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Team> list;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;

    class C04981 implements Runnable {
        C04981() {
        }

        public void run() {
            initData();
        }
    }

    class C09292 implements Observer<List<Team>> {
        C09292() {
        }

        public void onCompleted() {
            recyclerView.setAdapter(new TeamInfoAdapter(UserTeamFragment.this, list));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Team> teams) {
            list = teams;
        }
    }

    public static UserTeamFragment newInstance(String param1, String param2) {
        UserTeamFragment fragment = new UserTeamFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_team, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.user_team_recyclerView);
        new Thread(new C04981()).start();
        return view;
    }

    public void initData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getTeamList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09292());
    }
}
