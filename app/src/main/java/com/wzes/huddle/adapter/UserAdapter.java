package com.wzes.huddle.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.wzes.huddle.user_info.UserEventFragment;
import com.wzes.huddle.user_info.UserTeamFragment;

public class UserAdapter extends FragmentPagerAdapter {
    private final Context context;
    private String[] titles = new String[]{"活动", "组队"};
    private UserEventFragment userEventFragment;
    private UserTeamFragment userTeamFragment;

    public UserAdapter(FragmentManager fm, Context context, UserEventFragment userEventFragment, UserTeamFragment userTeamFragment) {
        super(fm);
        this.context = context;
        this.userEventFragment = userEventFragment;
        this.userTeamFragment = userTeamFragment;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return this.userEventFragment;
        }
        return this.userTeamFragment;
    }

    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }
}
