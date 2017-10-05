package com.wzes.huddle.activities.manage;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.wzes.huddle.R;
import com.wzes.huddle.activities.follow.FollowActivity;
import com.wzes.huddle.activities.userdetail.UserInfoActivity;
import com.wzes.huddle.adapter.ManageAdapter;
import com.wzes.huddle.bean.UserTeam;
import com.wzes.huddle.service.MyRetrofit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Unbinder unbinder;
    @BindView(R.id.manage_recyclerView)
    RecyclerView manageRecyclerView;
    private List<UserTeam> list;
    private String mParam1;
    private String mParam2;
    private ManageAdapter manageAdapter;


    public static SignFragment newInstance(String param1, String param2) {
        SignFragment fragment = new SignFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        new Thread(this::initData).start();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void initData() {
        MyRetrofit.getGsonRetrofit().getSignUserList(mParam2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserTeam>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<UserTeam> events) {
                        list = events;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!(list.size() > 0 && list.get(0) != null)) {
                            list = new ArrayList<>();
                        }
                        manageAdapter = new ManageAdapter(R.layout.manage_item, list);

                        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(manageAdapter);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
                        itemTouchHelper.attachToRecyclerView(manageRecyclerView);


                        // 开启滑动删除
                        manageAdapter.enableSwipeItem();
                        manageAdapter.setOnItemSwipeListener(onItemSwipeListener);

                        manageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        manageRecyclerView.setAdapter(manageAdapter);

                        manageAdapter.setOnItemClickListener((adapter, view, position) -> {
                            Intent uIntent = new Intent(getContext(), UserInfoActivity.class);
                            uIntent.putExtra("user_id", list.get(position).getUser_id());
                            startActivity(uIntent);
                        });

                    }
                });
    }

    OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            Toast.makeText(getContext(), "左滑通过", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}