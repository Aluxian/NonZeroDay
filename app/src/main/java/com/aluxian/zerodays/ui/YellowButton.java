package com.aluxian.zerodays.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;

import com.aluxian.zerodays.R;
import com.devspark.robototextview.widget.RobotoButton;

public class YellowButton extends RobotoButton {

    public YellowButton(Context context) {
        super(context);
        init();
    }

    public YellowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YellowButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            getBackground().setAlpha(72);
        }
    }

}
