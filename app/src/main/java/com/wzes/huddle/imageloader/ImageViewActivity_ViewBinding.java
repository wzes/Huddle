package com.wzes.huddle.imageloader;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.wzes.huddle.C0479R;

public class ImageViewActivity_ViewBinding<T extends ImageViewActivity> implements Unbinder {
    protected T target;

    @UiThread
    public ImageViewActivity_ViewBinding(T target, View source) {
        this.target = target;
        target.backBtn = (ImageButton) Utils.findRequiredViewAsType(source, C0479R.id.image_loader_back, "field 'backBtn'", ImageButton.class);
        target.imageLoadView = (ImageLoadView) Utils.findRequiredViewAsType(source, C0479R.id.image_loader_view, "field 'imageLoadView'", ImageLoadView.class);
    }

    @CallSuper
    public void unbind() {
        T target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        target.backBtn = null;
        target.imageLoadView = null;
        this.target = null;
    }
}
