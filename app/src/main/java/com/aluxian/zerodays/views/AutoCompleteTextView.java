package com.aluxian.zerodays.views;

import android.content.Context;
import android.util.AttributeSet;

import com.aluxian.zerodays.utils.Shadow;
import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;

/**
 * An AutoCompleteTextView with text shadow that uses the Roboto font.
 */
public class AutoCompleteTextView extends RobotoAutoCompleteTextView {

    public AutoCompleteTextView(Context context) {
        super(context);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Shadow.setOn(this, attrs);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Shadow.setOn(this, attrs);
    }

}
