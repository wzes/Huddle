package com.wzes.huddle.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.MainAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.chatservice.ChatService;
import com.wzes.huddle.util.AppManager;
import com.wzes.huddle.util.BottomNavigationViewHelper;
import com.wzes.huddle.util.NoScrollViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content) NoScrollViewPager mViewPager;
    @BindView(R.id.navigation) BottomNavigationView navigation;
    private MainAdapter mainAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);

        Intent chatIntent = new Intent(this, ChatService.class);
        chatIntent.putExtra("user_id", Preferences.getUserAccount());
        startService(chatIntent);

        mainAdapter = new MainAdapter(getSupportFragmentManager(), this,
                EventFragment.newInstance(),
                TeamFragment.newInstance(),
                ChatFragment.newInstance(),
                MyFragment.newInstance());
        mViewPager.setAdapter(mainAdapter);

        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_team:
                    MainActivity.this.mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_event:
                    MainActivity.this.mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_chat:
                    MainActivity.this.mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_my:
                    MainActivity.this.mViewPager.setCurrentItem(3);
                    return true;
                default:
                    return false;
            }
        });


        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        MainActivity.this.navigation.setSelectedItemId(R.id.navigation_event);
                        return;
                    case 1:
                        MainActivity.this.navigation.setSelectedItemId(R.id.navigation_team);
                        return;
                    case 2:
                        MainActivity.this.navigation.setSelectedItemId(R.id.navigation_chat);
                        return;
                    case 3:
                        MainActivity.this.navigation.setSelectedItemId(R.id.navigation_my);
                        return;
                    default:
                        return;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        BottomNavigationViewHelper.disableShiftMode(this.navigation);
    }
}
