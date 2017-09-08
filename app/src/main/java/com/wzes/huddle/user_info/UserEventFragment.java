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
import com.wzes.huddle.adapter.UserEventAdapter;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.service.RetrofitService;
import java.util.List;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserEventFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Event> list;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;

    class C04931 implements Runnable {
        C04931() {
        }

        public void run() {
            UserEventFragment.this.initData();
        }
    }

    class C09272 implements Observer<List<Event>> {
        C09272() {
        }

        public void onCompleted() {
            UserEventFragment.this.recyclerView.setAdapter(new UserEventAdapter(UserEventFragment.this, UserEventFragment.this.list));
            UserEventFragment.this.recyclerView.setHasFixedSize(true);
            UserEventFragment.this.recyclerView.setLayoutManager(new LinearLayoutManager(UserEventFragment.this.getActivity()));
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Event> events) {
            UserEventFragment.this.list = events;
        }
    }

    public static UserEventFragment newInstance(String param1, String param2) {
        UserEventFragment fragment = new UserEventFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_event, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.user_event_recyclerView);
        new Thread(new C04931()).start();
        return view;
    }

    public void initData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getEventList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09272());
    }
}
