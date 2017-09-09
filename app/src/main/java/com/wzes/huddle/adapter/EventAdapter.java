package com.wzes.huddle.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.UIMsg.m_AppUI;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.Event;
import com.wzes.huddle.event_info.EventInfoActivity;
import com.wzes.huddle.homepage.EventFragment;
import com.wzes.huddle.imageloader.ImageViewActivity;
import com.wzes.huddle.util.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventAdapter extends Adapter<ViewHolder> {
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_NORMAL = 0;

    private List<String> hotImages;
    private EventFragment context;
    private LayoutInflater inflater;
    private List<Event> norList;
    private List<Event> hotList;
    private List<String> images;
    private List<String> titles;


    public EventAdapter(EventFragment context, List<Event> norList, List<Event> hotList) {
        this.context = context;
        this.hotList = hotList;
        this.inflater = LayoutInflater.from(context.getContext());
        this.norList = norList;
        hotImages = new ArrayList<>();
        images = new ArrayList<>();
        titles = new ArrayList<>();
        for (Event event : hotList) {
            images.add(event.getImage());
            titles.add(event.getTitle());
            hotImages.add(event.getImage());
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                return new NormalViewHolder(inflater.inflate(R.layout.event_item_normal, parent, false));
            case TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.event_item_banner, parent, false));
            default:
                return null;
        }
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            Event item = norList.get(position - 1);
            String uri = item.getImage();
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .priority(Priority.HIGH);
            Glide.with(this.context)
                    .load(item.getImage())
                    .apply(options)
                    .into(((NormalViewHolder) holder).Image);
            ((NormalViewHolder) holder).Image.setOnClickListener(view -> {
                Intent intent = new Intent(this.context.getContext(), ImageViewActivity.class);
                intent.putExtra("uri", uri);
                context.startActivity(intent);
            });
            ((NormalViewHolder) holder).Title.setText(item.getTitle());
            ((NormalViewHolder) holder).Sign.setText("报名时间 " + item.getEnrool_start_date().split(" ")[0] + " - " + item.getEnrool_end_date().split(" ")[0]);
            ((NormalViewHolder) holder).Rice.setText("开始时间 " + item.getMatch_start_date().split(" ")[0] + " - " + item.getMatch_end_date().split(" ")[0]);
            ((NormalViewHolder) holder).Host.setText("主办方 " + item.getOrganizer());
            ((NormalViewHolder) holder).View.setText(item.getPage_view() + " 浏览");
            ((NormalViewHolder) holder).Follow.setText(item.getFollow_account() + " 关注");
            ((NormalViewHolder) holder).Level.setText(item.getLevel() + "");
            ((NormalViewHolder) holder).Layout.setOnClickListener(view -> {
                Intent intent = new Intent(context.getActivity(), EventInfoActivity.class);
                intent.putExtra("event_id", norList.get(position - 1).getEvent_id() + "");
                EventAdapter.this.context.startActivity(intent);
            });
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).banner
                    .setImages(images)
                    .setBannerStyle(5)
                    .setBannerTitles(titles)
                    .setDelayTime(m_AppUI.MSG_APP_GPS)
                    .setIndicatorGravity(7)
                    .setImageLoader(new GlideImageLoader())
                    .start();
            ((HeaderViewHolder) holder).banner.setOnBannerListener(bPosition -> {
                Intent intent = new Intent(context.getActivity(), EventInfoActivity.class);
                intent.putExtra("event_id", hotList.get(bPosition).getEvent_id() + "");
                context.startActivity(intent);
            });
        }
    }

    class HeaderViewHolder extends ViewHolder {
        @BindView(R.id.event_banner) Banner banner;
        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class NormalViewHolder extends ViewHolder {

        @BindView(R.id.event_item_image) ImageView Image;
        @BindView(R.id.event_item_title) TextView Title;
        @BindView(R.id.event_item_signtime) TextView Sign;
        @BindView(R.id.event_item_ricetime) TextView Rice;
        @BindView(R.id.event_item_host) TextView Host;
        @BindView(R.id.event_item_view) TextView View;
        @BindView(R.id.event_item_follow) TextView Follow;
        @BindView(R.id.event_item_level) TextView Level;
        @BindView(R.id.event_item_layout) LinearLayout Layout;

        NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    public int getItemCount() {
        return norList.size() + 1;
    }

}
