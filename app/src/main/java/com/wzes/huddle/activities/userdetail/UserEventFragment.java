package com.wzes.huddle.activities.userdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wzes.huddle.R;
import com.wzes.huddle.adapter.UserEventAdapter;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.service.MyRetrofit;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserEventFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Event> list;
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;


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
        this.recyclerView = view.findViewById(R.id.user_event_recyclerView);
        new Thread(this::initData).start();
        return view;
    }

    public void initData() {
       MyRetrofit.getGsonRetrofit().getUserEventList(mParam2)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Observer<List<Event>>() {
                   @Override
                   public void onSubscribe(@NonNull Disposable d) {

                   }

                   @Override
                   public void onNext(@NonNull List<Event> events) {
                       list = events;
                   }

                   @Override
                   public void onError(@NonNull Throwable e) {

                   }

                   @Override
                   public void onComplete() {
                       recyclerView.setAdapter(new UserEventAdapter(UserEventFragment.this, list));
                       recyclerView.setHasFixedSize(true);
                       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                   }
               });
    }
}
