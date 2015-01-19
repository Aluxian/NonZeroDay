package com.aluxian.zerodays.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.aluxian.zerodays.R;
import com.aluxian.zerodays.Utils;
import com.devspark.robototextview.widget.RobotoTextView;

public class YellowTextView extends RobotoTextView {

    public YellowTextView(Context context) {
        super(context);
    }

    public YellowTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Utils.initShadow(this, attrs);
    }

    public YellowTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Utils.initShadow(this, attrs);
    }

}
