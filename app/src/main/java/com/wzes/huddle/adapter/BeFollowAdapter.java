package com.wzes.huddle.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.Follow;

import java.util.List;

/**
 * Created by xuantang on 17-10-2.
 */

public class BeFollowAdapter extends BaseItemDraggableAdapter<Follow, BaseViewHolder> {

    public BeFollowAdapter(int layoutResId, List<Follow> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Follow item) {
        helper.setText(R.id.follow_item_name, item.getName());
        helper.setText(R.id.follow_item_content, item.getMotto());
        // 加载网络图片
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.follow_item_image));
    }
}