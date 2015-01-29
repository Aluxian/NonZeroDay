package com.aluxian.zerodays.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.aluxian.zerodays.utils.Utils;
import com.devspark.robototextview.widget.RobotoTextView;

public class YellowTextView extends RobotoTextView {

    public YellowTextView(Context context) {
        super(context);
    }

    public YellowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Utils.setShadow(this, attrs);
    }

    public YellowTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Utils.setShadow(this, attrs);
    }

}
