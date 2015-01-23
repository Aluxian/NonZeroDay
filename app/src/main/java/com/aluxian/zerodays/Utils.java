package com.aluxian.zerodays;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

public class Utils {

    public static void initShadow(TextView textView, AttributeSet attrs) {
        TypedArray a = textView.getContext().obtainStyledAttributes(attrs, R.styleable.TextViewShadowDips);
        int shadowColor = a.getColor(R.styleable.TextViewShadowDips_shadowColor, 0);

        if (shadowColor != 0) {
            float shadowRadius = a.getDimension(R.styleable.TextViewShadowDips_shadowRadius, 1f);
            float shadowDx = a.getDimension(R.styleable.TextViewShadowDips_shadowDx, 0f);
            float shadowDy = a.getDimension(R.styleable.TextViewShadowDips_shadowDy, textView.getTextSize() / 12);

            textView.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        }

        a.recycle();
    }

}
