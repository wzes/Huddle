package com.wzes.huddle.team_info;

import android.view.View;
import android.view.View.OnClickListener;
import com.wzes.huddle.team_info.TeamInfoActivity.C09261;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TeamInfoActivity$1$$Lambda$2 implements OnClickListener {
    private final C09261 arg$1;

    private TeamInfoActivity$1$$Lambda$2(C09261 c09261) {
        this.arg$1 = c09261;
    }

    private static OnClickListener get$Lambda(C09261 c09261) {
        return new TeamInfoActivity$1$$Lambda$2(c09261);
    }

    public static OnClickListener lambdaFactory$(C09261 c09261) {
        return new TeamInfoActivity$1$$Lambda$2(c09261);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$onCompleted$1(view);
    }
}
