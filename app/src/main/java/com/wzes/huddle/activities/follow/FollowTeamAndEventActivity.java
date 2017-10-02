package com.wzes.huddle.activities.follow;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.wzes.huddle.R;
import com.wzes.huddle.activities.userdetail.UserEventFragment;
import com.wzes.huddle.activities.userdetail.UserTeamFragment;
import com.wzes.huddle.adapter.UserAdapter;
import com.wzes.huddle.adapter.UserFollowAdapter;
import com.wzes.huddle.app.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FollowTeamAndEventActivity extends AppCompatActivity {

    @BindView(R.id.follow_team_and_event_back)
    ImageButton followTeamAndEventBack;
    @BindView(R.id.follow_team_and_event_tabs)
    TabLayout followTeamAndEventTabs;
    @BindView(R.id.follow_team_and_event_viewpager)
    ViewPager followTeamAndEventViewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_team_and_event);
        ButterKnife.bind(this);

        String user_id = Preferences.getUserAccount();
        followTeamAndEventViewpager.setAdapter(new UserFollowAdapter(getSupportFragmentManager(), this,
                FollowEventFragment.newInstance("FollowEventFragment", user_id),
                FollowTeamFragment.newInstance("FollowTeamFragment", user_id)));
        followTeamAndEventTabs.setupWithViewPager(followTeamAndEventViewpager);
    }

    @OnClick(R.id.follow_team_and_event_back)
    public void onViewClicked() {
        finish();
    }
}
