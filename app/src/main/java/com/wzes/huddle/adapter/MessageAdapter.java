package com.wzes.huddle.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Message;
import com.wzes.huddle.user_info.UserInfoActivity;
import com.wzes.huddle.util.DateUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class MessageAdapter extends Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<Message> mMsgList;

    static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        CircleImageView leftImg;
        RelativeLayout leftLayout;
        TextView leftMsg;
        CircleImageView rightImg;
        RelativeLayout rightLayout;
        TextView rightMsg;
        TextView timeTxt;

        public ViewHolder(View view) {
            super(view);
            this.timeTxt = view.findViewById(R.id.message_time);
            this.leftLayout = view.findViewById(R.id.left_layout);
            this.rightLayout = view.findViewById(R.id.right_layout);
            this.leftMsg = view.findViewById(R.id.left_txt);
            this.rightMsg = view.findViewById(R.id.right_txt);
            this.leftImg = view.findViewById(R.id.left_img);
            this.rightImg = view.findViewById(R.id.right_img);
        }
    }

    public MessageAdapter(Context context, List<Message> mMsgList) {
        this.mMsgList = mMsgList;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Message message = this.mMsgList.get(position);
        if (position == 0) {
            holder.timeTxt.setText(DateUtils.getChatTime(message.getSend_date()));
        } else if (DateUtils.ltTwo(mMsgList.get(position - 1).getSend_date(), message.getSend_date())) {
            holder.timeTxt.setVisibility(View.GONE);
        } else {
            holder.timeTxt.setVisibility(View.VISIBLE);
            holder.timeTxt.setText(DateUtils.getChatTime(message.getSend_date()));
        }
        if (message.getFrom_id().equals(Preferences.getUserAccount())) {
            Glide.with(this.context).load(message.getFrom_img()).into(holder.rightImg);
            holder.rightImg.setOnClickListener(v -> {
                Intent nIntent = new Intent(context, UserInfoActivity.class);
                nIntent.putExtra("user_id", mMsgList.get(position).getFrom_id());
                context.startActivity(nIntent);
            });
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(message.getContent());
            return;
        }
        Glide.with(this.context).load(message.getFrom_img()).into(holder.leftImg);
        holder.leftImg.setOnClickListener(v -> {
            Intent nIntent = new Intent(context, UserInfoActivity.class);
            nIntent.putExtra("user_id", mMsgList.get(position).getFrom_id());
            context.startActivity(nIntent);
        });
        holder.rightLayout.setVisibility(View.GONE);
        holder.leftLayout.setVisibility(View.VISIBLE);
        holder.leftMsg.setText(message.getContent());
    }

    public int getItemCount() {
        return this.mMsgList.size();
    }
}
