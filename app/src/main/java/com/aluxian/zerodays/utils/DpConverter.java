package com.aluxian.zerodays.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Convert from dp units to pixels.
 */
public class DpConverter {

    /** The DisplayMetrics object to use for calculations. */
    private DisplayMetrics mDisplayMetrics;

    public DpConverter(Context context) {
        mDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * @param units The amount of DP units to convert.
     * @return The corresponding value in pixels.
     */
    public int toPx(float units) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, units, mDisplayMetrics);
    }

}
