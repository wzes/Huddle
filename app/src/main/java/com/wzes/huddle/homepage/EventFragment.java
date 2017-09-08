package com.wzes.huddle.homepage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import com.google.gson.GsonBuilder;
import com.wzes.huddle.C0479R;
import com.wzes.huddle.adapter.EventAdapter;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.service.RetrofitService;
import com.youth.banner.Banner;
import java.util.List;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static boolean FirstLoad = true;
    private static List<Event> hotList;
    private static List<Event> list;
    public Banner banner;
    private EventAdapter eventAdapter;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private SearchView searchView;
    private Toolbar toolbar;

    class C09041 implements Observer<List<Event>> {

        class C09031 implements Observer<List<Event>> {
            C09031() {
            }

            public void onCompleted() {
                EventFragment.this.refreshLayout.setRefreshing(false);
                EventFragment.this.eventAdapter = new EventAdapter(EventFragment.this, EventFragment.list, EventFragment.hotList);
                EventFragment.this.recyclerView.setAdapter(EventFragment.this.eventAdapter);
                EventFragment.this.recyclerView.setHasFixedSize(true);
                EventFragment.this.recyclerView.setLayoutManager(new LinearLayoutManager(EventFragment.this.getActivity()));
            }

            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onNext(List<Event> events) {
                EventFragment.hotList = events;
            }
        }

        C09041() {
        }

        public void onCompleted() {
            ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getHotEventList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09031());
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Event> events) {
            EventFragment.list = events;
        }
    }

    class C09052 implements Observer<List<Event>> {
        C09052() {
        }

        public void onCompleted() {
            EventFragment.this.eventAdapter = new EventAdapter(EventFragment.this, EventFragment.list, EventFragment.hotList);
            EventFragment.this.recyclerView.setAdapter(EventFragment.this.eventAdapter);
            EventFragment.this.refreshLayout.setRefreshing(false);
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(List<Event> events) {
            EventFragment.list = events;
        }
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
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

    public void initData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getEventList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09041());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0479R.layout.fragment_event, container, false);
        this.searchView = (SearchView) view.findViewById(C0479R.id.event_searchview);
        this.searchView.bringToFront();
        this.recyclerView = (RecyclerView) view.findViewById(C0479R.id.event_recyclerView);
        this.refreshLayout = (SwipeRefreshLayout) view.findViewById(C0479R.id.event_refreshLayout);
        this.refreshLayout.setColorSchemeResources(C0479R.color.colorPrimary);
        this.refreshLayout.setOnRefreshListener(EventFragment$$Lambda$1.lambdaFactory$(this));
        if (FirstLoad) {
            this.refreshLayout.setRefreshing(true);
            new Thread(EventFragment$$Lambda$2.lambdaFactory$(this)).start();
            FirstLoad = false;
        } else {
            this.eventAdapter = new EventAdapter(this, list, hotList);
            this.recyclerView.setAdapter(this.eventAdapter);
            this.recyclerView.setHasFixedSize(true);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        setHasOptionsMenu(true);
        return view;
    }

    private /* synthetic */ void lambda$onCreateView$0() {
        refreshData();
    }

    private /* synthetic */ void lambda$onCreateView$1() {
        initData();
    }

    public void refreshData() {
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getEventList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09052());
    }
}
