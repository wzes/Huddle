package com.wzes.huddle.login;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class LoginActivity$$Lambda$2 implements OnClickListener {
    private final LoginActivity arg$1;

    private LoginActivity$$Lambda$2(LoginActivity loginActivity) {
        this.arg$1 = loginActivity;
    }

    private static OnClickListener get$Lambda(LoginActivity loginActivity) {
        return new LoginActivity$$Lambda$2(loginActivity);
    }

    public static OnClickListener lambdaFactory$(LoginActivity loginActivity) {
        return new LoginActivity$$Lambda$2(loginActivity);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$onCreate$1(view);
    }
}
