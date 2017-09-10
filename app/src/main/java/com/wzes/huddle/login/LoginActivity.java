package com.wzes.huddle.login;


import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.homepage.MainActivity;
import com.wzes.huddle.homepage.MyFragment;
import com.wzes.huddle.service.Identity;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.util.AppManager;
import com.wzes.huddle.util.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.wzes.huddle.homepage.MyFragment.currentUser;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.login_image) CircleImageView mUserImage;
    @BindView(R.id.login_sign) Button signInBtn;
    @BindView(R.id.login_username) EditText mUsernameView;
    @BindView(R.id.login_password) EditText mPasswordView;

    private static final String[] INTERNET =
            {Manifest.permission.INTERNET};
    private static final int INTERNET_PERM = 123;
    private UserLoginTask mAuthTask = null;
    private User lastUser = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);

        /*
         * request for database if local not username
         */
        if(Preferences.getUserAccount() != null){
            if (currentUser == null) {
                MyRetrofit.getGsonRetrofit()
                        .getUserByUername(Preferences.getUserAccount())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<User>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                            }

                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull User user) {
                                currentUser = user;
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
            //show defalut image and enter the main
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else if(Preferences.getLastUserAccount() != null) {
            if(NetworkUtils.isConnected(this)){
                MyRetrofit.getGsonRetrofit()
                        .getUserByUername(Preferences.getLastUserAccount())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<User>(){
                            public void onComplete() {
                                Glide.with(LoginActivity.this)
                                        .load(lastUser.getImage())
                                        .into(mUserImage);
                            }

                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            public void onNext(User user) {
                                lastUser = user;
                            }
                        });
            }else{
                Toast.makeText(this, "网络不太好 ^_^", Toast.LENGTH_SHORT).show();
            }
        }

        /*
         * login when click the button
         */
        signInBtn.setOnClickListener(view -> internetTask());
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
            this.username = username;
            this.password = password;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                if (!Identity.Login(username,password)) {
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
                Preferences.saveLastUserAccount(username);
                currentUser = null;
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

    @AfterPermissionGranted(INTERNET_PERM)
    private void internetTask() {
        String perm = Manifest.permission.INTERNET;
        if (hasInternetPermission()) {
            attemptLogin();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.internet_permission),
                    INTERNET_PERM, perm);
        }
    }

    private boolean hasInternetPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.INTERNET);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasInternetPermission() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}
