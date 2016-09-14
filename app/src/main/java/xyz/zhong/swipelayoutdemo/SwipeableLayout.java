package xyz.zhong.swipelayoutdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * Created by xinyuanzhong on 16/9/12.
 */
public class SwipeableLayout extends FrameLayout {
    private static final float INCLINATION = 10;
    public static final float SPEED_TO_TRIGGER_ACTION = 0.5f;
    private View mainView;
    private float mLastX;
    private float mLastY;
    private float offSetX;
    private float menuLayoutWidth;
    private boolean isOnFocus = false;
    private long mLastTime;
    private View menuLayout;
    private boolean isPinned = false;

    public void setOnPinnedListener(OnPinnedListener onPinnedListener) {
        this.onPinnedListener = onPinnedListener;
    }

    private OnPinnedListener onPinnedListener;

    public boolean isPinned() {
        return isPinned;
    }

    public interface OnPinnedListener {
        void onPinned();

        void onDisPinned();
    }

    public SwipeableLayout(Context context) {
        super(context);

    }

    public SwipeableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public SwipeableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mainView == null) {
            super.onTouchEvent(event);
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (offSetX < 0) {
                        setPinned(false);
                        return false;
                    }
                    mLastX = event.getRawX();
                    mLastY = event.getRawY();
                    mLastTime = System.currentTimeMillis();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float deltaY = mLastY - event.getRawY();
                    float deltaX = mLastX - event.getRawX();
                    if (!isOnFocus && deltaX < 0 || Math.abs(offSetX - deltaX) > menuLayoutWidth) {
                        return false;
                    }
                    if (Math.abs(deltaX) > Math.abs(deltaY) * INCLINATION || isOnFocus) {
                        requestDisallowInterceptTouchEvent(true);
                        isOnFocus = true;
                    } else {
                        requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                    mainView.setTranslationX(offSetX - deltaX);
                    invalidate();
                    return true;
                case MotionEvent.ACTION_UP:
                    offSetX = mainView.getTranslationX();
                    if (isFastMove(event.getRawX(), mLastX, mLastTime, System.currentTimeMillis())) {
                        setPinnedWithSpeed(offSetX, getSpeed(event.getRawX(),
                                mLastX, mLastTime, System.currentTimeMillis()));
                    } else {
                        if (Math.abs(offSetX) < menuLayoutWidth / 2) {
                            setPinned(false);
                        } else {
                            setPinned(true);
                        }
                    }
                    isOnFocus = false;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);

    }

    private float getSpeed(float endX, float startX, long startTime, long endTime) {
        float speed = (endX - startX) / (startTime - endTime);
        return Math.abs(speed);
    }

    private void setPinnedWithSpeed(float offSetX, float speed) {
        long duration = (long) ((menuLayoutWidth - Math.abs(offSetX)) / speed);
        if (onPinnedListener != null) {
            onPinnedListener.onPinned();
        }
        isPinned = true;
        smoothScrollToPosition(offSetX, -menuLayoutWidth, Math.abs(duration) / 2);

    }

    private boolean isFastMove(float endX, float startX, long startTime, long endTime) {
        if (endX > startX) {
            return false;
        }
        if ((getSpeed(endX, startX, startTime, endTime)) > SPEED_TO_TRIGGER_ACTION) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * set item pinned state
     * @param shouldPinned
     * @param shouldAnim smooth or not
     */
    public void setPinned(boolean shouldPinned, boolean shouldAnim) {
        if (shouldPinned) {
            if (onPinnedListener != null) {
                onPinnedListener.onPinned();
            }
            smoothScrollToPosition(offSetX, -menuLayoutWidth, shouldAnim ? 300 : 0);
            isPinned = true;

        } else {
            if (onPinnedListener != null) {
                onPinnedListener.onDisPinned();
            }
            smoothScrollToPosition(offSetX, 0, shouldAnim ? 300 : 0);
            isPinned = false;
        }
    }

    public void setPinned(boolean shouldPinned) {
        setPinned(shouldPinned, true);
    }

    private void smoothScrollToPosition(float startOffSet, float endOffSet, long duration) {
        ValueAnimator objectAnimator = ValueAnimator.ofFloat(startOffSet, endOffSet);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float offSet = (float) valueAnimator.getAnimatedValue();
                if (mainView == null) {
                    return;
                }
                mainView.setTranslationX(offSet);
            }
        });
        objectAnimator.start();
        offSetX = endOffSet;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mainView = getChildAt(1);
        menuLayout = getChildAt(0);
        if (menuLayout == null) {
            menuLayoutWidth = getWidth() / 4;
        } else {
            menuLayoutWidth = menuLayout.getWidth();
        }
    }
}
