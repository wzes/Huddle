package com.wzes.huddle.activities.teamdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.UIMsg.m_AppUI;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.TeamInfoUserAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Image;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.bean.teamuser;
import com.wzes.huddle.chatservice.ChatActivity;
import com.wzes.huddle.imageloader.ImageViewActivity;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.activities.userdetail.UserInfoActivity;
import com.wzes.huddle.util.AppManager;
import com.wzes.huddle.util.DateUtils;
import com.wzes.huddle.util.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TeamInfoActivity extends AppCompatActivity {
    @BindView(R.id.team_info_banner) Banner banner;
    @BindView(R.id.team_info_category) TextView categoryTxt;
    @BindView(R.id.team_info_user_img) CircleImageView circleImageView;
    @BindView(R.id.team_info_collapsing) CollapsingToolbarLayout collapsing;
    @BindView(R.id.team_info_content) TextView contentTxt;
    @BindView(R.id.team_info_follow) Button followBtn;
    @BindView(R.id.team_info_user_follow) TextView followTxt;
    @BindView(R.id.team_info_user_info) TextView infoTxt;
    @BindView(R.id.team_info_locationname) TextView locationTxt;
    @BindView(R.id.team_info_user_major) TextView majorTxt;
    @BindView(R.id.team_info_title) TextView teamInfoTitle;
    @BindView(R.id.team_info_location_detail) TextView teamInfoLocationDetail;
    @BindView(R.id.team_info_user_name) TextView nameTxt;
    @BindView(R.id.team_info_people) TextView peopleTxt;
    @BindView(R.id.team_info_user_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.team_info_user_save) LinearLayout saveBtn;
    @BindView(R.id.team_info_user_sign) LinearLayout signBtn;
    @BindView(R.id.team_info_user_talk) LinearLayout talkBtn;
    @BindView(R.id.team_info_start_time) TextView timeTxt;
    @BindView(R.id.team_info_toolbar) Toolbar toolBar;
    @BindView(R.id.team_info_user_layout) LinearLayout userBtn;


    private Team myTeam;
    private String team_id;
    private List<teamuser> list;
    private TeamInfoUserAdapter teamInfoUserAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        AppManager.getAppManager().addActivity(this);
        team_id = getIntent().getStringExtra("team_id");
        if (team_id == null) {
            team_id = "1";
        }
        ButterKnife.bind(this);
        MyRetrofit.getGsonRetrofit().getTeam(team_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Team>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Team team) {
                        myTeam = team;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
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

                        timeTxt.setText("——" + DateUtils.getYearTime(myTeam.getStart_date()));
                        userBtn.setOnClickListener(v -> {
                            Intent nIntent = new Intent(TeamInfoActivity.this, UserInfoActivity.class);
                            nIntent.putExtra("user_id", myTeam.getUser_id());
                            startActivity(nIntent);
                        });
                        locationTxt.setText("——" + myTeam.getLocationname());
                        teamInfoLocationDetail.setOnClickListener(v -> {
                            Intent nIntent = new Intent(TeamInfoActivity.this, TeamInfoLocationActivity.class);
                            nIntent.putExtra("title", myTeam.getLocationname());
                            nIntent.putExtra("longitude", myTeam.getLocationlongitude());
                            nIntent.putExtra("latitude", myTeam.getLocationlatitude());
                            startActivity(nIntent);
                        });

                        if(myTeam.getTeamusers().get(0) == null){
                            list = new ArrayList<>();
                        }else{
                            list = myTeam.getTeamusers();
                        }
                        peopleTxt.setText(String.format("组员%d人已报名(还差%d人)", list.size(), (myTeam.getJoin_acount() - list.size() - 1)));
//                        try{
//                            list.get(0).getImage();
//                        }catch (Exception e){
//                            list = new ArrayList<>();
//                        }
                        teamInfoUserAdapter = new TeamInfoUserAdapter(TeamInfoActivity.this, list);
                        recyclerView.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TeamInfoActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(teamInfoUserAdapter);

                    }
                });
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        signBtn.setOnClickListener(view -> Toast.makeText(this, "报名成功", 0).show());
        talkBtn.setOnClickListener(view -> {
            if (myTeam.getUser_id().equals(Preferences.getUserAccount())) {
                Toast.makeText(this, "不能和自己聊天哦！", 0).show();
                return;
            }
            Intent intent1 = new Intent(this, ChatActivity.class);
            intent1.putExtra("to_id", myTeam.getUser_id());
            intent1.putExtra("to_name", myTeam.getName());
            startActivity(intent1);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
