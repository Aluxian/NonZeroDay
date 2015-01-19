package com.aluxian.zerodays.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ListPopupWindow;

import com.aluxian.zerodays.Utils;
import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;

import java.lang.reflect.Field;

public class YellowAutoCompleteTextView extends RobotoAutoCompleteTextView {

    public YellowAutoCompleteTextView(Context context) {
        super(context);
    }

    public YellowAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Utils.initShadow(this, attrs);
    }

    public YellowAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Utils.initShadow(this, attrs);
    }

}
