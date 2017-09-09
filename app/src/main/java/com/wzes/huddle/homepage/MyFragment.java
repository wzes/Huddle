package com.wzes.huddle.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.GsonBuilder;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.myinfo.MyInfoActivity;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.service.RetrofitService;
import com.wzes.huddle.setting.SettingActivity;
import com.wzes.huddle.util.AppManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.observable.ObservableGroupBy;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyFragment extends Fragment implements OnClickListener {
    public static User currentUser;
    public static boolean update = true;
    private View eventItem;
    private View feedbackItem;
    private View followItem;
    private ImageView imageView;
    private TextView majorTxt;
    private TextView mottoTxt;
    private View msgItem;
    private View myInfo;
    private TextView nameTxt;
    private View setItem;
    private View signItem;
    private View teamItem;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(getActivity());
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        this.myInfo = view.findViewById(R.id.my_info);
        this.myInfo.setOnClickListener(this);
        this.imageView = (ImageView) view.findViewById(R.id.my_image);
        this.nameTxt = (TextView) view.findViewById(R.id.my_name);
        this.majorTxt = (TextView) view.findViewById(R.id.my_major);
        this.mottoTxt = (TextView) view.findViewById(R.id.my_motto);
        this.signItem = view.findViewById(R.id.my_sign_item);
        this.teamItem = view.findViewById(R.id.my_team_item);
        this.eventItem = view.findViewById(R.id.my_event_item);
        this.followItem = view.findViewById(R.id.my_follow_item);
        this.msgItem = view.findViewById(R.id.my_msg_item);
        this.setItem = view.findViewById(R.id.my_set_item);
        this.feedbackItem = view.findViewById(R.id.my_feedback_item);
        this.signItem.setOnClickListener(this);
        this.teamItem.setOnClickListener(this);
        this.eventItem.setOnClickListener(this);
        this.followItem.setOnClickListener(this);
        this.msgItem.setOnClickListener(this);
        this.setItem.setOnClickListener(this);
        this.feedbackItem.setOnClickListener(this);
        if (currentUser == null || !update) {
            MyRetrofit.getGsonRetrofit()
                    .getUserByUername(Preferences.getUserAccount())
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
                            Glide.with(MyFragment.this.getContext()).load(MyFragment.currentUser.getImage()).into(MyFragment.this.imageView);
                            MyFragment.this.nameTxt.setText(MyFragment.currentUser.getName());
                            MyFragment.this.majorTxt.setText(MyFragment.currentUser.getMajor());
                            MyFragment.this.mottoTxt.setText(MyFragment.currentUser.getMotto());
                        }
                    });
            update = true;
        } else {
            Glide.with(getContext()).load(currentUser.getImage()).into(this.imageView);
            this.nameTxt.setText(currentUser.getName());
            this.majorTxt.setText(currentUser.getMajor());
            this.mottoTxt.setText(currentUser.getMotto());
        }
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_info /*2131624200*/:
                startActivity(new Intent(getContext(), MyInfoActivity.class));
                return;
            case R.id.my_set_item /*2131624214*/:
                startActivity(new Intent(getContext(), SettingActivity.class));
                return;
            default:
                return;
        }
    }
}
