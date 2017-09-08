package com.wzes.huddle.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.MainAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.chatservice.ChatService;
import com.wzes.huddle.util.AppManager;
import com.wzes.huddle.util.BottomNavigationViewHelper;
import com.wzes.huddle.util.NoScrollViewPager;

public class MainActivity extends AppCompatActivity {
    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new C09061();
    private OnPageChangeListener mOnPageChangeListener = new C09072();
    private TextView mTextMessage;
    private NoScrollViewPager mViewPager;
    private MainAdapter mainAdapter;
    private BottomNavigationView navigation;

    class C09061 implements OnNavigationItemSelectedListener {
        C09061() {
        }

        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_team /*2131624329*/:
                    MainActivity.this.mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_event /*2131624330*/:
                    MainActivity.this.mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_chat /*2131624331*/:
                    MainActivity.this.mViewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_my /*2131624332*/:
                    MainActivity.this.mViewPager.setCurrentItem(3);
                    return true;
                default:
                    return false;
            }
        }
    }

    class C09072 implements OnPageChangeListener {
        C09072() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

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

        public void onPageScrollStateChanged(int state) {
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);
        Intent chatIntent = new Intent(this, ChatService.class);
        chatIntent.putExtra("user_id", Preferences.getUserAccount());
        startService(chatIntent);
        this.mViewPager = (NoScrollViewPager) findViewById(R.id.content);
        ChatFragment cf = ChatFragment.newInstance("ChatFragment", "1");
        MyFragment mf = MyFragment.newInstance("MyFragment", "2");
        this.mainAdapter = new MainAdapter(getSupportFragmentManager(), this, EventFragment.newInstance("EventFragment", "3"), TeamFragment.newInstance("TeamFragment", "4"), cf, mf);
        this.mViewPager.setAdapter(this.mainAdapter);
        this.navigation = (BottomNavigationView) findViewById(R.id.navigation);
        this.navigation.setOnNavigationItemSelectedListener(this.mOnNavigationItemSelectedListener);
        this.mViewPager.setOnPageChangeListener(this.mOnPageChangeListener);
        BottomNavigationViewHelper.disableShiftMode(this.navigation);
    }
}
