package com.wzes.huddle.activities.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.activities.login.LoginActivity;
import com.wzes.huddle.util.AppManager;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.setting_back)
    public ImageButton backBtn;
    @BindView(R.id.setting_login_out)
    public Button loginOut;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        backBtn.setOnClickListener(view -> finish());

        loginOut.setOnClickListener(view -> {
            Preferences.saveLastUserAccount(Preferences.getUserAccount());
            Preferences.saveUserAccount(null);
            AppManager.getAppManager().finishAllActivity();
            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            finish();
        });
    }
}
