package com.wzes.huddle.homepage;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class EventFragment$$Lambda$1 implements OnRefreshListener {
    private final EventFragment arg$1;

    private EventFragment$$Lambda$1(EventFragment eventFragment) {
        this.arg$1 = eventFragment;
    }

    private static OnRefreshListener get$Lambda(EventFragment eventFragment) {
        return new EventFragment$$Lambda$1(eventFragment);
    }

    public static OnRefreshListener lambdaFactory$(EventFragment eventFragment) {
        return new EventFragment$$Lambda$1(eventFragment);
    }

    @Hidden
    public void onRefresh() {
        this.arg$1.lambda$onCreateView$0();
    }
}
