package com.wzes.huddle.team_info;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class TeamInfoActivity$$Lambda$1 implements OnClickListener {
    private final TeamInfoActivity arg$1;

    private TeamInfoActivity$$Lambda$1(TeamInfoActivity teamInfoActivity) {
        this.arg$1 = teamInfoActivity;
    }

    private static OnClickListener get$Lambda(TeamInfoActivity teamInfoActivity) {
        return new TeamInfoActivity$$Lambda$1(teamInfoActivity);
    }

    public static OnClickListener lambdaFactory$(TeamInfoActivity teamInfoActivity) {
        return new TeamInfoActivity$$Lambda$1(teamInfoActivity);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$onCreate$0(view);
    }
}
