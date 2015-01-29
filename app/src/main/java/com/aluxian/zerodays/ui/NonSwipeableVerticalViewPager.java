package com.aluxian.zerodays.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import com.aluxian.zerodays.utils.ScrollerCustomDuration;

import java.lang.reflect.Field;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class NonSwipeableVerticalViewPager extends VerticalViewPager {

    public NonSwipeableVerticalViewPager(Context context) {
        super(context);
        init();
    }

    public NonSwipeableVerticalViewPager(Context context, AttributeSet attrs) {
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
            Field interpolatorField = VerticalViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            ScrollerCustomDuration mScroller = new ScrollerCustomDuration(getContext(), (Interpolator) interpolatorField.get(null));
            mScroller.setScrollDurationFactor(3);

            Field scrollerField = VerticalViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, mScroller);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Log.e(NonSwipeableVerticalViewPager.class.getName(), e.getMessage(), e);
        }
    }

}
