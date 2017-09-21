package com.wzes.huddle.add;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wzes.huddle.R;
import com.wzes.huddle.util.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddTeamActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.add_team_back_btn)
    ImageButton teamBackBtn;
    @BindView(R.id.add_team_title)
    EditText teamTitle;
    @BindView(R.id.add_team_content)
    EditText teamContent;
    @BindView(R.id.add_team_location)
    TextView teamLocation;
    @BindView(R.id.add_team_send_btn)
    ImageButton teamSendBtn;

    private LocationManager locationManager;
    private static final String[] LOCATION =
            {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_PERM = 123;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);
        ButterKnife.bind(this);
        locationTask();


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
        String latitude = location.getLatitude()+"";
        String longitude = location.getLongitude()+"";
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=pPGNKs75nVZPloDFuppTLFO3WXebPgXg&callback=renderReverse&location="+latitude+","+longitude+"&output=json&pois=0";
        try {
            run(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否有可用的内容提供器
     * @return 不存在返回null
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }else{
            Toast.makeText(AddTeamActivity.this, "没有可用的位置提供器",Toast.LENGTH_SHORT).show();
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
                String location = response.body().string().replace("renderReverse&&renderReverse","");
                location = location.replace("(","");
                location = location.replace(")","");
                String city = "";

                try {
                    JSONObject jsonObject = new JSONObject(location);
                    JSONObject address = null;
                    address = jsonObject.getJSONObject("result");
                    city = address.getString("formatted_address");
                    //String district = address.getString("sematic_description");
                    android.os.Message message = new android.os.Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("location", city);
                    message.what = 1;
                    message.setData(bundle);
                    handler.sendMessage(message);
                    MyLog.i(city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){

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


    @OnClick({R.id.add_team_back_btn, R.id.add_team_title, R.id.add_team_content, R.id.add_team_location, R.id.add_team_send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_team_back_btn:
                finish();
                break;
            case R.id.add_team_title:
                break;
            case R.id.add_team_content:
                break;
            case R.id.add_team_location:

                break;
            case R.id.add_team_send_btn:
                break;
        }
    }

    private boolean hasLocationPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION);
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
