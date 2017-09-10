package com.wzes.huddle.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.wzes.huddle.R;
import com.wzes.huddle.bean.ChatList;
import com.wzes.huddle.chatservice.ChatActivity;
import com.wzes.huddle.homepage.ChatFragment;
import com.wzes.huddle.util.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends Adapter<ViewHolder> {
    private ChatFragment context;
    private LayoutInflater inflater;
    private List<ChatList> list;

    public class ChatViewHolder extends ViewHolder implements OnClickListener {
        @BindView(R.id.chat_item_msg) TextView Content;
        @BindView(R.id.chat_item_img) ImageView Img;
        @BindView(R.id.chat_item_name) TextView Name;
        @BindView(R.id.chat_item_new) ImageView Status;
        @BindView(R.id.chat_item_time) TextView Time;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent intent = new Intent(context.getContext(), ChatActivity.class);
            intent.putExtra("to_id", list.get(getLayoutPosition()).getUser_id());
            intent.putExtra("to_name",  list.get(getLayoutPosition()).getName());
            context.startActivity(intent);
        }
    }

    public ChatAdapter(ChatFragment context, List<ChatList> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context.getContext());
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatViewHolder(inflater.inflate(R.layout.chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatList item = list.get(position);
        if (holder instanceof ChatViewHolder) {
            ((ChatViewHolder) holder).Name.setText(item.getName());
            ((ChatViewHolder) holder).Time.setText(DateUtils.getChatTime(item.getSend_date()));
            ((ChatViewHolder) holder).Status.setVisibility(View.GONE);
            if(item.getContent().contains("http://")){
                ((ChatViewHolder) holder).Content.setText("图片");
            }
            else ((ChatViewHolder) holder).Content.setText(item.getContent());
            Glide.with(context).load(item.getImage()).into(((ChatViewHolder) holder).Img);
        }
    }

    public int getItemCount() {
        return list.size();
    }
}
