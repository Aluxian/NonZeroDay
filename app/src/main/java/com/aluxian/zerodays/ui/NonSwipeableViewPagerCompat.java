package com.aluxian.zerodays.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import uk.co.androidalliance.edgeeffectoverride.ViewPager;

public class NonSwipeableViewPagerCompat extends ViewPager {

    public NonSwipeableViewPagerCompat(Context context) {
        super(context);
    }

    public NonSwipeableViewPagerCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Don't allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Don't allow swiping to switch between pages
        return false;
    }

}
