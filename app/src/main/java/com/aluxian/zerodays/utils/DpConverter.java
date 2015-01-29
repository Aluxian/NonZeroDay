package com.aluxian.zerodays.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DpConverter {

    private Resources mResources;

    public DpConverter(Context context) {
        mResources = context.getResources();
    }

    public int toPx(float units) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, units, mResources.getDisplayMetrics());
    }

}
