package com.aluxian.zerodays.utils;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * A Scroller with adjustable duration.
 */
public class CustomDurationScroller extends Scroller {

    /** The factor by which the duration will change. */
    private float mScrollFactor = 1;

    public CustomDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    /**
     * Sets the factor by which the duration will change.
     *
     * @param scrollFactor The new factor.
     */
    public void setScrollDurationFactor(float scrollFactor) {
        mScrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
    }

}
