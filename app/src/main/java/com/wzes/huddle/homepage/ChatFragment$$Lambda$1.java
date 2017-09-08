package com.wzes.huddle.homepage;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class ChatFragment$$Lambda$1 implements OnRefreshListener {
    private final ChatFragment arg$1;

    private ChatFragment$$Lambda$1(ChatFragment chatFragment) {
        this.arg$1 = chatFragment;
    }

    private static OnRefreshListener get$Lambda(ChatFragment chatFragment) {
        return new ChatFragment$$Lambda$1(chatFragment);
    }

    public static OnRefreshListener lambdaFactory$(ChatFragment chatFragment) {
        return new ChatFragment$$Lambda$1(chatFragment);
    }

    @Hidden
    public void onRefresh() {
        this.arg$1.lambda$onCreateView$0();
    }
}
