package com.example.techniqueshoppebackendconnectionattempt1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;

public class ZoomableFrameLayout extends FrameLayout {

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private float lastTouchX, lastTouchY;
    private int activePointerId = MotionEvent.INVALID_POINTER_ID;
    private boolean isDragging = false;

    public ZoomableFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        handleTouchEvents(event);
        return true;
    }

    private void handleTouchEvents(MotionEvent event) {
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = event.getActionIndex();
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Remember where we started
                lastTouchX = x;
                lastTouchY = y;
                activePointerId = event.getPointerId(0);
                isDragging = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(activePointerId);
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Calculate the distance moved
                final float dx = x - lastTouchX;
                final float dy = y - lastTouchY;

                // Check if movement is enough to be considered a drag
                if (!isDragging && Math.sqrt(dx * dx + dy * dy) > 5) {
                    isDragging = true;
                }

                if (isDragging) {
                    // Translate the layout
                    setTranslationX(getTranslationX() + dx);
                    setTranslationY(getTranslationY() + dy);
                }

                lastTouchX = x;
                lastTouchY = y;
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                activePointerId = MotionEvent.INVALID_POINTER_ID;
                isDragging = false;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = event.getActionIndex();
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == activePointerId) {
                    // This was our active pointer going up. Choose a new active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = event.getX(newPointerIndex);
                    lastTouchY = event.getY(newPointerIndex);
                    activePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f)); // Limit scale factor
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);
            return true;
        }
    }

    public void resetPosition() {
        setTranslationX(0);
        setTranslationY(0);
        setScaleX(1.0f);
        setScaleY(1.0f);
        scaleFactor = 1.0f;
    }
}
