package com.wzes.huddle.activities.follow;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.ImageButton;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.wzes.huddle.R;
import com.wzes.huddle.activities.userdetail.UserInfoActivity;
import com.wzes.huddle.adapter.BeFollowAdapter;
import com.wzes.huddle.adapter.FollowAdapter;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.Follow;
import com.wzes.huddle.service.MyRetrofit;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BeFollowActivity extends AppCompatActivity {

    @BindView(R.id.be_follow_back)
    ImageButton beFollowBack;
    @BindView(R.id.be_follow_recycler)
    RecyclerView beFollowRecycler;
    private BeFollowAdapter befollowAdapter;
    private List<Follow> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_follow);
        ButterKnife.bind(this);
        MyRetrofit.getGsonRetrofit().getBeFollowUsers(Preferences.getUserAccount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Follow>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Follow> users) {
                        list = users;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if(!(list.size() > 0 && list.get(0) != null)){
                            list = new ArrayList<>();
                        }
                        befollowAdapter = new BeFollowAdapter(R.layout.follow_item, list);

                        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(befollowAdapter);
                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
                        itemTouchHelper.attachToRecyclerView(beFollowRecycler);


                        // 开启滑动删除
                        befollowAdapter.enableSwipeItem();
                        befollowAdapter.setOnItemSwipeListener(onItemSwipeListener);

                        beFollowRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        beFollowRecycler.setAdapter(befollowAdapter);
                        befollowAdapter.setOnItemClickListener((adapter, view, position) -> {
                            Intent uIntent = new Intent(BeFollowActivity.this, UserInfoActivity.class);
                            uIntent.putExtra("user_id", list.get(position).getUser_id());
                            startActivity(uIntent);
                        });
                    }
                });
    }

    @OnClick(R.id.be_follow_back)
    public void onViewClicked() {
        finish();
    }

    OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {}
        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {}
        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {}

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

        }
    };

}
