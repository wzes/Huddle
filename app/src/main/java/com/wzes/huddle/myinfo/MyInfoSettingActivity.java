package com.wzes.huddle.myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.wzes.huddle.R;

public class MyInfoSettingActivity extends AppCompatActivity {
    ImageButton backBtn;
    private String content;
    EditText contentTxt;
    private String note;
    TextView noteTxt;
    Button saveBtn;
    private String title;
    TextView titleTxt;

    class C04841 implements OnClickListener {
        C04841() {
        }

        public void onClick(View v) {
            MyInfoSettingActivity.this.finish();
        }
    }

    class C04852 implements OnClickListener {
        C04852() {
        }

        public void onClick(View v) {
            Toast.makeText(MyInfoSettingActivity.this, "已保存", 0).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_setting);
        Intent intent = getIntent();
        this.title = intent.getStringExtra("title");
        this.content = intent.getStringExtra("content");
        this.note = intent.getStringExtra("note");
        this.titleTxt = findViewById(R.id.my_info_setting_title);
        this.titleTxt.setText("修改" + this.title);
        this.contentTxt = findViewById(R.id.my_info_setting_txt);
        this.contentTxt.setText(this.content);
        this.noteTxt = findViewById(R.id.my_info_setting_note);
        this.noteTxt.setText(this.note);
        this.contentTxt.setSelection(this.content.length());
        this.backBtn = findViewById(R.id.my_info_setting_back);
        this.backBtn.setOnClickListener(new C04841());
        this.saveBtn = findViewById(R.id.my_info_setting_save);
        this.saveBtn.setOnClickListener(new C04852());
    }
}
