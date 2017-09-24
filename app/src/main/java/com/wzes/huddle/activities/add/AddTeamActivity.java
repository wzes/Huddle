package com.wzes.huddle.activities.add;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.wzes.huddle.R;
import com.wzes.huddle.adapter.TeamAddImageAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Image;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.util.GalleryGlideImageLoader;
import com.wzes.huddle.util.MyLog;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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

    private LocationManager locationManager;
    private static final String[] LOCATION =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int LOCATION_PERM = 123;
    private static final int STORAGE_PERM = 234;
    private String provider;
    private int image_acount = 0;
    private int images = 0;

    private String locationname;
    private String locationlatitude;
    private String locationlongitude;
    private String team_id;

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
     *
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
                                        if(image_acount == images){
                                            teamSendBtn.doneLoadingAnimation(
                                                    ContextCompat.getColor(AddTeamActivity.this, R.color.colorPrimaryDark),
                                                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
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
        if(!TextUtils.isEmpty(user_id) && !TextUtils.isEmpty(content) && !TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(release_date) && !TextUtils.isEmpty(start_date)
                && !TextUtils.isEmpty(category) && !TextUtils.isEmpty(level)){
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
                    rLevel, rLocationname, rLocationlatitude,  rLocationlongitude)
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
                            for(int index = 0; index < list.size() -1; index ++){
                                MyLog.i(list.get(index).getImage());
                                uploadImage(list.get(index).getImage(), team_id , index + "");
                            }
                        }
                    });

        }else{
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

    public void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> locationList = locationManager.getProviders(true);

        if (locationList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(getApplicationContext(), "没有可用的定位服务", Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            showLocation(location);
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                showLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        /**
         * 第一个参数是提供定位的方式
         * 第二个参数是多少秒刷新
         * 第三个参数是移动多少距离
         * 第四个参数是监听器
         */
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    private void showLocation(Location location) {
        //String content = "--->" + location.getLatitude() + "\n" + location.getLongitude();

        //teamLocation.setText(content);
        getLocation(location);
    }

    /**
     * 得到当前经纬度并开启线程去反向地理编码
     */
    public void getLocation(Location location) {
        String latitude = location.getLatitude() + "";
        String longitude = location.getLongitude() + "";
        locationlatitude = latitude;
        locationlongitude = longitude;
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=pPGNKs75nVZPloDFuppTLFO3WXebPgXg&callback=renderReverse&location=" + latitude + "," + longitude + "&output=json&pois=0";
        try {
            run(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否有可用的内容提供器
     *
     * @return 不存在返回null
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(AddTeamActivity.this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private final OkHttpClient client = new OkHttpClient();

    public void run(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                String location = response.body().string().replace("renderReverse&&renderReverse", "");
                location = location.replace("(", "");
                location = location.replace(")", "");
                String city = "";

                try {
                    JSONObject jsonObject = new JSONObject(location);
                    JSONObject address = null;
                    address = jsonObject.getJSONObject("result");
                    city = address.getString("formatted_address");
                    //String district = address.getString("sematic_description");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("location", city);
                    message.what = 1;
                    message.setData(bundle);
                    handler.sendMessage(message);
                    locationname = city;
                    MyLog.i(city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            teamLocation.setText(msg.getData().getString("location"));
            super.handleMessage(msg);
        }
    };

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


    @OnClick({R.id.add_team_back_btn, R.id.add_team_sign_time,
            R.id.add_team_category, R.id.add_team_location, R.id.add_team_send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_team_back_btn:
                finish();
                break;
            case R.id.add_team_category:
                break;
            case R.id.add_team_location:

                break;
            case R.id.add_team_send_btn:
                animateButton(teamSendBtn);
//                teamSendBtn.startAnimation();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                teamSendBtn.revertAnimation(() -> Toast.makeText(this, "success", Toast.LENGTH_SHORT).show());
                break;
            case R.id.add_team_sign_time:

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

}
