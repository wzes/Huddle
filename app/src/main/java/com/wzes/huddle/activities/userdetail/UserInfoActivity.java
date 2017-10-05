package com.wzes.huddle.activities.userdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.activities.teamdetail.TeamInfoActivity;
import com.wzes.huddle.adapter.UserAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.imageloader.ImageViewActivity;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.util.AppManager;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class UserInfoActivity extends AppCompatActivity {
    private static boolean expend = false;
    private static String user_id;
    @BindView(R.id.user_info_back) ImageButton backBtn;
    private User currentUser;
    @BindView(R.id.user_info_expend) Button expendBtn;
    @BindView(R.id.user_info_image) CircleImageView imageView;
    @BindView(R.id.user_info_info) TextView infoTxt;
    @BindView(R.id.user_info_major) TextView majorTxt;
    @BindView(R.id.user_info_more) ImageButton moreBtn;
    @BindView(R.id.user_info_motto) TextView mottoTxt;
    @BindView(R.id.user_info_name) TextView nameTxt;
    @BindView(R.id.user_info_tabs) TabLayout tabLayout;
    @BindView(R.id.user_info_title) TextView titleTxt;
    @BindView(R.id.user_pager) ViewPager viewPager;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        user_id = getIntent().getStringExtra("user_id");
        viewPager.setAdapter(new UserAdapter(getSupportFragmentManager(), this,
                UserEventFragment.newInstance("UserEventFragment", user_id),
                UserTeamFragment.newInstance("UserTeamFragment", user_id)));
        tabLayout.setupWithViewPager(this.viewPager);
        backBtn.setOnClickListener(view -> finish());
        moreBtn.setOnClickListener(view -> {
            BottomSheetDialog dialog = new BottomSheetDialog(UserInfoActivity.this);
            View bottom = LayoutInflater.from(UserInfoActivity.this).inflate(R.layout.user_info_bottom_sheet_dialog, null);
            dialog.setContentView(bottom);
            dialog.show();

            View fView = bottom.findViewById(R.id.text_view_favorite);
            fView.setOnClickListener(view1 -> MyRetrofit.getNormalRetrofit().addUserFollow(Preferences.getUserAccount(),
                    user_id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull ResponseBody team) {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            Toast.makeText(UserInfoActivity.this, "关注成功！", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }));


        });
        MyRetrofit.getGsonRetrofit().getUserByUername(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull User user) {
                        currentUser = user;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (currentUser != null) {
                            Glide.with(UserInfoActivity.this).load(currentUser.getImage()).into(imageView);
                            nameTxt.setText(currentUser.getName());
                            majorTxt.setText(currentUser.getMajor());
                            mottoTxt.setText(currentUser.getMotto() == null ? "这家伙很懒，还没有写呢" : currentUser.getMotto());
                            titleTxt.setText(currentUser.getName());
                            infoTxt.setText((currentUser.getInfo() == null ? "暂无简介" : currentUser.getInfo()).length() > 20 ? currentUser.getInfo().substring(0, 20) + "..." : currentUser.getInfo());
                        }
                    }
                });
        expendBtn.setOnClickListener(view ->{
            if (UserInfoActivity.expend) {
                infoTxt.setText(currentUser.getInfo().length() > 20 ? currentUser.getInfo().substring(0, 20) + "..." : currentUser.getInfo());
                UserInfoActivity.expend = false;
                expendBtn.setText("展开");
                return;
            }
            infoTxt.setText(currentUser.getInfo());
            expendBtn.setText("收起");
            UserInfoActivity.expend = true;
        });
        imageView.setOnClickListener(view -> {
            Intent intentThree = new Intent(UserInfoActivity.this, ImageViewActivity.class);
            intentThree.putExtra("uri", currentUser.getImage());
            startActivity(intentThree);
        });
    }
}
