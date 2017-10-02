package com.wzes.huddle.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.MainAdapter;
import com.wzes.huddle.activities.add.AddEventActivity;
import com.wzes.huddle.activities.add.AddTeamActivity;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.chatservice.ChatService;
import com.wzes.huddle.util.AppManager;
import com.wzes.huddle.util.BottomNavigationViewHelper;
import com.wzes.huddle.util.MyLog;
import com.wzes.huddle.util.NoScrollViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content)
    NoScrollViewPager mViewPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.main_add_team)
    FloatingActionButton addTeamBtn;
    @BindView(R.id.main_add_event)
    FloatingActionButton addEventBtn;
    @BindView(R.id.main_add)
    FloatingActionsMenu mainAdd;
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


        addEventBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddEventActivity.class));
            mainAdd.collapse();
            mainAdd.setActivated(false);
        });

        addTeamBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddTeamActivity.class));
            mainAdd.setActivated(false);
            mainAdd.collapse();
        });

        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_event:
                    mViewPager.setCurrentItem(0);
                    mainAdd.setVisibility(View.VISIBLE);
                    mainAdd.collapse();
                    return true;
                case R.id.navigation_team:
                    mViewPager.setCurrentItem(1);
                    mainAdd.setVisibility(View.VISIBLE);
                    mainAdd.collapse();
                    return true;
                case R.id.navigation_chat:
                    mViewPager.setCurrentItem(2);
                    mainAdd.setVisibility(View.GONE);
                    mainAdd.collapse();
                    return true;
                case R.id.navigation_my:
                    mViewPager.setCurrentItem(3);
                    mainAdd.setVisibility(View.GONE);
                    mainAdd.collapse();
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
                        navigation.setSelectedItemId(R.id.navigation_event);
                        mainAdd.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_team);
                        mainAdd.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_chat);
                        mainAdd.setVisibility(View.GONE);
                        break;
                    case 3:
                        navigation.setSelectedItemId(R.id.navigation_my);
                        mainAdd.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        BottomNavigationViewHelper.disableShiftMode(this.navigation);
    }

    private boolean mIsExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                finish();

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(() -> mIsExit = false, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
