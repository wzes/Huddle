package com.wzes.huddle.activities.myevent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;

import com.wzes.huddle.R;
import com.wzes.huddle.adapter.UserEventAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.service.MyRetrofit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyEventActivity extends AppCompatActivity {

    @BindView(R.id.my_event_back)
    ImageButton myEventBack;
    @BindView(R.id.my_event_recycler)
    RecyclerView myEventRecycler;

    private List<Event> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);
        ButterKnife.bind(this);
        initData();
    }

    public void initData() {
        MyRetrofit.getGsonRetrofit().getUserEventList(Preferences.getUserAccount())
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
                        myEventRecycler.setAdapter(new UserEventAdapter(getApplicationContext(), list));
                        myEventRecycler.setHasFixedSize(true);
                        myEventRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                });
    }

    @OnClick(R.id.my_event_back)
    public void onViewClicked() {
        finish();
    }
}
