package com.aluxian.nonzeroday.views;

import android.content.Context;
import android.util.AttributeSet;

import com.aluxian.nonzeroday.utils.TextViewShadow;
import com.devspark.robototextview.widget.RobotoTextView;

import me.grantland.widget.AutofitHelper;

/**
 * A TextView that adjusts its text size in order to fill as much width as possible.
 */
public class ShadowedFullWidthTextView extends RobotoTextView {

    public ShadowedFullWidthTextView(Context context) {
        super(context);
        AutofitHelper.create(this);
    }

    public ShadowedFullWidthTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TextViewShadow.setOn(this, attrs);
        AutofitHelper.create(this, attrs);
    }

    public ShadowedFullWidthTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TextViewShadow.setOn(this, attrs);
        AutofitHelper.create(this, attrs, defStyle);
    }

}
