package com.wzes.huddle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.activities.eventdetail.EventInfoActivity;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;
import com.wzes.huddle.activities.userdetail.UserEventFragment;
import com.wzes.huddle.util.DateUtils;

import java.util.List;

public class UserEventAdapter extends Adapter<ViewHolder> {
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_NORMAL = 0;
    private Context context;
    private LayoutInflater inflater;
    private List<Event> list;
    private OnRecyclerViewOnClickListener listener;

    public class NormalViewHolder extends ViewHolder implements OnClickListener {
        View Divider;
        TextView Follow;
        TextView Host;
        ImageView Img;
        TextView Level;
        TextView Rice;
        TextView Sign;
        TextView Title;
        TextView View;
        OnRecyclerViewOnClickListener listener;

        public NormalViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            this.Img = itemView.findViewById(R.id.event_item_image);
            this.Title = itemView.findViewById(R.id.event_item_title);
            this.Sign = itemView.findViewById(R.id.event_item_signtime);
            this.Rice = itemView.findViewById(R.id.event_item_ricetime);
            this.Host = itemView.findViewById(R.id.event_item_host);
            this.View = itemView.findViewById(R.id.event_item_view);
            this.Follow = itemView.findViewById(R.id.event_item_follow);
            this.Level = itemView.findViewById(R.id.event_item_level);
            this.Divider = itemView.findViewById(R.id.event_divider);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent intent = new Intent(context, EventInfoActivity.class);
            intent.putExtra("event_id", UserEventAdapter.this.list.get(getLayoutPosition()).getEvent_id() + "");
            UserEventAdapter.this.context.startActivity(intent);
        }
    }

    public UserEventAdapter(@NonNull Context context, @NonNull List<Event> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(inflater.inflate(R.layout.event_item_normal, parent, false), this.listener);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            Event item = list.get(position);
            if (item != null) {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .priority(Priority.HIGH);
                Glide.with(this.context)
                        .load(item.getImage())
                        .apply(options).into(((NormalViewHolder) holder).Img);
                ((NormalViewHolder) holder).Title.setText(item.getTitle());
                ((NormalViewHolder) holder).Sign.setText("报名时间 " + DateUtils.getYearTime(item.getEnrool_start_date()) +
                        " - " + DateUtils.getYearTime(item.getEnrool_end_date()));
                ((NormalViewHolder) holder).Rice.setText("开始时间 " + DateUtils.getYearTime(item.getMatch_start_date()) + " - "
                        + DateUtils.getYearTime(item.getMatch_end_date()));
                ((NormalViewHolder) holder).Host.setText("主办方 " + item.getOrganizer());
                ((NormalViewHolder) holder).View.setText(item.getPage_view() + " 浏览");
                ((NormalViewHolder) holder).Follow.setText(item.getFollow_account() + " 关注");
                ((NormalViewHolder) holder).Level.setText(item.getLevel() + "");
            }
        }
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getItemCount() {
        return this.list.size();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener) {
        this.listener = listener;
    }
}
