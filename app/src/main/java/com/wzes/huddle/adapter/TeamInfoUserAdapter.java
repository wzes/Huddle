package com.wzes.huddle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.bean.teamuser;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;
import com.wzes.huddle.team_info.TeamInfoActivity;
import com.wzes.huddle.user_info.UserInfoActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class TeamInfoUserAdapter extends Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<teamuser> list;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        CircleImageView circleImageView;
        OnRecyclerViewOnClickListener listener;

        ViewHolder(View itemView) {
            super(itemView);
            this.circleImageView = itemView.findViewById(R.id.team_info_item_img);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (this.listener != null) {
                this.listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }

    public TeamInfoUserAdapter(Context context, List<teamuser> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.team_info_user_item, parent, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        teamuser user = list.get(position);
        if (holder instanceof ViewHolder) {
            Glide.with(context).load(user.getImage()).into(((ViewHolder) holder).circleImageView);
            ((ViewHolder) holder).circleImageView.setOnClickListener(view -> {
                Intent uIntent = new Intent(context, UserInfoActivity.class);
                uIntent.putExtra("user_id", list.get(position).getUser_id());
                context.startActivity(uIntent);
            });
        }
    }

    public int getItemCount() {
        return this.list.size();
    }
}
