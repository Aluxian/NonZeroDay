package com.aluxian.nonzeroday.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager that can only be swiped when the user is not interacting with its content.
 */
public class ContentAwareViewPager extends ViewPager {

    private SwipeListener mSwipeListener;
    private boolean mCanSwipe = true;

    public ContentAwareViewPager(Context context) {
        super(context);
    }

    public ContentAwareViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mCanSwipe && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSwipeListener != null && event.getAction() == MotionEvent.ACTION_MOVE) {
            mSwipeListener.isSwipingAway();
        }

        return mCanSwipe && super.onTouchEvent(event);
    }

    public void setSwipingEnabled(boolean enabled) {
        mCanSwipe = enabled;
    }

    public void setSwipeListener(SwipeListener swipeListener) {
        mSwipeListener = swipeListener;
    }

    public static interface SwipeListener {

        /**
         * Called when the user starts swiping the ViewPager.
         */
        public void isSwipingAway();

    }

}
