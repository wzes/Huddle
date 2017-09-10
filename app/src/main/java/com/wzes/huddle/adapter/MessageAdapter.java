package com.wzes.huddle.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Message;
import com.wzes.huddle.user_info.UserInfoActivity;
import com.wzes.huddle.util.DateUtils;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Message> mMsgList;

    class MsgViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.message_time) TextView messageTime;
        @BindView(R.id.right_txt) TextView rightTxt;
        @BindView(R.id.right_image) ImageView rightImage;
        @BindView(R.id.right_photo) CircleImageView rightPhoto;
        @BindView(R.id.right_layout) RelativeLayout rightLayout;
        @BindView(R.id.left_photo) CircleImageView leftPhoto;
        @BindView(R.id.left_image) ImageView leftImage;
        @BindView(R.id.left_txt) TextView leftTxt;
        @BindView(R.id.left_layout) RelativeLayout leftLayout;

        MsgViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public MessageAdapter(Context context, List<Message> mMsgList) {
        this.mMsgList = mMsgList;
        this.context = context;
    }

    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MsgViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = mMsgList.get(position);
        if (position == 0) {
            ((MsgViewHolder)holder).messageTime.setText(DateUtils.getChatTime(message.getSend_date()));
            ((MsgViewHolder)holder).messageTime.setVisibility(View.VISIBLE);
        } else if (DateUtils.ltTwo(mMsgList.get(position - 1).getSend_date(), message.getSend_date())) {
            ((MsgViewHolder)holder).messageTime.setVisibility(View.GONE);
        } else {
            ((MsgViewHolder)holder).messageTime.setVisibility(View.VISIBLE);
            ((MsgViewHolder)holder).messageTime.setText(DateUtils.getChatTime(message.getSend_date()));
        }
        if (message.getFrom_id().equals(Preferences.getUserAccount())) {

            Glide.with(context).load(message.getFrom_img()).into(((MsgViewHolder)holder).rightPhoto);
            ((MsgViewHolder)holder).rightPhoto.setOnClickListener(v -> {
                Intent nIntent = new Intent(context, UserInfoActivity.class);
                nIntent.putExtra("user_id", mMsgList.get(position).getFrom_id());
                context.startActivity(nIntent);
            });
            ((MsgViewHolder)holder).rightLayout.setVisibility(View.VISIBLE);
            ((MsgViewHolder)holder).leftLayout.setVisibility(View.GONE);
            if(mMsgList.get(position).getMessage_type().equals("words")){
                ((MsgViewHolder)holder).rightTxt.setVisibility(View.VISIBLE);
                ((MsgViewHolder)holder).rightImage.setVisibility(View.GONE);
                ((MsgViewHolder)holder).rightTxt.setText(message.getContent());
            }else if(mMsgList.get(position).getMessage_type().equals("image")){
                ((MsgViewHolder)holder).rightTxt.setVisibility(View.GONE);
                ((MsgViewHolder)holder).rightImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getContent()).into(((MsgViewHolder)holder).rightImage);
            }
            return;
        }
        Glide.with(context).load(message.getFrom_img()).into(((MsgViewHolder)holder).leftPhoto);
        ((MsgViewHolder)holder).leftPhoto.setOnClickListener(v -> {
            Intent nIntent = new Intent(context, UserInfoActivity.class);
            nIntent.putExtra("user_id", mMsgList.get(position).getFrom_id());
            context.startActivity(nIntent);
        });

        ((MsgViewHolder)holder).rightLayout.setVisibility(View.GONE);
        ((MsgViewHolder)holder).leftLayout.setVisibility(View.VISIBLE);
        if(mMsgList.get(position).getMessage_type().equals("words")){
            ((MsgViewHolder)holder).leftTxt.setVisibility(View.VISIBLE);
            ((MsgViewHolder)holder).leftImage.setVisibility(View.GONE);
            ((MsgViewHolder)holder).leftTxt.setText(message.getContent());
        }else if(mMsgList.get(position).getMessage_type().equals("image")){
            ((MsgViewHolder)holder).leftTxt.setVisibility(View.GONE);
            ((MsgViewHolder)holder).leftImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(message.getContent()).into(((MsgViewHolder)holder).leftImage);
        }
    }



    public int getItemCount() {
        return mMsgList.size();
    }
}
