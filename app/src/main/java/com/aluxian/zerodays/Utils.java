package com.aluxian.zerodays;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

public class Utils {

    public static void initShadow(TextView view, AttributeSet attrs) {
        TypedArray a = view.getContext().obtainStyledAttributes(attrs, R.styleable.TextViewShadowDips);
        float shadowRadius = a.getDimension(R.styleable.TextViewShadowDips_shadowRadius, 0.001f);
        float shadowDx = a.getDimension(R.styleable.TextViewShadowDips_shadowDx, 0f);
        float shadowDy = a.getDimension(R.styleable.TextViewShadowDips_shadowDy, 0f);
        int shadowColor = a.getColor(R.styleable.TextViewShadowDips_shadowColor, 0);

        if (shadowColor != 0) {
            view.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        } else {
            view.getPaint().clearShadowLayer();
        }

        a.recycle();
    }

}
