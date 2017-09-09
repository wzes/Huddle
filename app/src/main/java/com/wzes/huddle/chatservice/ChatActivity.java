package com.wzes.huddle.chatservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.MessageAdapter;
import com.wzes.huddle.app.DemoCache;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Message;
import com.wzes.huddle.homepage.MyFragment;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.service.RetrofitService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit.Builder;

public class ChatActivity extends AppCompatActivity implements OnClickListener {
    public static List<Message> list;
    public static MessageAdapter messageAdapter;
    public static RecyclerView recyclerView;
    private static String text;
    private static String to_id;
    private static String to_img;
    private static String to_name;
    private static String user_id = Preferences.getUserAccount();
    @BindView(R.id.chat_back)
    public ImageButton backBtn;
    @BindView(R.id.chat_send)
    public ImageButton sendBtn;
    @BindView(R.id.chat_text)
    public TextInputEditText textTxt;
    @BindView(R.id.msg_title)
    public TextView titleTxt;

    public static class UserReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            ChatActivity.refreshData(intent.getStringExtra("msg"));
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        to_id = intent.getStringExtra("to_id");
        to_name = intent.getStringExtra("to_name");
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.msg_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sendBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        titleTxt.setText(to_name);
        MyRetrofit.getGsonRetrofit().getMessageListByID(Preferences.getUserAccount(), to_id)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Observer<List<Message>>() {
                  @Override
                  public void onSubscribe(@NonNull Disposable d) {

                  }

                  @Override
                  public void onNext(@NonNull List<Message> messages) {
                      list = messages;
                  }

                  @Override
                  public void onError(@NonNull Throwable e) {

                  }

                  @Override
                  public void onComplete() {
                      int size;
                      if (ChatActivity.list == null) {
                          ChatActivity.list = new ArrayList();
                      }
                      if (ChatActivity.list.size() > 0) {
                          if ((ChatActivity.list.get(0)).getTo_id().equals(Preferences.getUserAccount())) {
                              ChatActivity.to_img = (list.get(0)).getFrom_img();
                          } else {
                              ChatActivity.to_img = (list.get(0)).getTo_img();
                          }
                      }
                      ChatActivity.messageAdapter = new MessageAdapter(ChatActivity.this, ChatActivity.list);
                      ChatActivity.recyclerView.setAdapter(ChatActivity.messageAdapter);
                      RecyclerView recyclerView = ChatActivity.recyclerView;
                      if (ChatActivity.list.size() > 0) {
                          size = ChatActivity.list.size() - 1;
                      } else {
                          size = 0;
                      }
                      recyclerView.scrollToPosition(size);
                  }
              });
    }

    public static void refreshData(String msg) {
        Gson gson = new Gson();
        JsonElement el = new JsonParser().parse(msg);
        if (list != null) {
            Message message;
            if (el.isJsonObject()) {
                message = gson.fromJson(el.getAsJsonObject(), Message.class);
                message.setFrom_img(to_img);
                list.add(message);
            } else if (el.isJsonArray()) {
                Iterator it = el.getAsJsonArray().iterator();
                while (it.hasNext()) {
                    message = gson.fromJson((JsonElement) it.next(), Message.class);
                    message.setFrom_img(to_img);
                    list.add(message);
                }
            }
            messageAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(list.size() > 0 ? list.size() - 1 : 0);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_back:
                finish();
                return;
            case R.id.chat_send:
                text = this.textTxt.getText().toString();
                if (text.length() != 0) {
                    if (list == null) {
                        list = new ArrayList();
                    }
                    Message message = new Message();
                    message.setFrom_id(user_id);
                    message.setTo_id(to_id);
                    message.setContent(text);
                    message.setTo_img("");
                    message.setFrom_img(MyFragment.currentUser.getImage());
                    message.setSend_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    message.setMessage_type("words");
                    String rs = new Gson().toJson(message);
                    ChatService.clientMain.sendMsg(rs);
                    list.add(message);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(list.size() > 0 ? list.size() - 1 : 0);
                    this.textTxt.setText("");
                    Intent intent = new Intent();
                    intent.setAction("com.wzes.huddle.list");
                    intent.putExtra("msg", rs);
                    DemoCache.getContext().sendBroadcast(intent);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
