package com.stom.videoseekerproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by stom.k on 28.04.2015.
 */
public class TimeIndicatorView extends View implements View.OnTouchListener {

    public static interface OnSeekListener {
        void onTimeIndicatonSeek(long time);
    }

    private static final long MAX_DURATION = 30000;

    public static final int TOTAL_HEIGHT = 50;
    private static final int SCALE_HEIGHT_DP = 5;
    public static final int INDICATOR_WIDTH_DP = 30;
    private static final float SCALE_STROKE_WIDTH = 5f;

    private static final int BACKGROUND_COLOR = Color.GRAY;
    private static final int SCALE_COLOR = Color.YELLOW;
    private static final int INDICATOR_COLOR = Color.BLACK;

    private OnSeekListener listener;

    private int totalHeight, scaleHeight, indicatorWidth;

    private long startTime = 0;
    private long endTime = 30000;

    private float position;
    private float left, right;
    private int width, height;

    private Paint backgroundPaint = new Paint();
    private Paint scalePaint = new Paint();
    private Paint indicatorPaint = new Paint();

    private int touchX;
    private float startX;
    private boolean isMoving = false;

    public TimeIndicatorView(Context context) {
        super(context);
        initView();
    }

    public TimeIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TimeIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        totalHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TOTAL_HEIGHT, getResources().getDisplayMetrics());
        scaleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SCALE_HEIGHT_DP, getResources().getDisplayMetrics());
        indicatorWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, INDICATOR_WIDTH_DP, getResources().getDisplayMetrics());

        setOnTouchListener(this);

        backgroundPaint.setColor(BACKGROUND_COLOR);

        scalePaint.setColor(SCALE_COLOR);
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setStrokeWidth(SCALE_STROKE_WIDTH);

        indicatorPaint.setColor(INDICATOR_COLOR);
        indicatorPaint.setStyle(Paint.Style.FILL);
    }

    public void setOnSeekListener(OnSeekListener listener) {
        this.listener = listener;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        invalidateSize();
        invalidate();
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        invalidateSize();
        if (position > right - left)
            position = right - left;
        invalidate();
    }

    public long getCurrentTime() {
        return (long) (position / (right - left) * getDuration());
    }

    public float getDuration() {
        return endTime - startTime;
    }

    public int getSize() {
        return getWidth() - indicatorWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, totalHeight);
        if (width > 0 && totalHeight > 0) {
            this.width = width;
            this.height = totalHeight;
            invalidateSize();
        }
    }

    private void invalidateSize() {
        left = indicatorWidth / 2 + (width - indicatorWidth) * startTime / MAX_DURATION;
        right = indicatorWidth / 2 + (width - indicatorWidth) * endTime / MAX_DURATION;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(left, 0f, right, height, backgroundPaint);
        drawScale(canvas);
        drawIndicator(canvas);
    }

    private void drawScale(Canvas canvas) {
        float secondSize = (right - left) / (endTime - startTime) * 1000f;

        Path path = new Path();
        path.moveTo(left, height - SCALE_STROKE_WIDTH / 2f);
        path.lineTo(right, height - SCALE_STROKE_WIDTH / 2f);
        for (int i = 1; i * secondSize < right - left; i++) {
            path.moveTo(left + i * secondSize, height);
            path.lineTo(left + i * secondSize, height - (i % 5 == 0 ? scaleHeight * 2 : scaleHeight));
        }
        path.close();

        canvas.drawPath(path, scalePaint);
    }

    private void drawIndicator(Canvas canvas) {
        Path path = new Path();
        path.moveTo(left + position, height);
        path.lineTo(left - indicatorWidth / 2f + position, 0);
        path.lineTo(left + indicatorWidth / 2f + position, 0);
        path.lineTo(left + position, height);
        path.close();

        canvas.drawPath(path, indicatorPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX() - getLeft();
        int y = (int) event.getY() - getTop();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (x >= position + left - indicatorWidth / 2f && x <= position + left + indicatorWidth / 2f) {
                    isMoving = true;
                    touchX = x;
                    startX = position;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving) {
                    float temp = startX + x - touchX;
                    if (temp < 0)
                        temp = 0;
                    if (temp > right - left)
                        temp = right - left;
                    this.position = temp;
                    invalidate();
                    if (listener != null)
                        listener.onTimeIndicatonSeek(getCurrentTime());
                }
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }
        return true;
    }
}
