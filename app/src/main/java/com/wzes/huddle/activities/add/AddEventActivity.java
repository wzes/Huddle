package com.wzes.huddle.activities.add;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzes.huddle.R;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEventActivity extends AppCompatActivity {

    @BindView(R.id.add_event_back_btn)
    ImageButton addEventBackBtn;
    @BindView(R.id.add_event_title)
    EditText addEventTitle;
    @BindView(R.id.add_event_content)
    EditText addEventContent;
    @BindView(R.id.add_event_enrool_start_date)
    TextView addEventEnroolStartDate;
    @BindView(R.id.add_event_enrool_end_date)
    TextView addEventEnroolEndDate;
    @BindView(R.id.add_event_match_start_date)
    TextView addEventMatchStartDate;
    @BindView(R.id.add_event_match_end_date)
    TextView addEventMatchEndDate;
    @BindView(R.id.add_event_type)
    TextView addEventType;
    @BindView(R.id.add_event_level)
    TextView addEventLevel;
    @BindView(R.id.add_event_add_iamge)
    TextView addEventAddIamge;
    @BindView(R.id.add_event_image)
    ImageView addEventImage;
    @BindView(R.id.add_event_send_btn)
    CircularProgressButton addEventSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.add_event_back_btn, R.id.add_event_enrool_start_date, R.id.add_event_enrool_end_date,
            R.id.add_event_match_start_date, R.id.add_event_match_end_date, R.id.add_event_type,
            R.id.add_event_level, R.id.add_event_add_iamge, R.id.add_event_send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_event_back_btn:
                finish();
                break;
            case R.id.add_event_enrool_start_date:
                break;
            case R.id.add_event_enrool_end_date:
                break;
            case R.id.add_event_match_start_date:
                break;
            case R.id.add_event_match_end_date:
                break;
            case R.id.add_event_type:
                break;
            case R.id.add_event_level:
                break;
            case R.id.add_event_add_iamge:
                break;
            case R.id.add_event_send_btn:
                break;
        }
    }
}
