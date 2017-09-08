package com.wzes.huddle.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class EventAdapter$$Lambda$1 implements OnClickListener {
    private final EventAdapter arg$1;
    private final String arg$2;

    private EventAdapter$$Lambda$1(EventAdapter eventAdapter, String str) {
        this.arg$1 = eventAdapter;
        this.arg$2 = str;
    }

    private static OnClickListener get$Lambda(EventAdapter eventAdapter, String str) {
        return new EventAdapter$$Lambda$1(eventAdapter, str);
    }

    public static OnClickListener lambdaFactory$(EventAdapter eventAdapter, String str) {
        return new EventAdapter$$Lambda$1(eventAdapter, str);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$onBindViewHolder$0(this.arg$2, view);
    }
}
