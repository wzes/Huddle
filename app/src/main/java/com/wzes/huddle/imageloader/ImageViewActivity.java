package com.wzes.huddle.imageloader;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wzes.huddle.C0479R;

public class ImageViewActivity extends AppCompatActivity {
    private static String TAG = "TTTT";
    @BindView(2131624081)
    public ImageButton backBtn;
    @BindView(2131624080)
    public ImageLoadView imageLoadView;
    private String uri;

    class C04831 implements OnClickListener {
        C04831() {
        }

        public void onClick(View v) {
            ImageViewActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0479R.layout.activity_image_view);
        ButterKnife.bind((Activity) this);
        if (VERSION.SDK_INT >= 21) {
            getWindow().addFlags(67108864);
        }
        this.uri = getIntent().getStringExtra("uri");
        Glide.with((FragmentActivity) this).load(this.uri).diskCacheStrategy(DiskCacheStrategy.ALL).into(this.imageLoadView);
        this.backBtn.setOnClickListener(new C04831());
    }
}
