package com.wzes.huddle.event_info;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.wzes.huddle.C0479R;

public class EventInfoActivity_ViewBinding<T extends EventInfoActivity> implements Unbinder {
    protected T target;

    @UiThread
    public EventInfoActivity_ViewBinding(T target, View source) {
        this.target = target;
        target.toolBar = (Toolbar) Utils.findRequiredViewAsType(source, C0479R.id.event_read_toolbar, "field 'toolBar'", Toolbar.class);
        target.createTeam = (FloatingActionButton) Utils.findRequiredViewAsType(source, C0479R.id.event_read_create, "field 'createTeam'", FloatingActionButton.class);
        target.collapsing = (CollapsingToolbarLayout) Utils.findRequiredViewAsType(source, C0479R.id.event_read_collapsing, "field 'collapsing'", CollapsingToolbarLayout.class);
        target.signTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.event_read_signTime, "field 'signTxt'", TextView.class);
        target.riceTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.event_read_riceTime, "field 'riceTxt'", TextView.class);
        target.contentTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.event_read_content, "field 'contentTxt'", TextView.class);
        target.imageView = (ImageView) Utils.findRequiredViewAsType(source, C0479R.id.event_read_image, "field 'imageView'", ImageView.class);
    }

    @CallSuper
    public void unbind() {
        T target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        target.toolBar = null;
        target.createTeam = null;
        target.collapsing = null;
        target.signTxt = null;
        target.riceTxt = null;
        target.contentTxt = null;
        target.imageView = null;
        this.target = null;
    }
}
