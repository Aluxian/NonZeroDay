package com.aluxian.zerodays.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import com.aluxian.zerodays.utils.CustomDurationScroller;
import com.aluxian.zerodays.utils.Log;

import java.lang.reflect.Field;

import uk.co.androidalliance.edgeeffectoverride.ViewPager;

/**
 * ViewPager that can only be swiped when the user is not interacting with its content. This extends the ViewPager from the
 * EdgeEffectOverride library to change the overscroll colour on devices previous to Lollipop.
 */
public class ContentAwareViewPagerCompat extends ViewPager {

    public boolean canSwipe = true;

    public ContentAwareViewPagerCompat(Context context) {
        super(context);
    }

    public ContentAwareViewPagerCompat(Context context, AttributeSet attrs) {
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
