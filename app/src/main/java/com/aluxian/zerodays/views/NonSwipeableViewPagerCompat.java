package com.aluxian.zerodays.views;

import android.content.Context;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.animation.Interpolator;

import com.aluxian.zerodays.utils.CustomDurationScroller;
import com.aluxian.zerodays.utils.Log;

import java.lang.reflect.Field;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import uk.co.androidalliance.edgeeffectoverride.ViewPager;

/**
 * Non-swipeable ViewPager used to display the goal input pages. The scrolling animation is slowed down. This extends the ViewPager from
 * the EdgeEffectOverride library to change the overscroll colour on devices previous to Lollipop.
 */
public class NonSwipeableViewPagerCompat extends ViewPager {

    public NonSwipeableViewPagerCompat(Context context) {
        super(context);
        init();
    }

    public NonSwipeableViewPagerCompat(Context context, AttributeSet attrs) {
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
