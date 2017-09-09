package com.wzes.huddle.event_info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.service.RetrofitService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class EventInfoActivity extends AppCompatActivity implements OnClickListener {
    private static Event myEvent;
    private RetrofitService Gsonservice;
    private RetrofitService Normalservice;
    @BindView(R.id.event_read_collapsing) CollapsingToolbarLayout collapsing;
    @BindView(R.id.event_read_content) TextView contentTxt;
    @BindView(R.id.event_read_create) FloatingActionButton createTeam;
    private String event_id;
    @BindView(R.id.event_read_image) ImageView imageView;
    @BindView(R.id.event_read_riceTime) TextView riceTxt;
    @BindView(R.id.event_read_signTime) TextView signTxt;
    @BindView(R.id.event_read_toolbar) Toolbar toolBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_read);
        ButterKnife.bind(this);
        this.event_id = getIntent().getStringExtra("event_id");
        setSupportActionBar(this.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.createTeam.setOnClickListener(this);
        this.Normalservice = MyRetrofit.getNormalRetrofit();
        this.Gsonservice = MyRetrofit.getGsonRetrofit();
        Observable<Event> observable = this.Gsonservice.getEventById(this.event_id);
        this.Normalservice.addEventView(this.event_id, Preferences.getUserAccount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Event event) {
                        myEvent = event;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Glide.with(getApplicationContext()).load(EventInfoActivity.myEvent.getImage()).into(imageView);
                        collapsing.setTitle(EventInfoActivity.myEvent.getTitle() + "(" + EventInfoActivity.myEvent.getLevel() + ")");
                        signTxt.setText("报名时间 " + EventInfoActivity.myEvent.getEnrool_start_date() + " - " + EventInfoActivity.myEvent.getEnrool_end_date());
                        riceTxt.setText("比赛时间 " + EventInfoActivity.myEvent.getMatch_start_date() + " - " + EventInfoActivity.myEvent.getMatch_end_date());
                        contentTxt.setText(EventInfoActivity.myEvent.getContent());
                        imageView.setOnClickListener(EventInfoActivity.this);
                    }
                });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_read_create /*2131624079*/:
                this.Normalservice = MyRetrofit.getNormalRetrofit();
                this.Normalservice.addEventFollow(this.event_id, Preferences.getUserAccount())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ResponseBody>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull ResponseBody responseBody) {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(EventInfoActivity.this, "关注成功", 0).show();
                            }
                        });
                return;
            default:
                return;
        }
    }
}
