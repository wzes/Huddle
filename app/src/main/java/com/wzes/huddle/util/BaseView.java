package com.wzes.huddle.util;

import android.view.View;

public interface BaseView<T> {
    void initViews(View view);

    void setPresenter(T t);
}
