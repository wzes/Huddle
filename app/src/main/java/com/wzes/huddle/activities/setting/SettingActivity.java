package com.wzes.huddle.activities.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wzes.huddle.R;
import com.wzes.huddle.activities.login.LoginActivity;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.util.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.setting_back)
    public ImageButton backBtn;
    @BindView(R.id.setting_about_layout)
    LinearLayout settingAboutLayout;
    @BindView(R.id.setting_version_layout)
    LinearLayout settingVersionLayout;
    @BindView(R.id.setting_clear_layout)
    LinearLayout settingClearLayout;
    @BindView(R.id.setting_login_out_layout)
    LinearLayout settingLoginOutLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        backBtn.setOnClickListener(view -> finish());

    }

    @OnClick({R.id.setting_about_layout, R.id.setting_version_layout, R.id.setting_clear_layout, R.id.setting_login_out_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_about_layout:
                Toast.makeText(this, "Android 挑战赛", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_version_layout:
                Toast.makeText(this, "已经是最新版", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_clear_layout:
                Toast.makeText(this, "清除缓存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_login_out_layout:
                Preferences.saveLastUserAccount(Preferences.getUserAccount());
                Preferences.saveUserAccount(null);
                AppManager.getAppManager().finishAllActivity();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}
