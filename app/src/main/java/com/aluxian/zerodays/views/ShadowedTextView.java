package com.aluxian.zerodays.views;

import android.content.Context;
import android.util.AttributeSet;

import com.aluxian.zerodays.utils.TextViewShadow;
import com.devspark.robototextview.widget.RobotoTextView;

/**
 * An TextView with text shadow that uses the Roboto font.
 */
public class ShadowedTextView extends RobotoTextView {

    public ShadowedTextView(Context context) {
        super(context);
    }

    public ShadowedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextViewShadow.setOn(this, attrs);
    }

    public ShadowedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TextViewShadow.setOn(this, attrs);
    }

}
