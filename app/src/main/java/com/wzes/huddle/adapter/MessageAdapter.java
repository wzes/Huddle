package com.wzes.huddle.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.wzes.huddle.C0479R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Message;
import com.wzes.huddle.user_info.UserInfoActivity;
import com.wzes.huddle.util.DateUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;

public class MessageAdapter extends Adapter<ViewHolder> {
    private static Bitmap left;
    private static Bitmap right;
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
            this.timeTxt = (TextView) view.findViewById(C0479R.id.message_time);
            this.leftLayout = (RelativeLayout) view.findViewById(C0479R.id.left_layout);
            this.rightLayout = (RelativeLayout) view.findViewById(C0479R.id.right_layout);
            this.leftMsg = (TextView) view.findViewById(C0479R.id.left_txt);
            this.rightMsg = (TextView) view.findViewById(C0479R.id.right_txt);
            this.leftImg = (CircleImageView) view.findViewById(C0479R.id.left_img);
            this.rightImg = (CircleImageView) view.findViewById(C0479R.id.right_img);
        }
    }

    public MessageAdapter(Context context, List<Message> mMsgList) {
        this.mMsgList = mMsgList;
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(C0479R.layout.message_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        Message message = (Message) this.mMsgList.get(position);
        if (position == 0) {
            holder.timeTxt.setText(message.getSend_date());
        } else if (DateUtils.ltTwo(((Message) this.mMsgList.get(position - 1)).getSend_date(), message.getSend_date())) {
            holder.timeTxt.setVisibility(8);
        } else {
            holder.timeTxt.setText(message.getSend_date());
        }
        if (message.getFrom_id().equals(Preferences.getUserAccount())) {
            Glide.with(this.context).load(message.getFrom_img()).centerCrop().into(holder.rightImg);
            holder.rightImg.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent nIntent = new Intent(MessageAdapter.this.context, UserInfoActivity.class);
                    nIntent.putExtra("user_id", ((Message) MessageAdapter.this.mMsgList.get(position)).getFrom_id());
                    MessageAdapter.this.context.startActivity(nIntent);
                }
            });
            holder.rightLayout.setVisibility(0);
            holder.leftLayout.setVisibility(8);
            holder.rightMsg.setText(message.getContent());
            return;
        }
        Glide.with(this.context).load(message.getFrom_img()).centerCrop().into(holder.leftImg);
        holder.leftImg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent nIntent = new Intent(MessageAdapter.this.context, UserInfoActivity.class);
                nIntent.putExtra("user_id", ((Message) MessageAdapter.this.mMsgList.get(position)).getFrom_id());
                MessageAdapter.this.context.startActivity(nIntent);
            }
        });
        holder.rightLayout.setVisibility(8);
        holder.leftLayout.setVisibility(0);
        holder.leftMsg.setText(message.getContent());
    }

    public int getItemCount() {
        return this.mMsgList.size();
    }
}
