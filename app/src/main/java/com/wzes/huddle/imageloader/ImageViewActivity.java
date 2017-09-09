package com.wzes.huddle.imageloader;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.wzes.huddle.R;

public class ImageViewActivity extends AppCompatActivity {
    @BindView(R.id.image_loader_back) ImageButton backBtn;
    @BindView(R.id.image_loader_view) ImageView imageView;
    private String uri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        uri = getIntent().getStringExtra("uri");
        Glide.with(this).load(this.uri).into(imageView);
        backBtn.setOnClickListener(view -> finish());
    }
}
