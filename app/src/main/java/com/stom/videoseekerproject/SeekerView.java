package com.stom.videoseekerproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by stom.k on 28.04.2015.
 */
public class SeekerView extends View implements View.OnTouchListener {

    public static interface OnSegmentStartChangedListener {
        void onSegmentStartChanged(View view, float seconds);
    }

    private int duration;
    private int totalDuration = 30;

    private int x;
    private Rect currentRect;

    private Paint paint = new Paint();

    private boolean isMoving = false;

    private OnSegmentStartChangedListener listener;

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
        paint.setColor(Color.BLACK);
    }

    public void setDuration(Video video) {
        duration = video.duration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void setListener(OnSegmentStartChangedListener listener) {
        this.listener = listener;
    }

    public float getSegmentStart() {
        return (float) x / getWidth() * totalDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentRect == null)
            updateRect();
        canvas.drawColor(Color.GRAY);
        canvas.drawRect(currentRect, paint);
    }

    private void updateRect() {
        currentRect = new Rect(x, 0, getWidth() * duration / totalDuration + x, getHeight());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX() - getLeft();
        int y = (int) event.getY() - getTop();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (currentRect.left <= x && currentRect.right >= x) {
                    isMoving = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving) {
                    if (x < 0)
                        x = 0;
                    if (x > getWidth() - getWidth() * duration / totalDuration)
                        x = getWidth() - getWidth() * duration / totalDuration;
                    this.x = x;
                    updateRect();
                    invalidate();
                    if (listener != null)
                        listener.onSegmentStartChanged(this, getSegmentStart());
                }
                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
                break;
        }
        return true;
    }
}
