package com.wzes.huddle.activities.manage;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.wzes.huddle.R;
import com.wzes.huddle.adapter.ManageFragmentAdapter;
import com.wzes.huddle.app.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageActivity extends AppCompatActivity {

    @BindView(R.id.manage_back)
    ImageButton manageBack;
    @BindView(R.id.manage_tabs)
    TabLayout manageTabs;
    @BindView(R.id.manage_appbar)
    AppBarLayout manageAppbar;
    @BindView(R.id.manage_viewpager)
    ViewPager manageViewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        ButterKnife.bind(this);

        String user_id = Preferences.getUserAccount();
        manageViewpager.setAdapter(new ManageFragmentAdapter(getSupportFragmentManager(), this,
                GroupFragment.newInstance("GroupFragment", user_id),
                SignFragment.newInstance("SignFragment", user_id)));
        manageTabs.setupWithViewPager(manageViewpager);
    }

    @OnClick(R.id.manage_back)
    public void onViewClicked() {
        finish();
    }
}
