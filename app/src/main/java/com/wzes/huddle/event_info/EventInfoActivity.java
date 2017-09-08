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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wzes.huddle.C0479R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.imageloader.ImageViewActivity;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.service.RetrofitService;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventInfoActivity extends AppCompatActivity implements OnClickListener {
    private static Event myEvent;
    private RetrofitService Gsonservice;
    private RetrofitService Normalservice;
    @BindView(2131624073)
    public CollapsingToolbarLayout collapsing;
    @BindView(2131624078)
    public TextView contentTxt;
    @BindView(2131624079)
    public FloatingActionButton createTeam;
    private String event_id;
    @BindView(2131624074)
    public ImageView imageView;
    @BindView(2131624077)
    public TextView riceTxt;
    @BindView(2131624076)
    public TextView signTxt;
    @BindView(2131624075)
    public Toolbar toolBar;

    class C08971 implements Observer<ResponseBody> {
        C08971() {
        }

        public void onCompleted() {
        }

        public void onError(Throwable e) {
        }

        public void onNext(ResponseBody responseBody) {
        }
    }

    class C08982 implements Observer<Event> {
        C08982() {
        }

        public void onCompleted() {
            Glide.with(EventInfoActivity.this.getApplicationContext()).load(EventInfoActivity.myEvent.getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(EventInfoActivity.this.imageView);
            EventInfoActivity.this.collapsing.setTitle(EventInfoActivity.myEvent.getTitle() + "(" + EventInfoActivity.myEvent.getLevel() + ")");
            EventInfoActivity.this.signTxt.setText("报名时间 " + EventInfoActivity.myEvent.getEnrool_start_date() + " - " + EventInfoActivity.myEvent.getEnrool_end_date());
            EventInfoActivity.this.riceTxt.setText("比赛时间 " + EventInfoActivity.myEvent.getMatch_start_date() + " - " + EventInfoActivity.myEvent.getMatch_end_date());
            EventInfoActivity.this.contentTxt.setText(EventInfoActivity.myEvent.getContent());
            EventInfoActivity.this.imageView.setOnClickListener(EventInfoActivity$2$$Lambda$1.lambdaFactory$(this));
        }

        private /* synthetic */ void lambda$onCompleted$0(View v) {
            Intent intent1 = new Intent(EventInfoActivity.this, ImageViewActivity.class);
            intent1.putExtra("uri", EventInfoActivity.myEvent.getImage());
            EventInfoActivity.this.startActivity(intent1);
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(Event event) {
            EventInfoActivity.myEvent = event;
        }
    }

    class C08993 implements Observer<ResponseBody> {
        C08993() {
        }

        public void onCompleted() {
            Toast.makeText(EventInfoActivity.this, "关注成功", 0).show();
        }

        public void onError(Throwable e) {
        }

        public void onNext(ResponseBody responseBody) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0479R.layout.activity_event_read);
        ButterKnife.bind((Activity) this);
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
        this.Normalservice.addEventView(this.event_id, Preferences.getUserAccount()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C08971());
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C08982());
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
            case C0479R.id.event_read_create /*2131624079*/:
                this.Normalservice = MyRetrofit.getNormalRetrofit();
                this.Normalservice.addEventFollow(this.event_id, Preferences.getUserAccount()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C08993());
                return;
            default:
                return;
        }
    }
}
