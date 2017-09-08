package com.wzes.huddle.event_info;

import android.view.View;
import android.view.View.OnClickListener;
import com.wzes.huddle.event_info.EventInfoActivity.C08982;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class EventInfoActivity$2$$Lambda$1 implements OnClickListener {
    private final C08982 arg$1;

    private EventInfoActivity$2$$Lambda$1(C08982 c08982) {
        this.arg$1 = c08982;
    }

    private static OnClickListener get$Lambda(C08982 c08982) {
        return new EventInfoActivity$2$$Lambda$1(c08982);
    }

    public static OnClickListener lambdaFactory$(C08982 c08982) {
        return new EventInfoActivity$2$$Lambda$1(c08982);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$onCompleted$0(view);
    }
}
