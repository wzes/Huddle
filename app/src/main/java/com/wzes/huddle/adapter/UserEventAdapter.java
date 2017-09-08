package com.wzes.huddle.adapter;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wzes.huddle.C0479R;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.event_info.EventInfoActivity;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;
import com.wzes.huddle.user_info.UserEventFragment;
import java.util.List;

public class UserEventAdapter extends Adapter<ViewHolder> {
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_NORMAL = 0;
    private UserEventFragment context;
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
            this.Img = (ImageView) itemView.findViewById(C0479R.id.event_item_image);
            this.Title = (TextView) itemView.findViewById(C0479R.id.event_item_title);
            this.Sign = (TextView) itemView.findViewById(C0479R.id.event_item_signtime);
            this.Rice = (TextView) itemView.findViewById(C0479R.id.event_item_ricetime);
            this.Host = (TextView) itemView.findViewById(C0479R.id.event_item_host);
            this.View = (TextView) itemView.findViewById(C0479R.id.event_item_view);
            this.Follow = (TextView) itemView.findViewById(C0479R.id.event_item_follow);
            this.Level = (TextView) itemView.findViewById(C0479R.id.event_item_level);
            this.Divider = itemView.findViewById(C0479R.id.event_divider);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent intent = new Intent(UserEventAdapter.this.context.getContext(), EventInfoActivity.class);
            intent.putExtra("event_id", ((Event) UserEventAdapter.this.list.get(getLayoutPosition())).getEvent_id() + "");
            UserEventAdapter.this.context.startActivity(intent);
        }
    }

    public UserEventAdapter(@NonNull UserEventFragment context, @NonNull List<Event> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context.getContext());
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(this.inflater.inflate(C0479R.layout.event_item_normal, parent, false), this.listener);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            Event item = (Event) this.list.get(position);
            Glide.with(this.context).load(item.getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(((NormalViewHolder) holder).Img);
            ((NormalViewHolder) holder).Title.setText(item.getTitle());
            ((NormalViewHolder) holder).Sign.setText("报名时间 " + item.getEnrool_start_date() + " - " + item.getEnrool_end_date());
            ((NormalViewHolder) holder).Rice.setText("开始时间 " + item.getMatch_start_date() + " - " + item.getMatch_end_date());
            ((NormalViewHolder) holder).Host.setText("主办方 " + item.getOrganizer());
            ((NormalViewHolder) holder).View.setText(item.getPage_view() + " 浏览");
            ((NormalViewHolder) holder).Follow.setText(item.getFollow_account() + " 关注");
            ((NormalViewHolder) holder).Level.setText(item.getLevel() + "");
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
