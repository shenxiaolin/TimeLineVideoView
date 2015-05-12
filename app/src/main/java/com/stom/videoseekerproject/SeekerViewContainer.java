package com.stom.videoseekerproject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by stom.k on 12.05.2015.
 */
public class SeekerViewContainer extends LinearLayout {

    private static final int MAX_HEIGHT_DP = 50;

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
        setOrientation(HORIZONTAL);
    }

    public void addVideo(Video video) {
        SeekerView seekerView = new SeekerView(getContext());
        seekerView.setDuration(video);
        seekerView.setLayoutParams(getLayoutParamsForSeekerView());
        addView(seekerView);
    }

    private LayoutParams getLayoutParamsForSeekerView() {
        LayoutParams layoutParams;
        int maxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MAX_HEIGHT_DP, getResources().getDisplayMetrics());
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxHeight);
        return layoutParams;
    }
}
