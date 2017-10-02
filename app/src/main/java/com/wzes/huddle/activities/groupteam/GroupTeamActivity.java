package com.wzes.huddle.activities.groupteam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.wzes.huddle.R;
import com.wzes.huddle.activities.signteam.SignTeamActivity;
import com.wzes.huddle.adapter.TeamInfoAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Team;
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

public class GroupTeamActivity extends AppCompatActivity {

    @BindView(R.id.group_team_back)
    ImageButton groupTeamBack;
    @BindView(R.id.group_team_recycler)
    RecyclerView groupTeamRecycler;

    private List<Team> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_team);
        ButterKnife.bind(this);
        initData();
    }

    @OnClick(R.id.group_team_back)
    public void onViewClicked() {
        finish();
    }

    public void initData() {
        MyRetrofit.getGsonRetrofit().getUserSignTeamList(Preferences.getUserAccount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Team>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Team> teams) {
                        list = teams;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        groupTeamRecycler.setAdapter(new TeamInfoAdapter(GroupTeamActivity.this, list));
                        groupTeamRecycler.setHasFixedSize(true);
                        groupTeamRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                });
    }

}
