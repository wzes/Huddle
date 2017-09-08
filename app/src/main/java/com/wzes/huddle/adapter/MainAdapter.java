package com.wzes.huddle.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.wzes.huddle.C0479R;
import com.wzes.huddle.homepage.ChatFragment;
import com.wzes.huddle.homepage.EventFragment;
import com.wzes.huddle.homepage.MyFragment;
import com.wzes.huddle.homepage.TeamFragment;

public class MainAdapter extends FragmentPagerAdapter {
    private ChatFragment chatFragment;
    private final Context context;
    private EventFragment eventFragment;
    private MyFragment myFragment;
    private TeamFragment teamFragment;
    private String[] titles;

    public MainAdapter(FragmentManager fm, Context context, EventFragment eventFragment, TeamFragment teamFragment, ChatFragment chatFragment, MyFragment myFragment) {
        super(fm);
        this.titles = new String[]{context.getResources().getString(C0479R.string.title_event), context.getResources().getString(C0479R.string.title_team), context.getResources().getString(C0479R.string.title_chat), context.getResources().getString(C0479R.string.title_my)};
        this.context = context;
        this.chatFragment = chatFragment;
        this.teamFragment = teamFragment;
        this.eventFragment = eventFragment;
        this.myFragment = myFragment;
    }

    public ChatFragment getChatFragment() {
        return this.chatFragment;
    }

    public EventFragment getEventFragment() {
        return this.eventFragment;
    }

    public MyFragment getMyFragment() {
        return this.myFragment;
    }

    public TeamFragment getTeamFragment() {
        return this.teamFragment;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return this.eventFragment;
        }
        if (position == 1) {
            return this.teamFragment;
        }
        if (position == 2) {
            return this.chatFragment;
        }
        return this.myFragment;
    }

    public int getCount() {
        return this.titles.length;
    }

    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }
}
