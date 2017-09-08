package com.wzes.huddle.team_info;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.wzes.huddle.C0479R;
import com.youth.banner.Banner;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeamInfoActivity_ViewBinding<T extends TeamInfoActivity> implements Unbinder {
    protected T target;

    @UiThread
    public TeamInfoActivity_ViewBinding(T target, View source) {
        this.target = target;
        target.toolBar = (Toolbar) Utils.findRequiredViewAsType(source, C0479R.id.team_info_toolbar, "field 'toolBar'", Toolbar.class);
        target.collapsing = (CollapsingToolbarLayout) Utils.findRequiredViewAsType(source, C0479R.id.team_info_collapsing, "field 'collapsing'", CollapsingToolbarLayout.class);
        target.banner = (Banner) Utils.findRequiredViewAsType(source, C0479R.id.team_info_banner, "field 'banner'", Banner.class);
        target.recyclerView = (RecyclerView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_recyclerView, "field 'recyclerView'", RecyclerView.class);
        target.categoryTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_category, "field 'categoryTxt'", TextView.class);
        target.timeTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_start_time, "field 'timeTxt'", TextView.class);
        target.contentTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_content, "field 'contentTxt'", TextView.class);
        target.circleImageView = (CircleImageView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_img, "field 'circleImageView'", CircleImageView.class);
        target.nameTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_name, "field 'nameTxt'", TextView.class);
        target.majorTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_major, "field 'majorTxt'", TextView.class);
        target.peopleTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_people, "field 'peopleTxt'", TextView.class);
        target.followBtn = (Button) Utils.findRequiredViewAsType(source, C0479R.id.team_info_follow, "field 'followBtn'", Button.class);
        target.followTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_follow, "field 'followTxt'", TextView.class);
        target.infoTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_info, "field 'infoTxt'", TextView.class);
        target.locationTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_locationname, "field 'locationTxt'", TextView.class);
        target.locationBtn = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.team_info_location_detail, "field 'locationBtn'", TextView.class);
        target.talkBtn = (LinearLayout) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_talk, "field 'talkBtn'", LinearLayout.class);
        target.saveBtn = (LinearLayout) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_save, "field 'saveBtn'", LinearLayout.class);
        target.signBtn = (Button) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_sign, "field 'signBtn'", Button.class);
        target.userBtn = (LinearLayout) Utils.findRequiredViewAsType(source, C0479R.id.team_info_user_layout, "field 'userBtn'", LinearLayout.class);
    }

    @CallSuper
    public void unbind() {
        T target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        target.toolBar = null;
        target.collapsing = null;
        target.banner = null;
        target.recyclerView = null;
        target.categoryTxt = null;
        target.timeTxt = null;
        target.contentTxt = null;
        target.circleImageView = null;
        target.nameTxt = null;
        target.majorTxt = null;
        target.peopleTxt = null;
        target.followBtn = null;
        target.followTxt = null;
        target.infoTxt = null;
        target.locationTxt = null;
        target.locationBtn = null;
        target.talkBtn = null;
        target.saveBtn = null;
        target.signBtn = null;
        target.userBtn = null;
        this.target = null;
    }
}
