package com.wzes.huddle.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wzes.huddle.activities.follow.FollowEventFragment;
import com.wzes.huddle.activities.follow.FollowTeamFragment;
import com.wzes.huddle.activities.userdetail.UserEventFragment;
import com.wzes.huddle.activities.userdetail.UserTeamFragment;

public class UserFollowAdapter extends FragmentPagerAdapter {
    private final Context context;
    private String[] titles = new String[]{"活动", "组队"};
    private FollowEventFragment followEventFragment;
    private FollowTeamFragment followTeamFragment;

    public UserFollowAdapter(FragmentManager fm, Context context, FollowEventFragment followEventFragment, FollowTeamFragment followTeamFragment) {
        super(fm);
        this.context = context;
        this.followEventFragment = followEventFragment;
        this.followTeamFragment = followTeamFragment;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return followEventFragment;
        }
        return followTeamFragment;
    }

    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }
}
