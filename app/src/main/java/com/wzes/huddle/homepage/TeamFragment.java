package com.wzes.huddle.homepage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wzes.huddle.R;
import com.wzes.huddle.adapter.TeamAdapter;
import com.wzes.huddle.team.TeamHotFragment;
import com.wzes.huddle.team.TeamNearFragment;
import com.wzes.huddle.team.TeamNewFragment;
import com.wzes.huddle.team.TeamRecFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TeamFragment extends Fragment {
    @BindView(R.id.team_tabs) TabLayout teamTabs;
    @BindView(R.id.team_pager) ViewPager teamPager;
    Unbinder unbinder;
    private TeamAdapter teamAdapter;
    private TeamHotFragment teamHotFragment;
    private TeamNearFragment teamNearFragment;
    private TeamNewFragment teamNewFragment;
    private TeamRecFragment teamRecFragment;

    private static TeamFragment teamFragment;

    private TeamFragment() {
    }

    public static TeamFragment newInstance() {
        if (teamFragment == null) {
            teamFragment = new TeamFragment();
        }
        return teamFragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        ButterKnife.bind(this, view);
        teamHotFragment = new TeamHotFragment();
        teamNewFragment = new TeamNewFragment();
        teamRecFragment = new TeamRecFragment();
        teamNearFragment = new TeamNearFragment();
        teamAdapter = new TeamAdapter(getChildFragmentManager(), getContext(),
                teamNewFragment,
                teamRecFragment,
                teamHotFragment,
                teamNearFragment);
        teamPager.setAdapter(teamAdapter);
        teamTabs.setupWithViewPager(teamPager);
        setHasOptionsMenu(true);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
