package com.wzes.huddle.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = false;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    public boolean onTouchEvent(MotionEvent arg0) {
        arg0.getAction();
        return this.noScroll && super.onTouchEvent(arg0);
    }

    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return this.noScroll && super.onInterceptTouchEvent(arg0);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }
}
