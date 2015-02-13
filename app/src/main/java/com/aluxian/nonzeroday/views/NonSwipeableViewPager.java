package com.aluxian.nonzeroday.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.animation.Interpolator;

import com.aluxian.nonzeroday.utils.CustomDurationScroller;
import com.aluxian.nonzeroday.utils.Log;

import java.lang.reflect.Field;

/**
 * Non-swipeable ViewPager used to display the goal input pages. The scrolling animation is slowed down.
 */
public class NonSwipeableViewPager extends ViewPager {

    public NonSwipeableViewPager(Context context) {
        super(context);
        init();
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
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

    private void init() {
        try {
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            CustomDurationScroller mScroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
            mScroller.setScrollDurationFactor(3);

            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, mScroller);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Log.e(e);
        }
    }

}
