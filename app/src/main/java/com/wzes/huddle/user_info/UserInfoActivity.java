package com.wzes.huddle.user_info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.GsonBuilder;
import com.wzes.huddle.R;
import com.wzes.huddle.adapter.UserAdapter;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.imageloader.ImageViewActivity;
import com.wzes.huddle.service.RetrofitService;
import com.wzes.huddle.util.AppManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserInfoActivity extends AppCompatActivity {
    private static boolean expend = false;
    private static String user_id;
    @BindView(R.id.user_info_back)
    public ImageButton backBtn;
    private User currentUser;
    @BindView(R.id.user_info_expend)
    public Button expendBtn;
    @BindView(R.id.user_info_image)
    public CircleImageView imageView;
    @BindView(R.id.user_info_info)
    public TextView infoTxt;
    @BindView(R.id.user_info_major)
    public TextView majorTxt;
    @BindView(R.id.user_info_more)
    public ImageButton moreBtn;
    @BindView(R.id.user_info_motto)
    public TextView mottoTxt;
    @BindView(R.id.user_info_name)
    public TextView nameTxt;
    @BindView(R.id.user_info_tabs)
    public TabLayout tabLayout;
    @BindView(R.id.user_info_title)
    public TextView titleTxt;
    @BindView(R.id.user_pager)
    public ViewPager viewPager;

    class C04941 implements OnClickListener {
        C04941() {
        }

        public void onClick(View v) {
            finish();
        }
    }

    class C04952 implements OnClickListener {
        C04952() {
        }

        public void onClick(View v) {
            BottomSheetDialog dialog = new BottomSheetDialog(UserInfoActivity.this);
            dialog.setContentView(LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.user_info_bottom_sheet_dialog, null));
            dialog.show();
        }
    }

    class C04964 implements OnClickListener {
        C04964() {
        }

        public void onClick(View v) {
            if (UserInfoActivity.expend) {
                infoTxt.setText(currentUser.getInfo().length() > 20 ? currentUser.getInfo().substring(0, 20) + "..." : currentUser.getInfo());
                UserInfoActivity.expend = false;
                expendBtn.setText("展开");
                return;
            }
            infoTxt.setText(currentUser.getInfo());
            expendBtn.setText("收起");
            UserInfoActivity.expend = true;
        }
    }

    class C04975 implements OnClickListener {
        C04975() {
        }

        public void onClick(View v) {
            Intent intentThree = new Intent(UserInfoActivity.this, ImageViewActivity.class);
            intentThree.putExtra("uri", currentUser.getImage());
            startActivity(intentThree);
        }
    }

    class C09283 implements Observer<User> {
        C09283() {
        }

        public void onCompleted() {
            if (currentUser != null) {
                Glide.with(UserInfoActivity.this).load(currentUser.getImage()).into(imageView);
                nameTxt.setText(currentUser.getName());
                majorTxt.setText(currentUser.getMajor());
                mottoTxt.setText(currentUser.getMotto() == null ? "这家伙很懒，还没有写呢" : currentUser.getMotto());
                titleTxt.setText(currentUser.getName());
                infoTxt.setText((currentUser.getInfo() == null ? "暂无简介" : currentUser.getInfo()).length() > 20 ? currentUser.getInfo().substring(0, 20) + "..." : currentUser.getInfo());
            }
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(User user) {
            currentUser = user;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_user_info);
        ButterKnife.bind((Activity) this);
        AppManager.getAppManager().addActivity(this);
        user_id = getIntent().getStringExtra("user_id");
        this.viewPager.setAdapter(new UserAdapter(getSupportFragmentManager(), this, UserEventFragment.newInstance("UserEventFragment", "1"), UserTeamFragment.newInstance("UserTeamFragment", "2")));
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.backBtn.setOnClickListener(new C04941());
        this.moreBtn.setOnClickListener(new C04952());
        ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getUserByUername(user_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09283());
        this.expendBtn.setOnClickListener(new C04964());
        this.imageView.setOnClickListener(new C04975());
    }
}
