package com.aluxian.zerodays.views;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.aluxian.zerodays.utils.Shadow;
import com.devspark.robototextview.widget.RobotoTextView;

/**
 * A TextView that adjusts its text size in order to fill as much width as possible.
 */
public class FullWidthTextView extends RobotoTextView {

    /** Whether calculations have been made to adjust the text size to fill the width. */
    private boolean mWidthFilled = false;

    public FullWidthTextView(Context context) {
        super(context);
    }

    public FullWidthTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Shadow.setOn(this, attrs);
    }

    public FullWidthTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Shadow.setOn(this, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!mWidthFilled) {
            mWidthFilled = true;
            fillWidth();
        }
    }

    private void fillWidth() {
        post(() -> {
            float expectedWidth = getWidth();

            Rect bounds = new Rect();
            Paint paint = new Paint(getPaint());
            String text = getText().toString();
            paint.setTextSize(10);
            paint.getTextBounds(text, 0, text.length(), bounds);

            float size = expectedWidth / bounds.width() * 10;
            setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        });
    }

}
