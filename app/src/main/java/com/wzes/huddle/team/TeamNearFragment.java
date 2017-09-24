package com.wzes.huddle.team;

import android.Manifest;
import android.content.Context;
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
import android.widget.Toast;

import com.wzes.huddle.R;
import com.wzes.huddle.adapter.TeamInfoAdapter;
import com.wzes.huddle.bean.Team;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.util.MyLog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TeamNearFragment extends Fragment {
    private static List<Team> list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private TeamInfoAdapter teamInfoAdapter;
    private static TeamNearFragment fragment;
    private String latitude, longitude;
    
    private TeamNearFragment(){

    }
    public static TeamNearFragment newInstance() {
        if(fragment == null){
            fragment = new TeamNearFragment();
        }
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocation();
    }
    public void initLocation() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        List<String> locationList = locationManager.getProviders(true);
        String provider = "";
        if (locationList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(getContext().getApplicationContext(), "没有可用的定位服务", Toast.LENGTH_LONG).show();
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        }else{
            MyLog.i("aa无法获取定位");
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
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
}
