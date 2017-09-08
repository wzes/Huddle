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
import com.wzes.huddle.C0479R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.myinfo.MyInfoActivity;
import com.wzes.huddle.service.RetrofitService;
import com.wzes.huddle.setting.SettingActivity;
import com.wzes.huddle.util.AppManager;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyFragment extends Fragment implements OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static User currentUser;
    public static boolean update = true;
    private View eventItem;
    private View feedbackItem;
    private View followItem;
    private ImageView imageView;
    private String mParam1;
    private String mParam2;
    private TextView majorTxt;
    private TextView mottoTxt;
    private View msgItem;
    private View myInfo;
    private TextView nameTxt;
    private View setItem;
    private View signItem;
    private View teamItem;

    class C09081 implements Observer<User> {
        C09081() {
        }

        public void onCompleted() {
            Glide.with(MyFragment.this.getContext()).load(MyFragment.currentUser.getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(MyFragment.this.imageView);
            MyFragment.this.nameTxt.setText(MyFragment.currentUser.getName());
            MyFragment.this.majorTxt.setText(MyFragment.currentUser.getMajor());
            MyFragment.this.mottoTxt.setText(MyFragment.currentUser.getMotto());
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(User user) {
            MyFragment.currentUser = user;
        }
    }

    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(getActivity());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(C0479R.layout.fragment_my, container, false);
        this.myInfo = view.findViewById(C0479R.id.my_info);
        this.myInfo.setOnClickListener(this);
        this.imageView = (ImageView) view.findViewById(C0479R.id.my_image);
        this.nameTxt = (TextView) view.findViewById(C0479R.id.my_name);
        this.majorTxt = (TextView) view.findViewById(C0479R.id.my_major);
        this.mottoTxt = (TextView) view.findViewById(C0479R.id.my_motto);
        this.signItem = view.findViewById(C0479R.id.my_sign_item);
        this.teamItem = view.findViewById(C0479R.id.my_team_item);
        this.eventItem = view.findViewById(C0479R.id.my_event_item);
        this.followItem = view.findViewById(C0479R.id.my_follow_item);
        this.msgItem = view.findViewById(C0479R.id.my_msg_item);
        this.setItem = view.findViewById(C0479R.id.my_set_item);
        this.feedbackItem = view.findViewById(C0479R.id.my_feedback_item);
        this.signItem.setOnClickListener(this);
        this.teamItem.setOnClickListener(this);
        this.eventItem.setOnClickListener(this);
        this.followItem.setOnClickListener(this);
        this.msgItem.setOnClickListener(this);
        this.setItem.setOnClickListener(this);
        this.feedbackItem.setOnClickListener(this);
        if (currentUser == null || !update) {
            ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getUserByUername(Preferences.getUserAccount()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09081());
            update = true;
        } else {
            Glide.with(getContext()).load(currentUser.getImage()).centerCrop().into(this.imageView);
            this.nameTxt.setText(currentUser.getName());
            this.majorTxt.setText(currentUser.getMajor());
            this.mottoTxt.setText(currentUser.getMotto());
        }
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0479R.id.my_info /*2131624200*/:
                startActivity(new Intent(getContext(), MyInfoActivity.class));
                return;
            case C0479R.id.my_set_item /*2131624214*/:
                startActivity(new Intent(getContext(), SettingActivity.class));
                return;
            default:
                return;
        }
    }
}
