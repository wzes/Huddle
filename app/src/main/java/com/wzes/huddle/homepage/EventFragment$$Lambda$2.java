package com.wzes.huddle.homepage;

import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class EventFragment$$Lambda$2 implements Runnable {
    private final EventFragment arg$1;

    private EventFragment$$Lambda$2(EventFragment eventFragment) {
        this.arg$1 = eventFragment;
    }

    private static Runnable get$Lambda(EventFragment eventFragment) {
        return new EventFragment$$Lambda$2(eventFragment);
    }

    public static Runnable lambdaFactory$(EventFragment eventFragment) {
        return new EventFragment$$Lambda$2(eventFragment);
    }

    @Hidden
    public void run() {
        this.arg$1.lambda$onCreateView$1();
    }
}
