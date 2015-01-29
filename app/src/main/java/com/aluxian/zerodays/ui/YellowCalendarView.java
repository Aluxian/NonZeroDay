package com.aluxian.zerodays.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.aluxian.zerodays.R;
import com.devspark.robototextview.util.RobotoTypefaceManager;

public class YellowCalendarView extends CalendarView {

    public YellowCalendarView(Context context) {
        super(context);
        init(context);
    }

    public YellowCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public YellowCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        TextView monthName = (TextView) findViewById(context.getResources().getIdentifier("month_name", "id", "android"));
        monthName.setTypeface(RobotoTypefaceManager.obtainTypeface(context, RobotoTypefaceManager.Typeface.ROBOTO_SLAB_REGULAR));
        monthName.setTextColor(Color.WHITE);

        ViewGroup dayNamesHeader = (ViewGroup) findViewById(context.getResources().getIdentifier("day_names", "id", "android"));
        dayNamesHeader.setBackgroundResource(R.color.primaryDarkLighter);
    }

}
