package com.aluxian.zerodays.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.aluxian.zerodays.utils.Utils;
import com.devspark.robototextview.widget.RobotoTextView;

public class SquareTextView extends RobotoTextView {

    public SquareTextView(Context context) {
        super(context);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}
