package com.wzes.huddle.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wzes.huddle.activities.manage.GroupFragment;
import com.wzes.huddle.activities.manage.SignFragment;

public class ManageFragmentAdapter extends FragmentPagerAdapter {
    private final Context context;
    private String[] titles = new String[]{"活动", "组队"};
    private GroupFragment groupFragment;
    private SignFragment signFragment;

    public ManageFragmentAdapter(FragmentManager fm, Context context, GroupFragment groupFragment, SignFragment signFragment) {
        super(fm);
        this.context = context;
        this.groupFragment = groupFragment;
        this.signFragment = signFragment;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return groupFragment;
        }
        return signFragment;
    }

    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }
}
