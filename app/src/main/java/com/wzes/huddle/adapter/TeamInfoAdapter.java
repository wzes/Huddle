package com.wzes.huddle.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.Image;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;
import com.wzes.huddle.team_info.TeamInfoActivity;
import com.wzes.huddle.user_info.UserInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeamInfoAdapter extends Adapter<ViewHolder> {

    private Fragment context;
    private LayoutInflater inflater;
    private List<Team> list;
    private OnRecyclerViewOnClickListener listener;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        @BindView(R.id.team_item_img_title) TextView teamItemImgTitle;
        @BindView(R.id.team_item_img_status) TextView teamItemImgStatus;
        @BindView(R.id.team_item_images) BGANinePhotoLayout teamItemImages;
        @BindView(R.id.team_item_img_content) TextView teamItemImgContent;
        @BindView(R.id.team_item_img_image) CircleImageView teamItemImgImage;
        @BindView(R.id.team_item_img_name) TextView teamItemImgName;
        @BindView(R.id.team_item_img_location) Button teamItemImgLocation;

        OnRecyclerViewOnClickListener listener;

        public MyViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            teamItemImgLocation.setOnClickListener(this);
            teamItemImgImage.setOnClickListener(this);
            teamItemImgName.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.team_item_img_image:
                    Intent uIntent = new Intent(TeamInfoAdapter.this.context.getContext(), UserInfoActivity.class);
                    uIntent.putExtra("user_id", TeamInfoAdapter.this.list.get(getLayoutPosition()).getUser_id());
                    TeamInfoAdapter.this.context.startActivity(uIntent);
                    return;
                case R.id.team_item_img_name:
                    Intent nIntent = new Intent(TeamInfoAdapter.this.context.getContext(), UserInfoActivity.class);
                    nIntent.putExtra("user_id", TeamInfoAdapter.this.list.get(getLayoutPosition()).getUser_id());
                    TeamInfoAdapter.this.context.startActivity(nIntent);
                    return;
                case R.id.team_item_img_location:
                    Intent mIntent = new Intent(TeamInfoAdapter.this.context.getContext(), TeamInfoActivity.class);
                    mIntent.putExtra("team_id", TeamInfoAdapter.this.list.get(getLayoutPosition()).getTeam_id());
                    TeamInfoAdapter.this.context.startActivity(mIntent);
                    return;
                default:
                    Intent intent = new Intent(TeamInfoAdapter.this.context.getContext(), TeamInfoActivity.class);
                    intent.putExtra("team_id", TeamInfoAdapter.this.list.get(getLayoutPosition()).getTeam_id() + "");
                    TeamInfoAdapter.this.context.startActivity(intent);
                    return;
            }
        }
    }

    public TeamInfoAdapter(@NonNull Fragment context, @NonNull List<Team> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context.getContext());
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(this.inflater.inflate(R.layout.team_item, parent, false), this.listener);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        CharSequence charSequence;
        Team item = list.get(position);
        ((MyViewHolder) holder).teamItemImgTitle.setText(item.getTitle() + "[" + item.getCategory() + "]");
        ((MyViewHolder) holder).teamItemImgName.setText(item.getName());
        ((MyViewHolder) holder).teamItemImgStatus.setText(item.getStatus());
        TextView textView = ((MyViewHolder) holder).teamItemImgContent;
        if (item.getContent().trim().length() > 40) {
            charSequence = item.getContent().substring(0, 40) + "...";
        } else {
            charSequence = item.getContent().trim();
        }
        textView.setText(charSequence);
        Glide.with(this.context).load(item.getImage()).into(((MyViewHolder) holder).teamItemImgImage);

        ArrayList<String> images = new ArrayList<>();
        for (Image image : list.get(position).getImages()){
            images.add(image.getImage());
        }
        ((MyViewHolder) holder).teamItemImages.setData(images);

    }

    public int getItemCount() {
        return this.list.size();
    }
}
