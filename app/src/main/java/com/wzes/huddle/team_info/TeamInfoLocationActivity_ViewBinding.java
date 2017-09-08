package com.wzes.huddle.team_info;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.wzes.huddle.C0479R;

public class TeamInfoLocationActivity_ViewBinding<T extends TeamInfoLocationActivity> implements Unbinder {
    protected T target;

    @UiThread
    public TeamInfoLocationActivity_ViewBinding(T target, View source) {
        this.target = target;
        target.titleTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_location_title, "field 'titleTxt'", TextView.class);
        target.backBtn = (ImageButton) Utils.findRequiredViewAsType(source, C0479R.id.team_info_location_back, "field 'backBtn'", ImageButton.class);
    }

    @CallSuper
    public void unbind() {
        T target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        target.titleTxt = null;
        target.backBtn = null;
        this.target = null;
    }
}
