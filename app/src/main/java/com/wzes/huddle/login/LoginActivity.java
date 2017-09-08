package com.wzes.huddle.login;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.wzes.huddle.R;
import com.wzes.huddle.R2;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.homepage.MainActivity;
import com.wzes.huddle.homepage.MyFragment;
import com.wzes.huddle.service.Identity;
import com.wzes.huddle.service.RetrofitService;
import com.wzes.huddle.util.AppManager;
import com.wzes.huddle.util.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @BindView(R2.id.login_image) CircleImageView mUserImage;
    @BindView(R2.id.login_sign) Button signInBtn;
    @BindView(R2.id.login_username) AutoCompleteTextView mUsernameView;
    @BindView(R2.id.login_password) EditText mPasswordView;


    private UserLoginTask mAuthTask = null;
    private User lastUser = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);

        /*
         * login when click the button
         */
        signInBtn.setOnClickListener(view -> attemptLogin());

        /*
         * request for database if local not username
         */
        if (Preferences.getLastUserAccount() != null) {
            if(NetworkUtils.isConnected(this)){
                new Builder().baseUrl("http://59.110.136.134/")
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build().create(RetrofitService.class).getUserByUername(Preferences.getLastUserAccount())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<User>(){
                            public void onCompleted() {
                                Glide.with(LoginActivity.this)
                                        .load(lastUser.getImage())
                                        .into(mUserImage);
                            }

                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            public void onNext(User user) {
                                lastUser = user;
                            }
                        });
            }else {
                //show defalut image and enter the main
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }

    }

    // check login
    private void attemptLogin() {
        if (mAuthTask == null) {
            mUsernameView.setError(null);
            mPasswordView.setError(null);

            String username = mUsernameView.getText().toString();
            String password = mPasswordView.getText().toString();
            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(password) || !isPasswordValid(password)){
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }
            if (TextUtils.isEmpty(username)) {
                mUsernameView.setError(getString(R.string.error_field_required));
                focusView = mUsernameView;
                cancel = true;
            } else if (!isUsernameValid(username)) {
                mUsernameView.setError(getString(R.string.error_invalid_username));
                focusView = mUsernameView;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
                return;
            }
            /*
             * network is not good !
             */
            if(!NetworkUtils.isConnected(this)){
                Toast.makeText(this, "网络不太好 ^_^", Toast.LENGTH_SHORT).show();
            }else{
                signInBtn.setText("正在登录...");
                mAuthTask = new UserLoginTask(username, password);
                mAuthTask.execute((Void) null);
            }

        }
    }

    private boolean isUsernameValid(String username) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return true;
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String password;
        private final String username;

        UserLoginTask(String username, String password) {
            this.password = username;
            this.username = password;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                if (!Identity.Login(password, username)) {
                    return Boolean.valueOf(false);
                }
                Preferences.saveUserAccount(username);
                return Boolean.valueOf(true);
            } catch (Exception e) {
                return Boolean.valueOf(false);
            }
        }

        protected void onPostExecute(Boolean success) {
            mAuthTask = null;
            if (success.booleanValue()) {
                Preferences.saveUserAccount(username);
                MyFragment.currentUser = null;
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                return;
            }
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
            signInBtn.setText("立即登录");
        }

        protected void onCancelled() {
            mAuthTask = null;
            signInBtn.setText("立即登录");
        }
    }


}
