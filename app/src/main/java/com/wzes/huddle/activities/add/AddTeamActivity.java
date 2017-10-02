package com.wzes.huddle.activities.add;

import android.Manifest;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.TeamAddImageAdapter;
import com.wzes.huddle.app.DemoCache;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Image;
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
import java.util.Date;
import java.util.Iterator;
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
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class AddTeamActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.add_team_back_btn)
    ImageButton teamBackBtn;
    @BindView(R.id.add_team_title)
    EditText teamTitle;
    @BindView(R.id.add_team_content)
    EditText teamContent;
    @BindView(R.id.add_team_location)
    TextView teamLocation;
    @BindView(R.id.add_team_sign_time)
    TextView teamsignTime;
    @BindView(R.id.add_team_send_btn)
    CircularProgressButton teamSendBtn;
    @BindView(R.id.team_add_recyclerView)
    RecyclerView addRecyclerView;
    @BindView(R.id.add_team_category)
    TextView teamCategory;
    @BindView(R.id.add_team_level)
    TextView teamLevel;

    private static final String[] LOCATION =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int LOCATION_PERM = 123;
    private static final int STORAGE_PERM = 234;
    @BindView(R.id.add_team_people)
    TextView addTeamPeople;
    private String provider;
    private int image_acount = 0;
    private int images = 0;

    private String locationname;
    private String locationlatitude;
    private String locationlongitude;
    private String team_id;

    public LocationClient mLocationClient = null;

    public BDAbstractLocationListener myListener = null;


    private TeamAddImageAdapter teamAddImageAdapter;
    private List<Image> list;


    private IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {

        public void onStart() {

        }

        public void onSuccess(List<String> photoList) {
            Iterator it = photoList.iterator();
            list.clear();
            while (it.hasNext()) {
                list.add(new Image((String) it.next()));
            }
            list.add(new Image("add"));
            teamAddImageAdapter.notifyDataSetChanged();

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
    private void uploadImage(String path, String team_id, String index) {

        Luban.with(AddTeamActivity.this)
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
                        RequestBody id = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), team_id);
                        RequestBody in = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), index);
                        MyRetrofit.getNormalRetrofit().uploadTeamimage(id, in, body)
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

                                    }

                                    @Override
                                    public void onComplete() {
                                        image_acount++;
                                        if (image_acount == images) {
                                            teamSendBtn.doneLoadingAnimation(
                                                    ContextCompat.getColor(AddTeamActivity.this, R.color.colorPrimaryDark),
                                                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                                            finish();
                                        }
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
                        .multiSelect(true)
                        .maxSize(10)
                        .isShowCamera(true)
                        .filePath("/Gallery/Pictures")
                        .build())
                .open(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        ButterKnife.bind(this);

        teamsignTime.setText(DateUtils.getDateTime(System.currentTimeMillis()));
        locationTask();

        list = new ArrayList<>();
        list.add(new Image("add"));

        teamAddImageAdapter = new TeamAddImageAdapter(AddTeamActivity.this, list);
        teamAddImageAdapter.setOnItemClickLitener((view, i) -> {
            switch (view.getId()) {
                case R.id.team_add_item_cancel:
                    list.remove(i);
                    teamAddImageAdapter.notifyDataSetChanged();
                    break;
            }
            if (i == list.size() - 1) {
                if (hasStoragePermission()) {
                    openGallery();
                } else {
                    Toast.makeText(this, "您禁止了读权限", Toast.LENGTH_SHORT).show();
                }

            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        addRecyclerView.setLayoutManager(layoutManager);

        int spanCount = 4; // 3 columns
        int spacing = 5;  // 50px
        boolean includeEdge = false;

        addRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        addRecyclerView.setAdapter(teamAddImageAdapter);

    }


    // 动画
    private void animateButton(final CircularProgressButton circularProgressButton) {
        images = list.size() - 1;
        circularProgressButton.startAnimation();
        String user_id = Preferences.getUserAccount();
        String content = teamContent.getText().toString();
        String title = teamTitle.getText().toString();
        String release_date = String.valueOf(System.currentTimeMillis());
        String start_date = String.valueOf(System.currentTimeMillis());
        String category = teamCategory.getText().toString();
        String level = teamLevel.getText().toString();
        String join_account = "5";
        if (!TextUtils.isEmpty(user_id) && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(release_date) && !TextUtils.isEmpty(start_date)
                && !TextUtils.isEmpty(category) && !TextUtils.isEmpty(level)) {
            RequestBody rUser_id = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), user_id);
            RequestBody rContent = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), content);
            RequestBody rTitle = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), title);
            RequestBody rRelease_date = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), release_date);
            RequestBody rStart_date = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), start_date);
            RequestBody rCategory = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), category);
            RequestBody rLevel = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), level);
            RequestBody rJoin_account = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), join_account);
            RequestBody rLocationname = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), locationname);
            RequestBody rLocationlatitude = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), locationlatitude);
            RequestBody rLocationlongitude = RequestBody.create(MediaType.parse(HttpHeaders.Values.MULTIPART_FORM_DATA), locationlongitude);

            MyRetrofit.getNormalRetrofit().addTeam(rUser_id, rTitle, rCategory, rContent, rStart_date, rRelease_date, rJoin_account,
                    rLevel, rLocationname, rLocationlatitude, rLocationlongitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull ResponseBody responseBody) {
                            try {
                                team_id = responseBody.string();
                                MyLog.i(team_id);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            Toast.makeText(AddTeamActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            for (int index = 0; index < list.size() - 1; index++) {
                                MyLog.i(list.get(index).getImage());
                                uploadImage(list.get(index).getImage(), team_id, index + "");
                            }

                        }
                    });

        } else {
            Toast.makeText(this, "请输入完成信息", Toast.LENGTH_SHORT).show();

        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount;   // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private void initLocation() {
        mLocationClient = new LocationClient(DemoCache.getContext());

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 10000;

        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        mLocationClient.setLocOption(option);

        //声明LocationClient类
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);


        mLocationClient.start();


    }

    @AfterPermissionGranted(LOCATION_PERM)
    private void locationTask() {
        String perm = Manifest.permission.INTERNET;
        if (hasLocationPermission()) {
            initLocation();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.internet_permission),
                    LOCATION_PERM, perm);
        }
    }


    @OnClick({R.id.add_team_back_btn, R.id.add_team_sign_time, R.id.add_team_level,
            R.id.add_team_category, R.id.add_team_location, R.id.add_team_send_btn, R.id.add_team_people})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_team_back_btn:
                finish();
                break;
            case R.id.add_team_level:
                ArrayList<String> lOptionsItems = new ArrayList<>();
                lOptionsItems.add("国家级");
                lOptionsItems.add("省级");
                lOptionsItems.add("市级");
                lOptionsItems.add("校级");
                lOptionsItems.add("自由");
                lOptionsItems.add("国际级");
                OptionsPickerView lOptions = new OptionsPickerView.Builder(this, (option1,option2, option3, v) -> {
                    String tx = lOptionsItems.get(option1);
                    teamLevel.setText(tx);
                }).build();
                lOptions.setPicker(lOptionsItems);
                lOptions.show();
                break;
            case R.id.add_team_category:
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
                OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, (option1,option2, option3, v) -> {
                    String tx = cOptionsItems.get(option1);
                    teamCategory.setText(tx);
                }).build();
                pvOptions.setPicker(cOptionsItems);
                pvOptions.show();
                break;
            case R.id.add_team_people:

                //条件选择器
                ArrayList<String> pOptionsItems = new ArrayList<>();
                for(int index = 2; index < 50; index ++){
                    pOptionsItems.add(index + "");
                }
                pOptionsItems.add("不限");

                OptionsPickerView pOptions = new OptionsPickerView.Builder(this, (option1,option2, option3, v) -> {
                    String tx = pOptionsItems.get(option1);
                    addTeamPeople.setText(tx);
                }).build();
                pOptions.setPicker(pOptionsItems);
                pOptions.show();
                break;
            case R.id.add_team_location:

                break;
            case R.id.add_team_send_btn:
                animateButton(teamSendBtn);
                break;
            case R.id.add_team_sign_time:
                //时间选择器
                TimePickerView pvTime = new TimePickerView.Builder(this, (date, v) -> { //选中事件回调
                    teamsignTime.setText(DateUtils.getDateTime(date.getTime()));
                })
                        .build();
                pvTime.setDate(Calendar.getInstance()); //注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
        }
    }

    private boolean hasLocationPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasLocationPermission() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }


    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            locationlatitude = String.valueOf(location.getLatitude());
            locationlongitude = String.valueOf(location.getLongitude());
            if (TextUtils.isEmpty(location.getCity())) {
                locationlatitude = String.valueOf(-1);
                locationlongitude = String.valueOf(-1);
                locationname = "null";
                teamLocation.setVisibility(View.GONE);
            } else {
                teamLocation.setVisibility(View.VISIBLE);
                locationlatitude = String.valueOf(location.getLatitude());
                locationlongitude = String.valueOf(location.getLongitude());
                locationname = location.getCity() + location.getDistrict() + location.getStreet();
                teamLocation.setText(locationname);
            }

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                //当前为GPS定位结果，可获取以下信息
                location.getSpeed();    //获取当前速度，单位：公里每小时
                location.getSatelliteNumber();    //获取当前卫星数
                location.getAltitude();    //获取海拔高度信息，单位米
                location.getDirection();    //获取方向信息，单位度

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                //当前为网络定位结果，可获取以下信息
                location.getOperators();    //获取运营商信息

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                //当前为网络定位结果

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                //当前网络定位失败
                //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                //当前网络不通

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码

            }
        }
    }
}
