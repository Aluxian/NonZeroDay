package com.aluxian.zerodays.views;

import android.content.Context;
import android.util.AttributeSet;

import com.aluxian.zerodays.utils.Shadow;
import com.devspark.robototextview.widget.RobotoTextView;

/**
 * An TextView with text shadow that uses the Roboto font.
 */
public class TextView extends RobotoTextView {

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Shadow.setOn(this, attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Shadow.setOn(this, attrs);
    }

}
