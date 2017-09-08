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
import com.wzes.huddle.C0479R;
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
    @BindView(2131624140)
    public ImageButton backBtn;
    private User currentUser;
    @BindView(2131624148)
    public Button expendBtn;
    @BindView(2131624143)
    public CircleImageView imageView;
    @BindView(2131624147)
    public TextView infoTxt;
    @BindView(2131624146)
    public TextView majorTxt;
    @BindView(2131624142)
    public ImageButton moreBtn;
    @BindView(2131624145)
    public TextView mottoTxt;
    @BindView(2131624144)
    public TextView nameTxt;
    @BindView(2131624149)
    public TabLayout tabLayout;
    @BindView(2131624141)
    public TextView titleTxt;
    @BindView(2131624150)
    public ViewPager viewPager;

    class C04941 implements OnClickListener {
        C04941() {
        }

        public void onClick(View v) {
            UserInfoActivity.this.finish();
        }
    }

    class C04952 implements OnClickListener {
        C04952() {
        }

        public void onClick(View v) {
            BottomSheetDialog dialog = new BottomSheetDialog(UserInfoActivity.this);
            dialog.setContentView(LayoutInflater.from(UserInfoActivity.this).inflate(C0479R.layout.user_info_bottom_sheet_dialog, null));
            dialog.show();
        }
    }

    class C04964 implements OnClickListener {
        C04964() {
        }

        public void onClick(View v) {
            if (UserInfoActivity.expend) {
                UserInfoActivity.this.infoTxt.setText(UserInfoActivity.this.currentUser.getInfo().length() > 20 ? UserInfoActivity.this.currentUser.getInfo().substring(0, 20) + "..." : UserInfoActivity.this.currentUser.getInfo());
                UserInfoActivity.expend = false;
                UserInfoActivity.this.expendBtn.setText("展开");
                return;
            }
            UserInfoActivity.this.infoTxt.setText(UserInfoActivity.this.currentUser.getInfo());
            UserInfoActivity.this.expendBtn.setText("收起");
            UserInfoActivity.expend = true;
        }
    }

    class C04975 implements OnClickListener {
        C04975() {
        }

        public void onClick(View v) {
            Intent intentThree = new Intent(UserInfoActivity.this, ImageViewActivity.class);
            intentThree.putExtra("uri", UserInfoActivity.this.currentUser.getImage());
            UserInfoActivity.this.startActivity(intentThree);
        }
    }

    class C09283 implements Observer<User> {
        C09283() {
        }

        public void onCompleted() {
            if (UserInfoActivity.this.currentUser != null) {
                Glide.with(UserInfoActivity.this).load(UserInfoActivity.this.currentUser.getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(UserInfoActivity.this.imageView);
                UserInfoActivity.this.nameTxt.setText(UserInfoActivity.this.currentUser.getName());
                UserInfoActivity.this.majorTxt.setText(UserInfoActivity.this.currentUser.getMajor());
                UserInfoActivity.this.mottoTxt.setText(UserInfoActivity.this.currentUser.getMotto() == null ? "这家伙很懒，还没有写呢" : UserInfoActivity.this.currentUser.getMotto());
                UserInfoActivity.this.titleTxt.setText(UserInfoActivity.this.currentUser.getName());
                UserInfoActivity.this.infoTxt.setText((UserInfoActivity.this.currentUser.getInfo() == null ? "暂无简介" : UserInfoActivity.this.currentUser.getInfo()).length() > 20 ? UserInfoActivity.this.currentUser.getInfo().substring(0, 20) + "..." : UserInfoActivity.this.currentUser.getInfo());
            }
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(User user) {
            UserInfoActivity.this.currentUser = user;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0479R.layout.activity_user_info);
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
