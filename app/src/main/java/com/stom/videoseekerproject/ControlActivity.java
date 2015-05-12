package com.stom.videoseekerproject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class ControlActivity extends ActionBarActivity implements SeekerView.OnSegmentStartChangedListener {

    private TextView textView;
    private SeekerViewContainer seekerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controll);
        initViews();
    }

    private void initViews() {
        textView = (TextView) findViewById(R.id.textView);
        seekerViewContainer = (SeekerViewContainer) findViewById(R.id.seekerViewContainer);
        seekerViewContainer.addVideo(new Video(20));
        seekerViewContainer.addVideo(new Video(10));
        seekerViewContainer.addVideo(new Video(15));
    }

    @Override
    public void onSegmentStartChanged(View view, float seconds) {
        textView.setText(Float.toString(seconds));
    }
}
