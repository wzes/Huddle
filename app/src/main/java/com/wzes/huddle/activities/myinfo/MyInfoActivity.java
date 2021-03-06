package com.wzes.huddle.activities.myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;
import com.wzes.huddle.app.Preferences;
import com.wzes.huddle.homepage.MyFragment;
import com.wzes.huddle.service.MyRetrofit;
import com.wzes.huddle.util.GalleryGlideImageLoader;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import io.netty.handler.codec.http.HttpHeaders.Values;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyInfoActivity extends AppCompatActivity implements OnClickListener {
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESIZE_REQUEST_CODE = 2;
    private static String TAG = "TTTT";
    private ImageButton backBtn;
    private TextView birthTxt;
    private TextView gradeTxt;
    private String imageUri;
    private ImageView imageView;
    private TextView majorTxt;
    private TextView moreTxt;
    private TextView mottoTxt;
    private TextView nameTxt;
    private TextView phoneTxt;
    private TextView sexTxt;
    private TextView usernameTxt;

    private IHandlerCallBack iHandlerCallBack = new IHandlerCallBack() {

        public void onStart() {
            Log.i(MyInfoActivity.TAG, "onStart: 开启");
        }

        public void onSuccess(List<String> photoList) {
            Iterator it = photoList.iterator();
            if (it.hasNext()) {
                String s = (String) it.next();
                uploadImage(s);
                Glide.with(MyInfoActivity.this).load(s).into(imageView);
            }
        }

        private void uploadImage(String path) {
            File file = new File(path);
            Part body = Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
            RequestBody user_id = RequestBody.create(MediaType.parse(Values.MULTIPART_FORM_DATA), Preferences.getUserAccount());
            MyRetrofit.getNormalRetrofit().uploaduserImage(user_id, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull ResponseBody responseBody) {

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

        public void onCancel() {
            Log.i(MyInfoActivity.TAG, "onCancel: 取消");
        }

        public void onFinish() {
            Log.i(MyInfoActivity.TAG, "onFinish: 结束");
        }

        public void onError() {
            Log.i(MyInfoActivity.TAG, "onError: 出错");
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        this.backBtn = findViewById(R.id.my_info_back);
        this.backBtn.setOnClickListener(this);
        this.usernameTxt = findViewById(R.id.my_info_username);
        this.nameTxt = findViewById(R.id.my_info_name);
        this.sexTxt = findViewById(R.id.my_info_sex);
        this.gradeTxt = findViewById(R.id.my_info_grade);
        this.majorTxt = findViewById(R.id.my_info_major);
        this.birthTxt = findViewById(R.id.my_info_birth);
        this.phoneTxt = findViewById(R.id.my_info_phone);
        this.mottoTxt = findViewById(R.id.my_info_motto);
        this.moreTxt = findViewById(R.id.my_info_more);
        this.imageView = findViewById(R.id.my_info_img);
        this.usernameTxt.setOnClickListener(this);
        this.nameTxt.setOnClickListener(this);
        this.sexTxt.setOnClickListener(this);
        this.gradeTxt.setOnClickListener(this);
        this.majorTxt.setOnClickListener(this);
        this.birthTxt.setOnClickListener(this);
        this.phoneTxt.setOnClickListener(this);
        this.mottoTxt.setOnClickListener(this);
        this.moreTxt.setOnClickListener(this);
        this.imageView.setOnClickListener(this);
        Glide.with(this).load(MyFragment.currentUser.getImage()).into(this.imageView);
        this.usernameTxt.setText(MyFragment.currentUser.getUser_id());
        this.nameTxt.setText(MyFragment.currentUser.getName());
        this.sexTxt.setText(MyFragment.currentUser.getSex());
        this.gradeTxt.setText(MyFragment.currentUser.getGrade());
        this.majorTxt.setText(MyFragment.currentUser.getMajor());
        this.birthTxt.setText(MyFragment.currentUser.getDob());
        this.phoneTxt.setText(MyFragment.currentUser.getTelephone());
        this.mottoTxt.setText(MyFragment.currentUser.getMotto());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_info_back:
                finish();
                return;
            case R.id.my_info_img:
                requestPermission();
                return;
            case R.id.my_info_name:
                Intent name = new Intent(this, MyInfoSettingActivity.class);
                name.putExtra("title", "姓名");
                name.putExtra("content", this.nameTxt.getText());
                name.putExtra("note", "请不要超过40个字符");
                startActivity(name);
                return;
            case R.id.my_info_sex:
                Intent sex = new Intent(this, MyInfoSettingActivity.class);
                sex.putExtra("title", "姓别");
                sex.putExtra("content", this.sexTxt.getText());
                sex.putExtra("note", "请输入性别");
                startActivity(sex);
                return;
            case R.id.my_info_grade:
                Intent grade = new Intent(this, MyInfoSettingActivity.class);
                grade.putExtra("title", "年级");
                grade.putExtra("content", this.phoneTxt.getText());
                grade.putExtra("note", "");
                startActivity(grade);
                return;
            case R.id.my_info_major:
                Intent major = new Intent(this, MyInfoSettingActivity.class);
                major.putExtra("title", "专业");
                major.putExtra("content", MyFragment.currentUser.getMajor());
                major.putExtra("type", "major");
                major.putExtra("note", "请输入专业");
                startActivity(major);
                return;
            case R.id.my_info_birth:
                Intent birth = new Intent(this, MyInfoSettingActivity.class);
                birth.putExtra("title", "生日");
                birth.putExtra("content", this.birthTxt.getText());
                birth.putExtra("note", "请选择日期");
                startActivity(birth);
                return;
            case R.id.my_info_phone:
                Intent phone = new Intent(this, MyInfoSettingActivity.class);
                phone.putExtra("title", "电话");
                phone.putExtra("content", this.phoneTxt.getText());
                phone.putExtra("note", "请输入电话号码");
                startActivity(phone);
                return;
            case R.id.my_info_motto:
                Intent motto = new Intent(this, MyInfoSettingActivity.class);
                motto.putExtra("title", "签名");
                motto.putExtra("content", this.mottoTxt.getText());
                motto.putExtra("type", "motto");
                motto.putExtra("note", "不超过100个字符");
                startActivity(motto);
                return;
            case R.id.my_info_more:
                Intent more = new Intent(this, MyInfoSettingActivity.class);
                more.putExtra("title", "简介");
                more.putExtra("content", MyFragment.currentUser.getInfo());
                more.putExtra("type", "info");
                more.putExtra("note", "请输入个人简历");
                startActivity(more);
                return;
            default:
                break;
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            openGallery();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            Toast.makeText(this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        }
    }

    private void openGallery() {
        GalleryPick.getInstance()
                .setGalleryConfig(new GalleryConfig.Builder()
                .imageLoader(new GalleryGlideImageLoader())
                .iHandlerCallBack(iHandlerCallBack)
                .provider("com.yancy.gallerypickdemo.fileprovider")
                .pathList(new ArrayList<>())
                .multiSelect(false)
                .maxSize(9)
                .crop(true)
                .isShowCamera(true)
                .filePath("/Gallery/Pictures")
                .build())
                .open(this);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Toast.makeText(this, "您禁止了权限", 0).show();
                    return;
                } else {
                    openGallery();
                    return;
                }
            default:
                break;
        }
    }
}
