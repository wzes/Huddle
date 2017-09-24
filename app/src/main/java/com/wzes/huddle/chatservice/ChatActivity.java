package com.wzes.huddle.chatservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.MessageAdapter;
import com.wzes.huddle.app.DemoCache;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Message;
import com.wzes.huddle.homepage.MyFragment;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.util.GalleryGlideImageLoader;
import com.wzes.huddle.util.MyLog;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.netty.handler.codec.http.HttpHeaders;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class ChatActivity extends AppCompatActivity implements OnClickListener {
    private int MSG_TYPE = 0;
    private List<Message> list;
    private MessageAdapter messageAdapter;
    private String text;
    private String to_id;
    private String to_img;
    private String to_name;
    private UserReceiver userReceiver;
    private String user_id;
    private String msgPath;
    private String message_id;

    @BindView(R.id.chat_back_btn) ImageButton backBtn;
    @BindView(R.id.msg_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.chat_voice) ImageButton chatVoice;
    @BindView(R.id.chat_send) ImageButton sendBtn;
    @BindView(R.id.chat_add) ImageButton addBtn;
    @BindView(R.id.chat_text) EditText textTxt;
    @BindView(R.id.msg_title) TextView titleTxt;

    public class UserReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            refreshData(intent.getStringExtra("msg"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(userReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        user_id = Preferences.getUserAccount();
        to_id = intent.getStringExtra("to_id");
        to_name = intent.getStringExtra("to_name");
        MyLog.i(to_id + " " + user_id);
        // 广播
        userReceiver = new UserReceiver();
        IntentFilter intentFilter = new IntentFilter("com.wzes.huddle.user");
        registerReceiver(userReceiver, intentFilter );

        sendBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        titleTxt.setText(to_name);

        titleTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        MyRetrofit.getGsonRetrofit()
                .getMessageListByID(user_id, to_id)
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
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        messageAdapter = new MessageAdapter(ChatActivity.this, list);
                        recyclerView.setAdapter(messageAdapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                    }

                    @Override
                    public void onComplete() {
                        int size;
                        if(!(list.size() > 0 && list.get(0) != null)){
                            list = new ArrayList<>();
                        }
                        if (list.size() > 0) {
                            if ((list.get(0)).getTo_id().equals(user_id)) {
                                to_img = (list.get(0)).getFrom_img();
                            } else {
                                to_img = (list.get(0)).getTo_img();
                            }
                        }
                        messageAdapter = new MessageAdapter(ChatActivity.this, list);
                        recyclerView.setAdapter(messageAdapter);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                        if (list.size() > 0) {
                            size = list.size() - 1;
                        } else {
                            size = 0;
                        }
                        recyclerView.scrollToPosition(size);
                    }
                });
    }

    public void refreshData(String msg) {
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

    private IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {

        public void onStart() {

        }

        public void onSuccess(List<String> photoList) {
            Iterator it = photoList.iterator();
            if (it.hasNext()) {
                String path = (String) it.next();
                msgPath = path;
                SpannableString spanString = new SpannableString(" ");
                Bitmap loadedImage = BitmapFactory.decodeFile(path);
                Matrix matrix = new Matrix();
                loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(),
                        loadedImage.getHeight(), matrix, true);
                ImageSpan imageSpan = new ImageSpan(ChatActivity.this, loadedImage);
                spanString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                textTxt.setText(spanString);
            }
        }

        public void onCancel() {
        }

        public void onFinish() {
        }

        public void onError() {
        }
    };

    private void uploadImage(String path, String id) {

        Luban.with(ChatActivity.this)
                .load(path)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {

                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
                        RequestBody message_id =
                                RequestBody.create(
                                        MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), id);
                        MyRetrofit.getNormalRetrofit().uploadMessageimage(message_id, body)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<ResponseBody>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {
                                        //uploadmessageimage.php
                                    }

                                    @Override
                                    public void onNext(@NonNull ResponseBody responseBody) {

                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        MyLog.i("error");
                                    }

                                    @Override
                                    public void onComplete() {
                                        MyLog.i("Complete");
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            openGallery();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            Toast.makeText(this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        }
    }

    private void openGallery() {
        GalleryPick.getInstance()
                .setGalleryConfig(new GalleryConfig.Builder()
                        .imageLoader(new GalleryGlideImageLoader())
                        .iHandlerCallBack(iHandlerCallBack)
                        .provider("com.yancy.gallerypickdemo.fileprovider")
                        .pathList(new ArrayList<>())
                        .multiSelect(false)
                        .crop(true)
                        .isShowCamera(true)
                        .filePath("/Gallery/Pictures")
                        .build())
                .open(this);
    }

    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(this, "您禁止了权限", 0).show();
                    return;
                } else {
                    openGallery();
                    return;
                }
            default:
                break;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_back_btn:
                finish();
                break;
            case R.id.chat_send:
                text = textTxt.getText().toString();
                if(text.length() == 0){
                    Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    textTxt.setFocusable(true);
                    break;
                }
                if(MSG_TYPE == 1){
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    Message message = new Message();
                    message.setFrom_id(user_id);
                    message.setTo_id(to_id);
                    message.setContent(msgPath);
                    MyLog.i("msgPath" + " " +msgPath);
                    message.setTo_img("");
                    message.setFrom_img(MyFragment.currentUser.getImage());
                    long time = System.currentTimeMillis();
                    message_id = String.valueOf(time);
                    message.setSend_date(time);
                    message.setMessage_type("image");
                    list.add(message);

                    uploadImage(msgPath, message_id);
                    Message messageNet = new Message();
                    messageNet.setFrom_id(user_id);
                    messageNet.setTo_id(to_id);
                    messageNet.setContent("http://59.110.136.134/huddle/messages/" + message_id + ".jpg");
                    messageNet.setTo_img("");
                    messageNet.setFrom_img(MyFragment.currentUser.getImage());
                    messageNet.setSend_date(time);
                    messageNet.setMessage_type("image");
                    String rs = new Gson().toJson(messageNet);
                    ChatService.clientMain.sendMsg(rs);

                    Intent intent = new Intent();
                    intent.setAction("com.wzes.huddle.list");
                    intent.putExtra("msg", rs);
                    DemoCache.getContext().sendBroadcast(intent);
                }
                if (MSG_TYPE == 1 && text.length() > 1) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    Message message = new Message();
                    message.setFrom_id(user_id);
                    message.setTo_id(to_id);
                    message.setContent(text.substring(1));
                    message.setTo_img("");
                    message.setFrom_img(MyFragment.currentUser.getImage());
                    message.setSend_date(System.currentTimeMillis());
                    message.setMessage_type("words");
                    list.add(message);


                    String rs = new Gson().toJson(message);
                    ChatService.clientMain.sendMsg(rs);

                    Intent intent = new Intent();
                    intent.setAction("com.wzes.huddle.list");
                    intent.putExtra("msg", rs);
                    DemoCache.getContext().sendBroadcast(intent);
                }
                if (MSG_TYPE == 0 && text.length() > 0) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }

                    Message message = new Message();
                    message.setFrom_id(user_id);
                    message.setTo_id(to_id);
                    message.setContent(text);
                    message.setTo_img("");
                    message.setFrom_img(MyFragment.currentUser.getImage());
                    message.setSend_date(System.currentTimeMillis());
                    message.setMessage_type("words");
                    list.add(message);

                    String rs = new Gson().toJson(message);
                    ChatService.clientMain.sendMsg(rs);
                    messageAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(list.size() > 0 ? list.size() - 1 : 0);
                    textTxt.setText("");

                    Intent intent = new Intent();
                    intent.setAction("com.wzes.huddle.list");
                    intent.putExtra("msg", rs);
                    DemoCache.getContext().sendBroadcast(intent);
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(list.size() > 0 ? list.size() - 1 : 0);
                textTxt.setText("");
                break;
            case R.id.chat_add:
                MSG_TYPE = 1;
                requestPermission();
                break;
            default:
                break;
        }
    }
}
