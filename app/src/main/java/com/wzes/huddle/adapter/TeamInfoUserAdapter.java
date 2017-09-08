package com.wzes.huddle.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;
import com.wzes.huddle.team_info.TeamInfoActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class TeamInfoUserAdapter extends Adapter<android.support.v7.widget.RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<User> list;
    private OnRecyclerViewOnClickListener listener;
    private TeamInfoActivity teamInfoActivity;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        CircleImageView circleImageView;
        OnRecyclerViewOnClickListener listener;

        public ViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            this.circleImageView = (CircleImageView) itemView.findViewById(R.id.team_info_item_img);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (this.listener != null) {
                this.listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }

    public TeamInfoUserAdapter(TeamInfoActivity teamInfoActivity, List<User> list) {
        this.teamInfoActivity = teamInfoActivity;
        this.inflater = LayoutInflater.from(teamInfoActivity);
        this.list = list;
    }

    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.inflater.inflate(R.layout.team_info_user_item, parent, false), this.listener);
    }

    public void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder, int position) {
        User user = (User) this.list.get(position);
        if (holder instanceof ViewHolder) {
            Glide.with(this.teamInfoActivity).load(user.getImage()).into(((ViewHolder) holder).circleImageView);
        }
    }

    public int getItemCount() {
        return this.list.size();
    }
}
