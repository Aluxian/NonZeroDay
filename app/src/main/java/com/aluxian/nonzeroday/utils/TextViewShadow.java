package com.aluxian.nonzeroday.utils;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.aluxian.nonzeroday.R;

/**
 * Helper class to simply setting a shadow on TextViews.
 */
public class TextViewShadow {

    /**
     * Set a shadow on the given TextView, whose size depends on the height of the TextView.
     *
     * @param textView The TextView on which the shadow is applied.
     * @param attrs    An AttributeSet used to extract the shadow's colour, radius and offsets.
     */
    public static void setOn(TextView textView, AttributeSet attrs) {
        TypedArray a = textView.getContext().obtainStyledAttributes(attrs, R.styleable.TextViewShadowDips);
        int shadowColor = a.getColor(R.styleable.TextViewShadowDips_shadowColor, 0);

        if (shadowColor != 0) {
            float shadowRadius = a.getDimension(R.styleable.TextViewShadowDips_shadowRadius, 1f);
            float shadowDx = a.getDimension(R.styleable.TextViewShadowDips_shadowDx, 0f);
            float shadowDy = a.getDimension(R.styleable.TextViewShadowDips_shadowDy, textView.getTextSize() / 12);

            textView.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
        }

        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        a.recycle();
    }

}
