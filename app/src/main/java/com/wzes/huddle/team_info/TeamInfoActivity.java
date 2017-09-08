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
import com.wzes.huddle.C0479R;
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
import com.youth.banner.listener.OnBannerListener;
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
    private static final String TAG = "TTTT";
    @BindView(2131624116)
    public Banner banner;
    @BindView(2131624121)
    public TextView categoryTxt;
    @BindView(2131624127)
    public CircleImageView circleImageView;
    @BindView(2131624115)
    public CollapsingToolbarLayout collapsing;
    @BindView(2131624119)
    public TextView contentTxt;
    @BindView(2131624130)
    public Button followBtn;
    @BindView(2131624131)
    public TextView followTxt;
    @BindView(2131624132)
    public TextView infoTxt;
    @BindView(2131624123)
    public TextView locationBtn;
    @BindView(2131624122)
    public TextView locationTxt;
    @BindView(2131624129)
    public TextView majorTxt;
    private Team myTeam;
    @BindView(2131624128)
    public TextView nameTxt;
    @BindView(2131624124)
    public TextView peopleTxt;
    @BindView(2131624125)
    public RecyclerView recyclerView;
    @BindView(2131624134)
    public LinearLayout saveBtn;
    @BindView(2131624135)
    public Button signBtn;
    @BindView(2131624133)
    public LinearLayout talkBtn;
    private String team_id;
    @BindView(2131624120)
    public TextView timeTxt;
    @BindView(2131624117)
    public Toolbar toolBar;
    @BindView(2131624126)
    public LinearLayout userBtn;

    class C09261 implements Observer<Team> {
        C09261() {
        }

        public void onCompleted() {
            TeamInfoActivity.this.collapsing.setTitle(TeamInfoActivity.this.myTeam.getTitle());
            final List<String> images = new ArrayList();
            for (Image img : TeamInfoActivity.this.myTeam.getImages()) {
                images.add(img.getImage());
            }
            Glide.with(TeamInfoActivity.this).load(TeamInfoActivity.this.myTeam.getImage()).into(TeamInfoActivity.this.circleImageView);
            TeamInfoActivity.this.infoTxt.setText(TeamInfoActivity.this.myTeam.getInfo());
            TeamInfoActivity.this.banner.setImages(images).setDelayTime(m_AppUI.MSG_APP_GPS).setIndicatorGravity(6).setImageLoader(new GlideImageLoader()).start();
            TeamInfoActivity.this.banner.setOnBannerListener(new OnBannerListener() {
                public void OnBannerClick(int position) {
                    Intent intent = new Intent(TeamInfoActivity.this, ImageViewActivity.class);
                    intent.putExtra("uri", (String) images.get(position));
                    TeamInfoActivity.this.startActivity(intent);
                }
            });
            TeamInfoActivity.this.categoryTxt.setText("——" + TeamInfoActivity.this.myTeam.getCategory().toString());
            TeamInfoActivity.this.nameTxt.setText(TeamInfoActivity.this.myTeam.getName());
            TeamInfoActivity.this.contentTxt.setText("——" + TeamInfoActivity.this.myTeam.getContent());
            TeamInfoActivity.this.timeTxt.setText("——" + TeamInfoActivity.this.myTeam.getStart_date());
            TeamInfoActivity.this.peopleTxt.setText("组员" + TeamInfoActivity.this.myTeam.getJoin_people() + "人已报名(还差" + ((TeamInfoActivity.this.myTeam.getJoin_acount() - TeamInfoActivity.this.myTeam.getJoin_people()) - 1) + "人)");
            TeamInfoActivity.this.userBtn.setOnClickListener(TeamInfoActivity$1$$Lambda$1.lambdaFactory$(this));
            TeamInfoActivity.this.locationTxt.setText("——" + TeamInfoActivity.this.myTeam.getLocationname());
            TeamInfoActivity.this.locationBtn.setOnClickListener(TeamInfoActivity$1$$Lambda$2.lambdaFactory$(this));
        }

        private /* synthetic */ void lambda$onCompleted$0(View v) {
            Intent nIntent = new Intent(TeamInfoActivity.this, UserInfoActivity.class);
            nIntent.putExtra("user_id", TeamInfoActivity.this.myTeam.getUser_id());
            TeamInfoActivity.this.startActivity(nIntent);
        }

        private /* synthetic */ void lambda$onCompleted$1(View v) {
            Intent nIntent = new Intent(TeamInfoActivity.this, TeamInfoLocationActivity.class);
            nIntent.putExtra("title", TeamInfoActivity.this.myTeam.getLocationname());
            nIntent.putExtra("longitude", TeamInfoActivity.this.myTeam.getLocationlongitude());
            nIntent.putExtra("latitude", TeamInfoActivity.this.myTeam.getLocationlatitude());
            TeamInfoActivity.this.startActivity(nIntent);
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(Team team) {
            TeamInfoActivity.this.myTeam = team;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0479R.layout.activity_team_info);
        AppManager.getAppManager().addActivity(this);
        this.team_id = getIntent().getStringExtra("team_id");
        if (this.team_id == null) {
            this.team_id = "1";
        }
        ButterKnife.bind((Activity) this);
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new Gson())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getTeam(this.team_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09261());
        setSupportActionBar(this.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.signBtn.setOnClickListener(TeamInfoActivity$$Lambda$1.lambdaFactory$(this));
        this.talkBtn.setOnClickListener(TeamInfoActivity$$Lambda$2.lambdaFactory$(this));
    }

    private /* synthetic */ void lambda$onCreate$0(View v) {
        Toast.makeText(this, "报名成功", 0).show();
    }

    private /* synthetic */ void lambda$onCreate$1(View v) {
        if (this.myTeam.getUser_id().equals(Preferences.getUserAccount())) {
            Toast.makeText(this, "不能和自己聊天哦！", 0).show();
            return;
        }
        Intent intent1 = new Intent(this, ChatActivity.class);
        intent1.putExtra("to_id", this.myTeam.getUser_id());
        intent1.putExtra(HttpPostBodyUtil.NAME, this.myTeam.getName());
        startActivity(intent1);
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
}
