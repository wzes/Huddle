package com.wzes.huddle.activities.add;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.util.DateUtils;
import com.wzes.huddle.util.GalleryGlideImageLoader;
import com.wzes.huddle.util.MyLog;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.netty.handler.codec.http.HttpHeaders;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class AddEventActivity extends AppCompatActivity {

    @BindView(R.id.add_event_back_btn)
    ImageButton addEventBackBtn;
    @BindView(R.id.add_event_title)
    EditText addEventTitle;
    @BindView(R.id.add_event_content)
    EditText addEventContent;
    @BindView(R.id.add_event_enrool_start_date)
    TextView addEventEnroolStartDate;
    @BindView(R.id.add_event_enrool_end_date)
    TextView addEventEnroolEndDate;
    @BindView(R.id.add_event_match_start_date)
    TextView addEventMatchStartDate;
    @BindView(R.id.add_event_match_end_date)
    TextView addEventMatchEndDate;
    @BindView(R.id.add_event_type)
    TextView addEventType;
    @BindView(R.id.add_event_level)
    TextView addEventLevel;
    @BindView(R.id.add_event_add_iamge)
    TextView addEventAddIamge;
    @BindView(R.id.add_event_image)
    ImageView addEventImage;
    @BindView(R.id.add_event_send_btn)
    CircularProgressButton addEventSendBtn;
    @BindView(R.id.add_event_organiser)
    EditText addEventOrganiser;


    private String imgPath;
    private String event_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.add_event_back_btn, R.id.add_event_enrool_start_date, R.id.add_event_enrool_end_date,
            R.id.add_event_match_start_date, R.id.add_event_match_end_date, R.id.add_event_type,
            R.id.add_event_level, R.id.add_event_add_iamge, R.id.add_event_send_btn, R.id.add_event_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_event_back_btn:
                finish();
                break;
            case R.id.add_event_enrool_start_date:
                //时间选择器
                TimePickerView addEventEnroolStartTime = new TimePickerView.Builder(this, (date, v) -> { //选中事件回调
                    addEventEnroolStartDate.setText(DateUtils.getYearTime(date.getTime()));
                }).build();
                addEventEnroolStartTime.setDate(Calendar.getInstance()); //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                addEventEnroolStartTime.show();
                break;
            case R.id.add_event_enrool_end_date:
                TimePickerView addEventEnroolEndTime = new TimePickerView.Builder(this, (date, v) -> { //选中事件回调
                    addEventEnroolEndDate.setText(DateUtils.getYearTime(date.getTime()));
                }).build();
                addEventEnroolEndTime.setDate(Calendar.getInstance()); //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                addEventEnroolEndTime.show();
                break;
            case R.id.add_event_match_start_date:
                TimePickerView  addEventMatchStartTime = new TimePickerView.Builder(this, (date, v) -> { //选中事件回调
                    addEventMatchStartDate.setText(DateUtils.getYearTime(date.getTime()));
                }).build();
                addEventMatchStartTime.setDate(Calendar.getInstance()); //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                addEventMatchStartTime.show();
                break;
            case R.id.add_event_match_end_date:
                TimePickerView addEventMatchEndTime = new TimePickerView.Builder(this, (date, v) -> { //选中事件回调
                    addEventMatchEndDate.setText(DateUtils.getYearTime(date.getTime()));
                }).build();
                addEventMatchEndTime.setDate(Calendar.getInstance()); //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                addEventMatchEndTime.show();
                break;
            case R.id.add_event_type:
                ArrayList<String> cOptionsItems = new ArrayList<>();
                cOptionsItems.add("健身锻炼");
                cOptionsItems.add("出行拼车");
                cOptionsItems.add("励志计划");
                cOptionsItems.add("周末骑行");
                cOptionsItems.add("学术竞赛");
                cOptionsItems.add("数学建模");
                cOptionsItems.add("数学竞赛");
                cOptionsItems.add("物理竞赛");
                cOptionsItems.add("约图书馆");
                cOptionsItems.add("组团出游");
                cOptionsItems.add("组团跑步");
                cOptionsItems.add("英语竞赛");
                cOptionsItems.add("购物拼单");
                cOptionsItems.add("跑马拉松");
                OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (option1, option2, option3, v) -> {
                    String tx = cOptionsItems.get(option1);
                    addEventType.setText(tx);
                }).build();
                pvOptions.setPicker(cOptionsItems);
                pvOptions.show();
                break;
            case R.id.add_event_level:
                ArrayList<String> lOptionsItems = new ArrayList<>();
                lOptionsItems.add("国家级");
                lOptionsItems.add("省级");
                lOptionsItems.add("市级");
                lOptionsItems.add("校级");
                lOptionsItems.add("自由");
                lOptionsItems.add("国际级");
                OptionsPickerView lOptions = new OptionsPickerView.Builder(this, (option1,option2, option3, v) -> {
                    String tx = lOptionsItems.get(option1);
                    addEventLevel.setText(tx);
                }).build();
                lOptions.setPicker(lOptionsItems);
                lOptions.show();
                break;
            case R.id.add_event_image:
                openGallery();
                break;
            case R.id.add_event_add_iamge:
                openGallery();
                break;
            case R.id.add_event_send_btn:
                animateButton(addEventSendBtn);
                break;
        }
    }

    // 动画
    private void animateButton(final CircularProgressButton circularProgressButton) {
        circularProgressButton.startAnimation();
        String user_id = Preferences.getUserAccount();
        String content = addEventContent.getText().toString();
        String title = addEventTitle.getText().toString();
        String release_date = String.valueOf(System.currentTimeMillis());
        String enrool_start_date = String.valueOf(System.currentTimeMillis());
        String enrool_end_date = String.valueOf(System.currentTimeMillis());
        String macth_start_date = String.valueOf(System.currentTimeMillis());
        String macth_end_date = String.valueOf(System.currentTimeMillis());
        String event_type = addEventType.getText().toString();
        String level = addEventLevel.getText().toString();
        String organizer = addEventOrganiser.getText().toString();
        if (!TextUtils.isEmpty(user_id) && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(release_date) && !TextUtils.isEmpty(enrool_start_date)
                && !TextUtils.isEmpty(enrool_end_date) && !TextUtils.isEmpty(macth_start_date)
                && !TextUtils.isEmpty(macth_end_date)
                && !TextUtils.isEmpty(event_type) && !TextUtils.isEmpty(level)) {
            RequestBody rUser_id = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), user_id);
            RequestBody rContent = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), content);
            RequestBody rTitle = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), title);
            RequestBody rRelease_date = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), release_date);
            RequestBody rEnrool_start_date = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), enrool_start_date);
            RequestBody rEnrool_end_date = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), enrool_end_date);
            RequestBody rMacth_start_date = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), macth_start_date);
            RequestBody rMacth_end_date = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), macth_end_date);
            RequestBody rOrganizer = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), organizer);
            RequestBody rEvent_type = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), event_type);
            RequestBody rLevel = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), level);

            MyRetrofit.getNormalRetrofit().addEvent(rUser_id, rTitle, rEvent_type, rContent, rOrganizer,rEnrool_start_date,
                    rEnrool_end_date, rMacth_start_date, rMacth_end_date, rRelease_date, rLevel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull ResponseBody responseBody) {
                            try {
                                event_id = responseBody.string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            Toast.makeText(AddEventActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            addEventSendBtn.doneLoadingAnimation(
                                    ContextCompat.getColor(AddEventActivity.this, R.color.colorPrimaryDark),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));

                        }

                        @Override
                        public void onComplete() {
                            uploadImage(imgPath, event_id);
                        }
                    });

        } else {
            Toast.makeText(this, "请输入完成信息", Toast.LENGTH_SHORT).show();

        }
    }

    private IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {

        public void onStart() {

        }

        public void onSuccess(List<String> photoList) {
            if (photoList.size() > 0) {
                imgPath = photoList.get(0);
                addEventImage.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(imgPath).into(addEventImage);
            }
        }


        public void onCancel() {

        }

        public void onFinish() {

        }

        public void onError() {

        }
    };

    /**
     * @param path
     */
    private void uploadImage(String path, String event_id) {

        Luban.with(AddEventActivity.this)
                .load(path)                                   // 传人要压缩的图片列表
                .ignoreBy(100)                                // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
                        RequestBody id = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), event_id);
                        MyRetrofit.getNormalRetrofit().uploadEventimage(id, body)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<ResponseBody>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onNext(@NonNull ResponseBody responseBody) {
                                        try {
                                            MyLog.i(responseBody.string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        addEventSendBtn.doneLoadingAnimation(
                                                ContextCompat.getColor(AddEventActivity.this, R.color.colorPrimaryDark),
                                                BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                                    }

                                    @Override
                                    public void onComplete() {
                                        addEventSendBtn.doneLoadingAnimation(
                                                ContextCompat.getColor(AddEventActivity.this, R.color.colorPrimaryDark),
                                                BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                                        finish();
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        MyLog.i(e.getMessage());
                    }
                }).launch();    //启动压缩
    }

    private void openGallery() {
        GalleryPick.getInstance()
                .setGalleryConfig(new GalleryConfig.Builder()
                        .imageLoader(new GalleryGlideImageLoader())
                        .iHandlerCallBack(iHandlerCallBack)
                        .provider("com.yancy.gallerypickdemo.fileprovider")
                        .pathList(new ArrayList<>())
                        .multiSelect(false)
                        .isShowCamera(true)
                        .filePath("/Gallery/Pictures")
                        .build())
                .open(this);
    }
}
