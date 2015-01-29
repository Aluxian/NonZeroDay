package com.aluxian.zerodays.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.aluxian.zerodays.utils.Utils;
import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;

public class YellowAutoCompleteTextView extends RobotoAutoCompleteTextView {

    public YellowAutoCompleteTextView(Context context) {
        super(context);
    }

    public YellowAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Utils.setShadow(this, attrs);
    }

    public YellowAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Utils.setShadow(this, attrs);
    }

}
