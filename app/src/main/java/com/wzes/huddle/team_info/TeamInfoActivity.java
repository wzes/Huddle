package com.wzes.huddle.team_info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.baidu.mapapi.UIMsg.m_AppUI;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Image;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.chatservice.ChatActivity;
import com.wzes.huddle.imageloader.ImageViewActivity;
import com.wzes.huddle.service.RetrofitService;
import com.wzes.huddle.user_info.UserInfoActivity;
import com.wzes.huddle.util.AppManager;
import com.wzes.huddle.util.GlideImageLoader;
import com.youth.banner.Banner;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TeamInfoActivity extends AppCompatActivity {
    @BindView(R.id.team_info_banner)
    public Banner banner;
    @BindView(R.id.team_info_category)
    public TextView categoryTxt;
    @BindView(R.id.team_info_user_img)
    public CircleImageView circleImageView;
    @BindView(R.id.team_info_collapsing)
    public CollapsingToolbarLayout collapsing;
    @BindView(R.id.team_info_content)
    public TextView contentTxt;
    @BindView(R.id.team_info_follow)
    public Button followBtn;
    @BindView(R.id.team_info_user_follow)
    public TextView followTxt;
    @BindView(R.id.team_info_user_info)
    public TextView infoTxt;
    @BindView(R.id.team_info_location_back)
    public TextView locationBtn;
    @BindView(R.id.team_info_locationname)
    public TextView locationTxt;
    @BindView(R.id.team_info_user_major)
    public TextView majorTxt;

    private Team myTeam;
    @BindView(R.id.team_info_user_name)
    public TextView nameTxt;

    @BindView(R.id.team_info_people)
    public TextView peopleTxt;

    @BindView(R.id.team_info_user_recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.team_info_user_save)
    public LinearLayout saveBtn;
    @BindView(R.id.team_info_user_sign)
    public Button signBtn;
    @BindView(R.id.team_info_user_talk)
    public LinearLayout talkBtn;
    private String team_id;
    @BindView(R.id.team_info_start_time)
    public TextView timeTxt;
    @BindView(R.id.team_info_toolbar)
    public Toolbar toolBar;
    @BindView(R.id.team_info_user_layout)
    public LinearLayout userBtn;

    class C09261 implements Observer<Team> {
        C09261() {
        }

        public void onCompleted() {
            collapsing.setTitle(myTeam.getTitle());
            final List<String> images = new ArrayList();
            for (Image img : myTeam.getImages()) {
                images.add(img.getImage());
            }
            Glide.with(TeamInfoActivity.this).load(myTeam.getImage()).into(circleImageView);
            infoTxt.setText(myTeam.getInfo());
            banner.setImages(images).setDelayTime(m_AppUI.MSG_APP_GPS).setIndicatorGravity(6).setImageLoader(new GlideImageLoader()).start();
            banner.setOnBannerListener(position -> {
                Intent intent = new Intent(TeamInfoActivity.this, ImageViewActivity.class);
                intent.putExtra("uri", (String) images.get(position));
                startActivity(intent);
            });
            categoryTxt.setText("——" + myTeam.getCategory().toString());
            nameTxt.setText(myTeam.getName());
            contentTxt.setText("——" + myTeam.getContent());
            timeTxt.setText("——" + myTeam.getStart_date());
            peopleTxt.setText("组员" + myTeam.getJoin_people() + "人已报名(还差" + ((myTeam.getJoin_acount() - myTeam.getJoin_people()) - 1) + "人)");
            userBtn.setOnClickListener(v -> {
                Intent nIntent = new Intent(TeamInfoActivity.this, UserInfoActivity.class);
                nIntent.putExtra("user_id", myTeam.getUser_id());
                startActivity(nIntent);
            });
            locationTxt.setText("——" + myTeam.getLocationname());
            locationBtn.setOnClickListener(v -> {
                Intent nIntent = new Intent(TeamInfoActivity.this, TeamInfoLocationActivity.class);
                nIntent.putExtra("title", myTeam.getLocationname());
                nIntent.putExtra("longitude", myTeam.getLocationlongitude());
                nIntent.putExtra("latitude", myTeam.getLocationlatitude());
                startActivity(nIntent);
            });
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(Team team) {
            myTeam = team;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_team_info);
        AppManager.getAppManager().addActivity(this);
        this.team_id = getIntent().getStringExtra("team_id");
        if (this.team_id == null) {
            this.team_id = "1";
        }
        ButterKnife.bind(this);
        new Builder().baseUrl("http://59.110.136.134/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(RetrofitService.class).getTeam(this.team_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new C09261());
        setSupportActionBar(this.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.signBtn.setOnClickListener(view -> Toast.makeText(this, "报名成功", 0).show());
        this.talkBtn.setOnClickListener(view -> {
            if (this.myTeam.getUser_id().equals(Preferences.getUserAccount())) {
                Toast.makeText(this, "不能和自己聊天哦！", 0).show();
                return;
            }
            Intent intent1 = new Intent(this, ChatActivity.class);
            intent1.putExtra("to_id", this.myTeam.getUser_id());
            intent1.putExtra("to_name", this.myTeam.getName());
            startActivity(intent1);
        });
    }
    


}
