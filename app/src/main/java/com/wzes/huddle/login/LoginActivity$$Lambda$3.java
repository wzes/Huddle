package com.wzes.huddle.login;

import android.view.View;
import android.view.View.OnClickListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class LoginActivity$$Lambda$3 implements OnClickListener {
    private final LoginActivity arg$1;

    private LoginActivity$$Lambda$3(LoginActivity loginActivity) {
        this.arg$1 = loginActivity;
    }

    private static OnClickListener get$Lambda(LoginActivity loginActivity) {
        return new LoginActivity$$Lambda$3(loginActivity);
    }

    public static OnClickListener lambdaFactory$(LoginActivity loginActivity) {
        return new LoginActivity$$Lambda$3(loginActivity);
    }

    @Hidden
    public void onClick(View view) {
        this.arg$1.lambda$mayRequestContacts$2(view);
    }
}
