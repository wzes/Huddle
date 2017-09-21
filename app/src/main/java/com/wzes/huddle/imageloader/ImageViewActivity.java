package com.wzes.huddle.imageloader;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;

public class ImageViewActivity extends AppCompatActivity {
    @BindView(R.id.image_loader_back) ImageButton backBtn;
    @BindView(R.id.image_loader_view) PinchImageView imageView;
    private String uri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        uri = getIntent().getStringExtra("uri");
        Glide.with(this).load(this.uri).into(imageView);
        backBtn.setOnClickListener(view -> finish());
    }
}
