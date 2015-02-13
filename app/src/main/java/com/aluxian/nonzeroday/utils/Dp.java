package com.aluxian.nonzeroday.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Convert from dp units to pixels.
 */
public class Dp {

    /**
     * @param units The amount of DP units to convert.
     * @return The corresponding value in pixels.
     */
    public static int toPx(float units) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, units, Resources.getSystem().getDisplayMetrics()));
    }

}
