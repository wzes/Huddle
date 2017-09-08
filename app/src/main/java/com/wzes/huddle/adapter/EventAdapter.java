package com.wzes.huddle.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.baidu.mapapi.UIMsg.m_AppUI;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.event_info.EventInfoActivity;
import com.wzes.huddle.homepage.EventFragment;
import com.wzes.huddle.imageloader.ImageViewActivity;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;
import com.wzes.huddle.util.GlideImageLoader;
import com.youth.banner.Banner;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends Adapter<ViewHolder> {
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_NORMAL = 0;
    private static List<String> hotImages;
    private EventFragment context;
    private List<Event> hotList;
    private List<String> images = new ArrayList();
    private LayoutInflater inflater;
    private List<Event> list;
    private OnRecyclerViewOnClickListener listener;
    private List<String> titles = new ArrayList();

    class HeaderViewHolder extends ViewHolder implements OnClickListener {
        Banner banner;

        HeaderViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            this.banner = (Banner) itemView.findViewById(R.id.event_item_banner);
            itemView.setOnClickListener(this);
            //this.banner.setOnBannerListener();
        }

        private /* synthetic */ void lambda$new$0(int position) {
            Intent intent = new Intent(EventAdapter.this.context.getContext(), EventInfoActivity.class);
            intent.putExtra("event_id", ((Event) EventAdapter.this.list.get(position)).getEvent_id() + "");
            EventAdapter.this.context.startActivity(intent);
        }

        public void onClick(View view) {
            if (EventAdapter.this.listener != null) {
                EventAdapter.this.listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }

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
            this.Img = (ImageView) itemView.findViewById(R.id.event_item_image);
            this.Title = (TextView) itemView.findViewById(R.id.event_item_title);
            this.Sign = (TextView) itemView.findViewById(R.id.event_item_signtime);
            this.Rice = (TextView) itemView.findViewById(R.id.event_item_ricetime);
            this.Host = (TextView) itemView.findViewById(R.id.event_item_host);
            this.View = (TextView) itemView.findViewById(R.id.event_item_view);
            this.Follow = (TextView) itemView.findViewById(R.id.event_item_follow);
            this.Level = (TextView) itemView.findViewById(R.id.event_item_level);
            this.Divider = itemView.findViewById(R.id.event_divider);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent intent = new Intent(EventAdapter.this.context.getContext(), EventInfoActivity.class);
            intent.putExtra("event_id", ((Event) EventAdapter.this.list.get(getLayoutPosition() - 1)).getEvent_id() + "");
            EventAdapter.this.context.startActivity(intent);
        }
    }

    public EventAdapter(EventFragment context, List<Event> list,List<Event> HotEvents) {
        this.context = context;
        this.hotList = HotEvents;
        this.inflater = LayoutInflater.from(context.getContext());
        this.list = list;
        hotImages = new ArrayList();
        for (Event event : this.hotList) {
            this.images.add(event.getImage());
            this.titles.add(event.getTitle());
            hotImages.add(event.getImage());
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new NormalViewHolder(this.inflater.inflate(R.layout.event_item_normal, parent, false), this.listener);
            case 2:
                return new HeaderViewHolder(this.inflater.inflate(R.layout.event_item_banner, parent, false), this.listener);
            default:
                return null;
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            Event item = list.get(position - 1);
            String uri = item.getImage();
            Glide.with(this.context)
                    .load(item.getImage())
                    .into(((NormalViewHolder) holder).Img);
            ((NormalViewHolder) holder).Img.setOnClickListener(v -> {
                Intent intent = new Intent(this.context.getContext(), ImageViewActivity.class);
                intent.putExtra("uri", uri);
                this.context.startActivity(intent);
            });
            ((NormalViewHolder) holder).Title.setText(item.getTitle());
            ((NormalViewHolder) holder).Sign.setText("报名时间 " + item.getEnrool_start_date().split(" ")[0] + " - " + item.getEnrool_end_date().split(" ")[0]);
            ((NormalViewHolder) holder).Rice.setText("开始时间 " + item.getMatch_start_date().split(" ")[0] + " - " + item.getMatch_end_date().split(" ")[0]);
            ((NormalViewHolder) holder).Host.setText("主办方 " + item.getOrganizer());
            ((NormalViewHolder) holder).View.setText(item.getPage_view() + " 浏览");
            ((NormalViewHolder) holder).Follow.setText(item.getFollow_account() + " 关注");
            ((NormalViewHolder) holder).Level.setText(item.getLevel() + "");
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).banner.setImages(this.images).setBannerStyle(5).setBannerTitles(this.titles).setDelayTime(m_AppUI.MSG_APP_GPS).setIndicatorGravity(7).setImageLoader(new GlideImageLoader()).start();
        }
    }

    private /* synthetic */ void lambda$onBindViewHolder$0(String uri, View v) {
        Intent intent = new Intent(this.context.getContext(), ImageViewActivity.class);
        intent.putExtra("uri", uri);
        this.context.startActivity(intent);
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 2;
        }
        return 0;
    }

    public int getItemCount() {
        return this.list.size() + 1;
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener) {
        this.listener = listener;
    }
}
