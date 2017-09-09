package com.wzes.huddle.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wzes.huddle.R;
import com.wzes.huddle.bean.Chat;
import com.wzes.huddle.chatservice.ChatActivity;
import com.wzes.huddle.homepage.ChatFragment;
import com.wzes.huddle.myinterface.OnRecyclerViewOnClickListener;
import java.util.List;

public class ChatAdapter extends Adapter<android.support.v7.widget.RecyclerView.ViewHolder> {
    private ChatFragment context;
    private LayoutInflater inflater;
    private List<Chat> list;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder implements OnClickListener {
        TextView Content;
        ImageView Img;
        TextView Name;
        ImageView Status;
        TextView Time;

        public ViewHolder(View itemView) {
            super(itemView);
            this.Img = itemView.findViewById(R.id.chat_item_img);
            this.Content = itemView.findViewById(R.id.chat_item_msg);
            this.Name =  itemView.findViewById(R.id.chat_item_name);
            this.Status =  itemView.findViewById(R.id.chat_item_new);
            this.Time = itemView.findViewById(R.id.chat_item_time);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            Intent intent = new Intent(ChatAdapter.this.context.getContext(), ChatActivity.class);
            intent.putExtra("to_id", list.get(getLayoutPosition()).getUser_id());
            intent.putExtra("to_name",  list.get(getLayoutPosition()).getName());
            ChatAdapter.this.context.startActivity(intent);
        }
    }

    public ChatAdapter(ChatFragment context, List<Chat> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context.getContext());
        this.list = list;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.chat_item, parent, false));
    }

    public void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder, int position) {
        Chat item = this.list.get(position);
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).Name.setText(item.getName());
            ((ViewHolder) holder).Time.setText(item.getSend_date());
            ((ViewHolder) holder).Status.setVisibility(View.GONE);
            ((ViewHolder) holder).Content.setText(item.getContent());
            Glide.with(this.context).load(item.getImage()).into(((ViewHolder) holder).Img);
        }
    }

    public int getItemCount() {
        return this.list.size();
    }
}
