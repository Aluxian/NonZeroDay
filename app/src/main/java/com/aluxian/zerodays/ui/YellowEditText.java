package com.aluxian.zerodays.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import com.aluxian.zerodays.R;
import com.aluxian.zerodays.Utils;
import com.devspark.robototextview.widget.RobotoEditText;

public class YellowEditText extends RobotoEditText {

    public YellowEditText(Context context) {
        super(context);
        init();
    }

    public YellowEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Utils.initShadow(this, attrs);
        init();
    }

    public YellowEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Utils.initShadow(this, attrs);
        init();
    }

    private void init() {
        getBackground().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
    }

}
