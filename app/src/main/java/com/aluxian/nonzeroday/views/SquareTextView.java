package com.aluxian.nonzeroday.views;

import android.content.Context;
import android.util.AttributeSet;

import com.devspark.robototextview.widget.RobotoTextView;

/**
 * A TextView whose height is always equal to its width.
 */
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
