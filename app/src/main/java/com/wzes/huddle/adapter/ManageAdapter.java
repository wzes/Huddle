package com.wzes.huddle.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.UserTeam;

import java.util.List;

/**
 * Created by xuantang on 17-10-2.
 */

public class ManageAdapter extends BaseItemDraggableAdapter<UserTeam, BaseViewHolder> {

    public ManageAdapter(int layoutResId, List<UserTeam> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserTeam item) {
        helper.setText(R.id.manage_item_name, item.getName());
        helper.setText(R.id.manage_item_team_name, item.getTitle());
        // 加载网络图片
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.manage_item_image));
    }
}