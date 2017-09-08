package com.wzes.huddle.login;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.lang.invoke.LambdaForm.Hidden;

final /* synthetic */ class LoginActivity$$Lambda$1 implements OnEditorActionListener {
    private final LoginActivity arg$1;

    private LoginActivity$$Lambda$1(LoginActivity loginActivity) {
        this.arg$1 = loginActivity;
    }

    private static OnEditorActionListener get$Lambda(LoginActivity loginActivity) {
        return new LoginActivity$$Lambda$1(loginActivity);
    }

    public static OnEditorActionListener lambdaFactory$(LoginActivity loginActivity) {
        return new LoginActivity$$Lambda$1(loginActivity);
    }

    @Hidden
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return this.arg$1.lambda$onCreate$0(textView, i, keyEvent);
    }
}
