package com.stom.videoseekerproject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by stom.k on 12.05.2015.
 */
public class SeekerViewContainer extends LinearLayout implements SeekerView.OnSeekListener {

    private static final int SEEKER_HEIGHT_DP = 50;
    private static final int MARGIN_DP = 5;

    private TimeIndicatorView timeIndicatorView;
    private ArrayList<SeekerView> seekerViews = new ArrayList<>();

    public SeekerViewContainer(Context context) {
        super(context);
        initView();
    }

    public SeekerViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SeekerViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        setPadding(0, 0, 0, MARGIN_DP);
        setGravity(Gravity.CENTER_HORIZONTAL);

        timeIndicatorView = new TimeIndicatorView(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_DP, getResources().getDisplayMetrics());
        timeIndicatorView.setLayoutParams(layoutParams);
        addView(timeIndicatorView);
    }

    public void setOnSeekListener(TimeIndicatorView.OnSeekListener onSeekListener) {
        timeIndicatorView.setOnSeekListener(onSeekListener);
    }

    public void addVideo(Video video) {
        SeekerView seekerView = new SeekerView(getContext());
        seekerView.setOnSeekListener(this);
        seekerView.setLayoutParams(getLayoutParamsForSeekerView());
        seekerViews.add(seekerView);
        addView(seekerView);
        seekerView.setVideo(video);
    }

    private LayoutParams getLayoutParamsForSeekerView() {
        LayoutParams layoutParams;
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SEEKER_HEIGHT_DP, getResources().getDisplayMetrics());
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        layoutParams.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN_DP, getResources().getDisplayMetrics());
        return layoutParams;
    }

    @Override
    public void onSeek(long start, long end) {
        long minStart = -1;
        long maxEnd = -1;
        for (SeekerView seekerView : seekerViews) {
            if (seekerView.getSegmentStart() < minStart || minStart < 0)
                minStart = seekerView.getSegmentStart();

            if (seekerView.getSegmentStart() + seekerView.getDuration() > maxEnd || maxEnd < 0)
                maxEnd = seekerView.getSegmentStart() + seekerView.getDuration();
        }
        timeIndicatorView.setStartTime(minStart);
        timeIndicatorView.setEndTime(maxEnd);
    }
}
