package com.wzes.huddle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.Image;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xuantang on 17-9-21.
 */

public class TeamAddImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<Image> list;
    private Context context;

    public TeamAddImageAdapter(Context context, List<Image> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    private OnRecyclerViewOnClickListener mOnRecyclerViewOnClickListener;

    public void setOnItemClickLitener(OnRecyclerViewOnClickListener mOnItemClickLitener) {
        this.mOnRecyclerViewOnClickListener = mOnItemClickLitener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.team_add_item_img)
        ImageView teamAddItemImg;
        @BindView(R.id.team_add_item_cancel)
        ImageButton teamAddItemCancel;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.team_add_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Image image = list.get(position);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH);
        if(position == list.size()-1){
            Glide.with(context).load(image.getImage()).apply(options).into(((ViewHolder)holder).teamAddItemImg);
            ((ViewHolder)holder).teamAddItemCancel.setVisibility(View.GONE);
        }else{
            Glide.with(context).load(image.getImage()).apply(options).into(((ViewHolder)holder).teamAddItemImg);
            ((ViewHolder)holder).teamAddItemCancel.setVisibility(View.VISIBLE);

        }

        if (mOnRecyclerViewOnClickListener != null) {
            ((ViewHolder)holder).teamAddItemImg.setOnClickListener(view -> {
                mOnRecyclerViewOnClickListener.OnItemClick(view, position);
            });
            ((ViewHolder)holder).teamAddItemCancel.setOnClickListener(view -> {
                mOnRecyclerViewOnClickListener.OnItemClick(view, position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
