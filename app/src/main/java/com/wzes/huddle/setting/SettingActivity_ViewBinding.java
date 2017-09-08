package com.wzes.huddle.setting;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.wzes.huddle.C0479R;

public class SettingActivity_ViewBinding<T extends SettingActivity> implements Unbinder {
    protected T target;

    @UiThread
    public SettingActivity_ViewBinding(T target, View source) {
        this.target = target;
        target.backBtn = (ImageButton) Utils.findRequiredViewAsType(source, C0479R.id.setting_back, "field 'backBtn'", ImageButton.class);
        target.loginOut = (Button) Utils.findRequiredViewAsType(source, C0479R.id.setting_login_out, "field 'loginOut'", Button.class);
    }

    @CallSuper
    public void unbind() {
        T target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        target.backBtn = null;
        target.loginOut = null;
        this.target = null;
    }
}
