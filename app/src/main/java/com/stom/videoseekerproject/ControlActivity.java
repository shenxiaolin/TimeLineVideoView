package com.stom.videoseekerproject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class ControlActivity extends ActionBarActivity implements View.OnClickListener, TimeIndicatorView.OnSeekListener {

    private SeekerViewContainer seekerViewContainer;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controll);
        initViews();
    }

    private void initViews() {
        seekerViewContainer = (SeekerViewContainer) findViewById(R.id.seekerViewContainer);
        textView = (TextView) findViewById(R.id.textView);

        seekerViewContainer.setOnSeekListener(this);
        findViewById(R.id.addButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                seekerViewContainer.addVideo(new Video(15));
                break;
        }
    }

    @Override
    public void onTimeIndicatonSeek(long time) {
        textView.setText(Long.toString(time));
    }
}
