package com.wzes.huddle.activities.team;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wzes.huddle.R;
import com.wzes.huddle.adapter.TeamInfoAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.util.MyLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class TeamNearFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    private static List<Team> list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TeamInfoAdapter teamInfoAdapter;
    private static TeamNearFragment fragment;
    private String latitude, longitude;
    private static final String[] LOCATION =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int LOCATION_PERM = 123;

    private TeamNearFragment() {

    }

    public static TeamNearFragment newInstance() {
        if (fragment == null) {
            fragment = new TeamNearFragment();
        }
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocation();
    }


    @AfterPermissionGranted(LOCATION_PERM)
    public void initLocation() {
        if(!TextUtils.isEmpty(Preferences.getLatitude())){
            latitude = Preferences.getLatitude();
            longitude = Preferences.getLongitude();
            return;
        }

        if (!hasLocationPermission()) {
            EasyPermissions.requestPermissions(this, getString(R.string.internet_permission),
                    LOCATION_PERM, LOCATION);
        }
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        List<String> locationList = locationManager.getProviders(true);
        String provider;
        if (locationList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            MyLog.i("没有服务");
            return;
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            Preferences.saveLatitude(latitude);
            Preferences.saveLongitude(longitude);
        }else{
            MyLog.i("aa无法获取定位");
        }
        new Thread(() -> {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location1) {
                    latitude = String.valueOf(location1.getLatitude());
                    longitude = String.valueOf(location1.getLongitude());
                }

                @Override
                public void onStatusChanged(String provider1, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider1) {

                }

                @Override
                public void onProviderDisabled(String provider1) {

                }
            };

            /**
             * 第一个参数是提供定位的方式
             * 第二个参数是多少秒刷新
             * 第三个参数是移动多少距离
             * 第四个参数是监听器
             */
            try{
                locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
            }catch (Exception e){
                MyLog.e(e.getMessage());
            }
        }).start();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_item_near, container, false);
        recyclerView = view.findViewById(R.id.team_near_recyclerView);
        refreshLayout = view.findViewById(R.id.team_near_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this::refreshData);
        if (list == null) {
            refreshLayout.setRefreshing(true);
            new Thread(this::initData).start();
        } else {
            teamInfoAdapter = new TeamInfoAdapter(this, list);
            recyclerView.setAdapter(teamInfoAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return view;
    }

    public void refreshData() {
        if(TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)){
            MyRetrofit.getGsonRetrofit().getHotTeamList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Team>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull List<Team> teams) {
                            list = teams;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            teamInfoAdapter = new TeamInfoAdapter(TeamNearFragment.this, TeamNearFragment.list);
                            recyclerView.setAdapter(teamInfoAdapter);
                            refreshLayout.setRefreshing(false);
                        }
                    });
        }else{
            MyRetrofit.getGsonRetrofit().getNearTeamList(latitude, longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Team>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull List<Team> teams) {
                            list = teams;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            if(!(list.size() > 0 && list.get(0) != null)){
                                list = new ArrayList<>();
                            }
                            teamInfoAdapter = new TeamInfoAdapter(TeamNearFragment.this, TeamNearFragment.list);
                            recyclerView.setAdapter(teamInfoAdapter);
                            refreshLayout.setRefreshing(false);
                        }
                    });
        }

    }

    public void initData() {
        while(TextUtils.isEmpty(latitude)){
            initLocation();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)){
            MyRetrofit.getGsonRetrofit().getHotTeamList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Team>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull List<Team> teams) {
                            list = teams;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            if(!(list.size() > 0 && list.get(0) != null)){
                                list = new ArrayList<>();
                            }
                            teamInfoAdapter = new TeamInfoAdapter(TeamNearFragment.this, TeamNearFragment.list);
                            recyclerView.setAdapter(teamInfoAdapter);
                            refreshLayout.setRefreshing(false);
                        }
                    });
        }else{
            MyRetrofit.getGsonRetrofit().getNearTeamList(latitude, longitude)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Team>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull List<Team> teams) {
                            list = teams;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            refreshLayout.setRefreshing(false);
                            teamInfoAdapter = new TeamInfoAdapter(TeamNearFragment.this, TeamNearFragment.list);
                            recyclerView.setAdapter(teamInfoAdapter);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    });
        }
    }

    private boolean hasLocationPermission() {
        return EasyPermissions.hasPermissions(getContext(), LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
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
                    getContext(),
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
