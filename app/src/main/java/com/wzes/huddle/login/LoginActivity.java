package com.wzes.huddle.login;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.ContactsContract.Profile;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.GsonBuilder;
import com.wzes.huddle.C0479R;
import com.wzes.huddle.app.DemoCache;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.bean.User;
import com.wzes.huddle.homepage.MainActivity;
import com.wzes.huddle.homepage.MyFragment;
import com.wzes.huddle.service.Identity;
import com.wzes.huddle.service.RetrofitService;
import com.wzes.huddle.util.AppManager;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private static final String[] DUMMY_CREDENTIALS = new String[]{"foo@example.com:hello", "bar@example.com:world"};
    private static final int REQUEST_READ_CONTACTS = 0;
    private CircleImageView circleImageView;
    private User lastUser = null;
    private UserLoginTask mAuthTask = null;
    private Button mEmailSignInButton;
    private AutoCompleteTextView mEmailView;
    private View mLoginFormView;
    private EditText mPasswordView;
    private View mProgressView;

    private interface ProfileQuery {
        public static final int ADDRESS = 0;
        public static final int IS_PRIMARY = 1;
        public static final String[] PROJECTION = new String[]{"data1", "is_primary"};
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mPassword;
        private final String mUsername;

        UserLoginTask(String username, String password) {
            this.mUsername = username;
            this.mPassword = password;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                if (!Identity.Login(this.mUsername, this.mPassword)) {
                    return Boolean.valueOf(false);
                }
                Preferences.saveUserAccount(this.mUsername);
                return Boolean.valueOf(true);
            } catch (Exception e) {
                return Boolean.valueOf(false);
            }
        }

        protected void onPostExecute(Boolean success) {
            LoginActivity.this.mAuthTask = null;
            if (success.booleanValue()) {
                Preferences.saveUserAccount(this.mUsername);
                MyFragment.currentUser = null;
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                LoginActivity.this.finish();
                return;
            }
            LoginActivity.this.mPasswordView.setError(LoginActivity.this.getString(C0479R.string.error_incorrect_password));
            LoginActivity.this.mPasswordView.requestFocus();
            LoginActivity.this.mEmailSignInButton.setText("立即登录");
        }

        protected void onCancelled() {
            LoginActivity.this.mAuthTask = null;
            LoginActivity.this.mEmailSignInButton.setText("立即登录");
        }
    }

    class C09101 implements Observer<User> {
        C09101() {
        }

        public void onCompleted() {
            Glide.with(LoginActivity.this).load(LoginActivity.this.lastUser.getImage()).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(LoginActivity.this.circleImageView);
        }

        public void onError(Throwable e) {
            e.printStackTrace();
        }

        public void onNext(User user) {
            LoginActivity.this.lastUser = user;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0479R.layout.activity_login);
        AppManager.getAppManager().addActivity(this);
        DemoCache.setContext(this);
        if (Preferences.getUserAccount() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        setSupportActionBar((Toolbar) findViewById(2131624084));
        this.mEmailView = (AutoCompleteTextView) findViewById(C0479R.id.login_username);
        this.circleImageView = (CircleImageView) findViewById(C0479R.id.login_image);
        this.mPasswordView = (EditText) findViewById(C0479R.id.login_password);
        this.mPasswordView.setOnEditorActionListener(LoginActivity$$Lambda$1.lambdaFactory$(this));
        this.mEmailSignInButton = (Button) findViewById(C0479R.id.email_sign_in_button);
        this.mEmailSignInButton.setOnClickListener(LoginActivity$$Lambda$2.lambdaFactory$(this));
        this.mLoginFormView = findViewById(C0479R.id.login_form);
        this.mProgressView = findViewById(C0479R.id.login_progress);
        if (Preferences.getLastUserAccount() != null) {
            ((RetrofitService) new Builder().baseUrl("http://59.110.136.134/").addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(RetrofitService.class)).getUserByUername(Preferences.getLastUserAccount()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new C09101());
        }
    }

    private /* synthetic */ boolean lambda$onCreate$0(TextView textView, int id, KeyEvent keyEvent) {
        if (id != C0479R.id.login && id != 0) {
            return false;
        }
        attemptLogin();
        return true;
    }

    private /* synthetic */ void lambda$onCreate$1(View view) {
        attemptLogin();
    }

    private void populateAutoComplete() {
        if (mayRequestContacts()) {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    private boolean mayRequestContacts() {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            return true;
        }
        if (shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS")) {
            Snackbar.make(this.mEmailView, (int) C0479R.string.permission_rationale, -2).setAction(17039370, LoginActivity$$Lambda$3.lambdaFactory$(this));
        } else {
            requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
        }
        return false;
    }

    private /* synthetic */ void lambda$mayRequestContacts$2(View v) {
        requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && grantResults.length == 1 && grantResults[0] == 0) {
            populateAutoComplete();
        }
    }

    private void attemptLogin() {
        if (this.mAuthTask == null) {
            this.mEmailView.setError(null);
            this.mPasswordView.setError(null);
            String email = this.mEmailView.getText().toString();
            String password = this.mPasswordView.getText().toString();
            boolean cancel = false;
            View focusView = null;
            if (!(TextUtils.isEmpty(password) || isPasswordValid(password))) {
                this.mPasswordView.setError(getString(C0479R.string.error_invalid_password));
                focusView = this.mPasswordView;
                cancel = true;
            }
            if (TextUtils.isEmpty(email)) {
                this.mEmailView.setError(getString(C0479R.string.error_field_required));
                focusView = this.mEmailView;
                cancel = true;
            } else if (!isEmailValid(email)) {
                this.mEmailView.setError(getString(C0479R.string.error_invalid_email));
                focusView = this.mEmailView;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
                return;
            }
            this.mEmailSignInButton.setText("正在登录...");
            this.mAuthTask = new UserLoginTask(email, password);
            this.mAuthTask.execute(new Void[]{(Void) null});
        }
    }

    private boolean isEmailValid(String email) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return true;
    }

    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, Uri.withAppendedPath(Profile.CONTENT_URI, "data"), ProfileQuery.PROJECTION, "mimetype = ?", new String[]{"vnd.android.cursor.item/email_v2"}, "is_primary DESC");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<String> emails = new ArrayList();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(0));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        this.mEmailView.setAdapter(new ArrayAdapter(this, 17367050, emailAddressCollection));
    }
}
