package com.wzes.huddle.activities.setting;

import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Button;

import com.wzes.huddle.R;

import java.util.List;

public class SetActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("退出登录");
            setListFooter(button);
        }
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    public static class Prefs1Fragment extends PreferenceFragment {

    }

    public static class Prefs2Fragment extends PreferenceFragment {

    }

}
