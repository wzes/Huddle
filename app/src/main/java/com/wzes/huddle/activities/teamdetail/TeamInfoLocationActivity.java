package com.wzes.huddle.activities.teamdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus.Builder;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.wzes.huddle.R;
import com.wzes.huddle.util.AppManager;

public class TeamInfoLocationActivity extends AppCompatActivity {
    @BindView(R.id.team_info_location_back) ImageButton backBtn;
    private BaiduMap baiduMap;
    private String latitude;
    private String longtitude;
    private String title;
    @BindView(R.id.team_info_location_title) TextView titleTxt;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        Intent intent = getIntent();
        this.title = intent.getStringExtra("title");
        this.latitude = intent.getStringExtra("latitude");
        this.longtitude = intent.getStringExtra("longitude");
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_team_info_location);
        ButterKnife.bind(this);
        this.titleTxt.setText(this.title);
        this.backBtn.setOnClickListener(view -> finish());
        this.baiduMap = ((MapView) findViewById(R.id.team_info_location_map)).getMap();
        this.baiduMap.setMyLocationEnabled(true);
        LatLng ll = new LatLng(Double.parseDouble(this.latitude), Double.parseDouble(this.longtitude));
        Builder builder = new Builder();
        builder.target(ll).zoom(17.0f);
        this.baiduMap.addOverlay(new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        this.baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
}
