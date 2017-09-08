package com.wzes.huddle.user_info;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.wzes.huddle.C0479R;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity_ViewBinding<T extends UserInfoActivity> implements Unbinder {
    protected T target;

    @UiThread
    public UserInfoActivity_ViewBinding(T target, View source) {
        this.target = target;
        target.viewPager = (ViewPager) Utils.findRequiredViewAsType(source, C0479R.id.user_pager, "field 'viewPager'", ViewPager.class);
        target.backBtn = (ImageButton) Utils.findRequiredViewAsType(source, C0479R.id.user_info_back, "field 'backBtn'", ImageButton.class);
        target.moreBtn = (ImageButton) Utils.findRequiredViewAsType(source, C0479R.id.user_info_more, "field 'moreBtn'", ImageButton.class);
        target.tabLayout = (TabLayout) Utils.findRequiredViewAsType(source, C0479R.id.user_info_tabs, "field 'tabLayout'", TabLayout.class);
        target.titleTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.user_info_title, "field 'titleTxt'", TextView.class);
        target.nameTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.user_info_name, "field 'nameTxt'", TextView.class);
        target.mottoTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.user_info_motto, "field 'mottoTxt'", TextView.class);
        target.majorTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.user_info_major, "field 'majorTxt'", TextView.class);
        target.infoTxt = (TextView) Utils.findRequiredViewAsType(source, C0479R.id.user_info_info, "field 'infoTxt'", TextView.class);
        target.imageView = (CircleImageView) Utils.findRequiredViewAsType(source, C0479R.id.user_info_image, "field 'imageView'", CircleImageView.class);
        target.expendBtn = (Button) Utils.findRequiredViewAsType(source, C0479R.id.user_info_expend, "field 'expendBtn'", Button.class);
    }

    @CallSuper
    public void unbind() {
        T target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        target.viewPager = null;
        target.backBtn = null;
        target.moreBtn = null;
        target.tabLayout = null;
        target.titleTxt = null;
        target.nameTxt = null;
        target.mottoTxt = null;
        target.majorTxt = null;
        target.infoTxt = null;
        target.imageView = null;
        target.expendBtn = null;
        this.target = null;
    }
}
