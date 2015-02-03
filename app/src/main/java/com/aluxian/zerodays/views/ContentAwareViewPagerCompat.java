package com.aluxian.zerodays.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import uk.co.androidalliance.edgeeffectoverride.ViewPager;

/**
 * ViewPager that can only be swiped when the user is not interacting with its content. This extends the ViewPager from the
 * EdgeEffectOverride library to change the overscroll colour on devices previous to Lollipop.
 */
public class ContentAwareViewPagerCompat extends ViewPager {

    private ContentAwareViewPager.Callbacks mCallbacks;
    private boolean mCanSwipe = true;

    public ContentAwareViewPagerCompat(Context context) {
        super(context);
    }

    public ContentAwareViewPagerCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mCanSwipe && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCallbacks != null && event.getAction() == MotionEvent.ACTION_MOVE) {
            mCallbacks.isSwipingAway();
        }

        return mCanSwipe && super.onTouchEvent(event);
    }

    public void setSwipingEnabled(boolean enabled) {
        mCanSwipe = enabled;
    }

    public void setCallbacks(ContentAwareViewPager.Callbacks callbacks) {
        mCallbacks = callbacks;
    }

}
