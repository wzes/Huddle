package com.wzes.huddle.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.activities.myinfo.MyInfoActivity;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.activities.setting.SettingActivity;
import com.wzes.huddle.util.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyFragment extends Fragment implements OnClickListener {

    public static User currentUser;
    public static boolean update = true;
    @BindView(R.id.my_beFollow_text) TextView myBeFollowText;
    @BindView(R.id.my_beFollow_layout) LinearLayout myBeFollowLayout;
    @BindView(R.id.my_follow_text) TextView myFollowText;
    @BindView(R.id.my_follow_layout) LinearLayout myFollowLayout;
    @BindView(R.id.my_sign_text) TextView mySignText;
    @BindView(R.id.my_sign_layout) LinearLayout mySignLayout;
    @BindView(R.id.my_team_text) TextView myTeamText;
    @BindView(R.id.my_team_layout) LinearLayout myTeamLayout;
    @BindView(R.id.my_event_text) TextView myEventText;
    @BindView(R.id.my_event_layout) LinearLayout myEventLayout;
    @BindView(R.id.my_all_follow_text) TextView myAllFollowText;
    @BindView(R.id.my_all_follw_layout) LinearLayout myAllFollwLayout;
    @BindView(R.id.my_message_layout) LinearLayout myMessageLayout;
    @BindView(R.id.my_set_layout) LinearLayout mySetLayout;
    @BindView(R.id.my_feedback_layout) LinearLayout myFeedbackLayout;
    @BindView(R.id.my_image) ImageView myImage;
    @BindView(R.id.my_name_text) TextView myNameText;
    @BindView(R.id.my_major_text) TextView myMajorText;
    @BindView(R.id.my_motto_text) TextView myMottoText;
    @BindView(R.id.my_info_layout) LinearLayout myInfoLayout;
    Unbinder unbinder;

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
        unbinder = ButterKnife.bind(this, view);
        mySignLayout.setOnClickListener(this);
        myTeamLayout.setOnClickListener(this);
        myEventLayout.setOnClickListener(this);
        myAllFollwLayout.setOnClickListener(this);
        myMessageLayout.setOnClickListener(this);
        mySetLayout.setOnClickListener(this);
        myFeedbackLayout.setOnClickListener(this);
        myBeFollowLayout.setOnClickListener(this);
        myFollowLayout.setOnClickListener(this);
        myInfoLayout.setOnClickListener(this);
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
                            Glide.with(getContext())
                                    .load(currentUser.getImage())
                                    .into(myImage);
                            myNameText.setText(currentUser.getName());
                            myMajorText.setText(currentUser.getMajor());
                            myMottoText.setText(currentUser.getMotto());
                            myBeFollowText.setText(String.valueOf(currentUser.getBefollow_account()));
                            myFollowText.setText(String.valueOf(currentUser.getFollow_account()));
                            mySignText.setText(String.valueOf(currentUser.getTeam_sign_account()));
                            myTeamText.setText(String.valueOf(currentUser.getTeam_group_account()));
                            myEventText.setText(String.valueOf(currentUser.getEvent_account()));
                            myAllFollowText.setText(String.valueOf(currentUser.getFollow_event_account()
                                    + currentUser.getFollow_team_account()));
                        }
                    });
            update = true;
        } else {
            Glide.with(getContext())
                    .load(currentUser.getImage())
                    .into(myImage);
            myNameText.setText(currentUser.getName());
            myMajorText.setText(currentUser.getMajor());
            myMottoText.setText(currentUser.getMotto());
            myBeFollowText.setText(String.valueOf(currentUser.getBefollow_account()));
            myFollowText.setText(String.valueOf(currentUser.getFollow_account()));
            mySignText.setText(String.valueOf(currentUser.getTeam_sign_account()));
            myTeamText.setText(String.valueOf(currentUser.getTeam_group_account()));
            myEventText.setText(String.valueOf(currentUser.getEvent_account()));
            myAllFollowText.setText(String.valueOf(currentUser.getFollow_event_account()
                    + currentUser.getFollow_team_account()));
        }
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_info_layout:
                startActivity(new Intent(getContext(), MyInfoActivity.class));
                break;
            case R.id.my_set_layout:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
