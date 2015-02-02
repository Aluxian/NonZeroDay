package com.aluxian.zerodays.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager that can only be swiped when the user is not interacting with its content.
 */
public class ContentAwareViewPager extends ViewPager {

    public boolean canSwipe = true;

    public ContentAwareViewPager(Context context) {
        super(context);
    }

    public ContentAwareViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return canSwipe && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return canSwipe && super.onTouchEvent(event);
    }

}
