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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wzes.huddle.C0479R;
import com.wzes.huddle.bean.Image;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;
import com.wzes.huddle.team_info.TeamInfoActivity;
import com.wzes.huddle.user_info.UserInfoActivity;
import java.util.List;

public class TeamInfoAdapter extends Adapter<ViewHolder> {
    private Fragment context;
    private LayoutInflater inflater;
    private List<Team> list;
    private OnRecyclerViewOnClickListener listener;

    public class MyViewHolder extends ViewHolder implements OnClickListener {
        TextView Category;
        TextView Content;
        View Divider;
        ImageView Img;
        ImageView Imgone;
        ImageView Imgthree;
        ImageView Imgtwo;
        Button Location;
        TextView Name;
        TextView Status;
        TextView Title;
        OnRecyclerViewOnClickListener listener;

        public MyViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            this.Img = (ImageView) itemView.findViewById(C0479R.id.team_item_img_image);
            this.Title = (TextView) itemView.findViewById(C0479R.id.team_item_img_title);
            this.Content = (TextView) itemView.findViewById(C0479R.id.team_item_img_content);
            this.Name = (TextView) itemView.findViewById(C0479R.id.team_item_img_name);
            this.Status = (TextView) itemView.findViewById(C0479R.id.team_item_img_status);
            this.Location = (Button) itemView.findViewById(C0479R.id.team_item_img_location);
            this.Imgone = (ImageView) itemView.findViewById(C0479R.id.team_item_img1);
            this.Imgtwo = (ImageView) itemView.findViewById(C0479R.id.team_item_img2);
            this.Imgthree = (ImageView) itemView.findViewById(C0479R.id.team_item_img3);
            this.listener = listener;
            this.Location.setOnClickListener(this);
            this.Img.setOnClickListener(this);
            this.Name.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            switch (view.getId()) {
                case C0479R.id.team_item_image /*2131624289*/:
                    Intent uIntent = new Intent(TeamInfoAdapter.this.context.getContext(), UserInfoActivity.class);
                    uIntent.putExtra("user_id", ((Team) TeamInfoAdapter.this.list.get(getLayoutPosition())).getUser_id());
                    TeamInfoAdapter.this.context.startActivity(uIntent);
                    return;
                case C0479R.id.team_item_img_name /*2131624300*/:
                    Intent nIntent = new Intent(TeamInfoAdapter.this.context.getContext(), UserInfoActivity.class);
                    nIntent.putExtra("user_id", ((Team) TeamInfoAdapter.this.list.get(getLayoutPosition())).getUser_id());
                    TeamInfoAdapter.this.context.startActivity(nIntent);
                    return;
                case C0479R.id.team_item_img_location /*2131624301*/:
                    Intent mIntent = new Intent(TeamInfoAdapter.this.context.getContext(), TeamInfoActivity.class);
                    mIntent.putExtra("team_id", ((Team) TeamInfoAdapter.this.list.get(getLayoutPosition())).getTeam_id());
                    TeamInfoAdapter.this.context.startActivity(mIntent);
                    return;
                default:
                    Intent intent = new Intent(TeamInfoAdapter.this.context.getContext(), TeamInfoActivity.class);
                    intent.putExtra("team_id", ((Team) TeamInfoAdapter.this.list.get(getLayoutPosition())).getTeam_id() + "");
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
        return new MyViewHolder(this.inflater.inflate(C0479R.layout.team_item_img, parent, false), this.listener);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        CharSequence charSequence;
        Team item = (Team) this.list.get(position);
        ((MyViewHolder) holder).Title.setText(item.getTitle() + "[" + item.getCategory() + "]");
        ((MyViewHolder) holder).Name.setText(item.getName());
        ((MyViewHolder) holder).Status.setText(item.getStatus());
        TextView textView = ((MyViewHolder) holder).Content;
        if (item.getContent().trim().length() > 40) {
            charSequence = item.getContent().substring(0, 40) + "...";
        } else {
            charSequence = item.getContent().trim();
        }
        textView.setText(charSequence);
        Glide.with(this.context).load(item.getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((MyViewHolder) holder).Img);
        if (item.getImages() == null) {
            return;
        }
        if (item.getImages().size() == 1) {
            Glide.with(this.context).load(((Image) item.getImages().get(0)).getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((MyViewHolder) holder).Imgone);
            ((MyViewHolder) holder).Imgtwo.setVisibility(8);
            ((MyViewHolder) holder).Imgthree.setVisibility(8);
        } else if (item.getImages().size() == 2) {
            Glide.with(this.context).load(((Image) item.getImages().get(0)).getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((MyViewHolder) holder).Imgone);
            Glide.with(this.context).load(((Image) item.getImages().get(1)).getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((MyViewHolder) holder).Imgtwo);
            ((MyViewHolder) holder).Imgthree.setVisibility(8);
        } else {
            Glide.with(this.context).load(((Image) item.getImages().get(0)).getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((MyViewHolder) holder).Imgone);
            Glide.with(this.context).load(((Image) item.getImages().get(1)).getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((MyViewHolder) holder).Imgtwo);
            Glide.with(this.context).load(((Image) item.getImages().get(2)).getImage()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((MyViewHolder) holder).Imgthree);
        }
    }

    public int getItemCount() {
        return this.list.size();
    }
}
