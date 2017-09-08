package com.wzes.huddle.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.wzes.huddle.C0479R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.login.LoginActivity;
import com.wzes.huddle.util.AppManager;

public class SettingActivity extends AppCompatActivity {
    @BindView(2131624112)
    public ImageButton backBtn;
    @BindView(2131624113)
    public Button loginOut;

    class C04861 implements OnClickListener {
        C04861() {
        }

        public void onClick(View v) {
            SettingActivity.this.finish();
        }
    }

    class C04872 implements OnClickListener {
        C04872() {
        }

        public void onClick(View v) {
            Preferences.saveLastUserAccount(Preferences.getUserAccount());
            Preferences.saveUserAccount(null);
            AppManager.getAppManager().finishAllActivity();
            SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class));
            SettingActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0479R.layout.activity_setting);
        ButterKnife.bind((Activity) this);
        this.backBtn.setOnClickListener(new C04861());
        this.loginOut.setOnClickListener(new C04872());
    }
}
