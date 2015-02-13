package com.aluxian.nonzeroday.views;

import android.content.Context;
import android.util.AttributeSet;

import com.aluxian.nonzeroday.utils.TextViewShadow;
import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;

/**
 * An AutoCompleteTextView with text shadow that uses the Roboto font.
 */
public class ShadowedAutoCompleteTextView extends RobotoAutoCompleteTextView {

    public ShadowedAutoCompleteTextView(Context context) {
        super(context);
    }

    public ShadowedAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextViewShadow.setOn(this, attrs);
    }

    public ShadowedAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TextViewShadow.setOn(this, attrs);
    }

}
