package com.stom.videoseekerproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by stom.k on 28.04.2015.
 */
public class SeekerView extends View implements View.OnTouchListener {

    public static interface OnSeekListener {
        void onSeek(long start, long end);
    }

    private static final long TOTAL_DURATION = 30000;

    private long duration;
    private float position;
    private Rect currentRect;

    private Paint segmentPaint = new Paint();
    private Paint backgroundPaint = new Paint();
    private int indicatorWidth;

    private float touchX, startPosition;
    private boolean isMoving = false;

    private Video video;

    private OnSeekListener listener;

    public SeekerView(Context context) {
        super(context);
        initView();
    }

    public SeekerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SeekerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOnTouchListener(this);
        backgroundPaint.setColor(Color.GRAY);
        segmentPaint.setColor(Color.BLACK);
        indicatorWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TimeIndicatorView.INDICATOR_WIDTH_DP, getResources().getDisplayMetrics());
    }

    public void setVideo(Video video) {
        this.video = video;
        this.duration = video.duration;
        if (listener != null)
            listener.onSeek(0, video.duration);
    }

    public Video getVideo() {
        return video;
    }

    public long getDuration() {
        return duration;
    }

    public long getSegmentStart() {
        return (long) (TOTAL_DURATION * position / (getWidth() - indicatorWidth));
    }

    public float getSegmentEnd() {
        return 0;
    }

    public void setOnSeekListener(OnSeekListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentRect == null)
            updateRect();
        canvas.drawRect(indicatorWidth / 2, 0, getWidth() - indicatorWidth / 2, getHeight(), backgroundPaint);
        canvas.drawRect(currentRect, segmentPaint);
    }

    private void updateRect() {
        currentRect = new Rect((int) position + indicatorWidth / 2, 0,
                (int) ((getWidth() - indicatorWidth) * duration / TOTAL_DURATION + position + indicatorWidth / 2), getHeight());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX() - getLeft();
        int y = (int) event.getY() - getTop();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (x >= currentRect.left && x <= currentRect.right) {
                    isMoving = true;
                    touchX = x;
                    startPosition = position;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving) {
                    float tempPosition = startPosition + x - touchX;
                    if (tempPosition < 0)
                        tempPosition = 0;
                    if (tempPosition > (getWidth() - indicatorWidth) * (1 - (float)duration / TOTAL_DURATION))
                        tempPosition = (getWidth() - indicatorWidth) * (1 - (float)duration / TOTAL_DURATION);
                    this.position = tempPosition;
                    updateRect();
                    invalidate();
                    if (listener != null)
                        listener.onSeek(getSegmentStart(), getSegmentStart() + duration);
                }
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }
        return true;
    }
}
