package com.wzes.huddle.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.wzes.huddle.R;
import com.wzes.huddle.team.TeamHotFragment;
import com.wzes.huddle.team.TeamNearFragment;
import com.wzes.huddle.team.TeamNewFragment;
import com.wzes.huddle.team.TeamRecFragment;

public class TeamAdapter extends FragmentPagerAdapter {
    private final Context context;
    private TeamHotFragment teamHotFragment;
    private TeamNearFragment teamNearFragment;
    private TeamNewFragment teamNewFragment;
    private TeamRecFragment teamRecFragment;
    private String[] titles;

    public TeamNearFragment getTeamNearFragment() {
        return this.teamNearFragment;
    }

    public TeamHotFragment getTeamHotFragment() {
        return this.teamHotFragment;
    }

    public TeamNewFragment getTeamNewFragment() {
        return this.teamNewFragment;
    }

    public TeamRecFragment getTeamRecFragment() {
        return this.teamRecFragment;
    }

    public TeamAdapter(FragmentManager fm, Context context, TeamNewFragment teamNewFragment, TeamRecFragment teamRecFragment, TeamHotFragment teamHotFragment, TeamNearFragment teamNearFragment) {
        super(fm);
        this.context = context;
        this.teamHotFragment = teamHotFragment;
        this.teamNearFragment = teamNearFragment;
        this.teamNewFragment = teamNewFragment;
        this.teamRecFragment = teamRecFragment;
        this.titles = new String[]{context.getResources().getString(R.string.team_new), context.getResources().getString(R.string.team_rec), context.getResources().getString(R.string.team_hot), context.getResources().getString(R.string.team_near)};
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return this.teamNewFragment;
        }
        if (position == 1) {
            return this.teamRecFragment;
        }
        if (position == 2) {
            return this.teamHotFragment;
        }
        return this.teamNearFragment;
    }

    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }
}
