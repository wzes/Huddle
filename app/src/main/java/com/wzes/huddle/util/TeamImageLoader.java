package com.wzes.huddle.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.ninegrid.NineGridView;
import com.wzes.huddle.R;

/**
 * Created by xuantang on 17-9-9.
 */

public class TeamImageLoader implements NineGridView.ImageLoader {

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_default_image)
                .error(R.drawable.ic_default_image)
                .priority(Priority.HIGH);
        Glide.with(context).load(url)//
               .apply(options)
                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }

}
