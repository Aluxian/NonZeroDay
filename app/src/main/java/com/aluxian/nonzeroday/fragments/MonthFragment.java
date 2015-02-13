package com.aluxian.nonzeroday.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aluxian.nonzeroday.R;
import com.aluxian.nonzeroday.models.DateInfo;
import com.aluxian.nonzeroday.models.DayGoal;
import com.aluxian.nonzeroday.utils.Async;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthFragment extends Fragment {

    private static final String KEY_DATE_MILLIS = "date_millis";

    private LayoutInflater mLayoutInflater;
    private TextView mHighlightedCellText;
    private HoverCardCallbacks mHoverCardCallbacks;

    public static MonthFragment newInstance(long date) {
        Bundle args = new Bundle();
        args.putLong(KEY_DATE_MILLIS, date);

        MonthFragment fragment = new MonthFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHoverCardCallbacks = (HoverCardCallbacks) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Calendar monthCalendar = Calendar.getInstance();
        monthCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        monthCalendar.setTimeInMillis(getArguments().getLong(KEY_DATE_MILLIS));

        // Create the layout
        mLayoutInflater = inflater;
        LinearLayout rootLayout = (LinearLayout) inflater.inflate(R.layout.calendar_grid, container, false);
        rootLayout.postDelayed(() -> Async.run(() -> buildList(monthCalendar), (datesList) -> populateLayout(datesList, rootLayout)), 300);

        return rootLayout;
    }

    private List<DateInfo> buildList(Calendar monthCalendar) {
        List<DateInfo> datesList = new ArrayList<>();

        DateInfo selectedDateInfo = new DateInfo(monthCalendar);
        DateInfo todayDateInfo = new DateInfo(Calendar.getInstance());

        int numWeeks = monthCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
        int daysBefore = (monthCalendar.get(Calendar.DAY_OF_WEEK) - monthCalendar.getFirstDayOfWeek());
        int totalDays = numWeeks * 7;

        if (daysBefore == -1) {
            daysBefore = 6;
        }

        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        monthCalendar.add(Calendar.DAY_OF_MONTH, -daysBefore);

        while (totalDays > 0) {
            DateInfo dateInfo = new DateInfo(monthCalendar);
            dateInfo.isDisabled = !(dateInfo.month == selectedDateInfo.month && dateInfo.year == selectedDateInfo.year);
            dateInfo.isHighlighted = dateInfo.equals(todayDateInfo);
            dateInfo.isAccomplished = (dateInfo.before(todayDateInfo) || dateInfo.equals(todayDateInfo)) &&
                    DayGoal.hasAccomplished(dateInfo.dayOfMonth, dateInfo.month, dateInfo.year);

            datesList.add(dateInfo);
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
            totalDays--;
        }

        return datesList;
    }

    @SuppressWarnings("Convert2streamapi")
    private void populateLayout(List<DateInfo> datesList, LinearLayout rootLayout) {
        new Thread(() -> {
            int numWeeks = datesList.size() / 7;

            for (int weekIndex = 0; weekIndex < numWeeks; weekIndex++) {
                LinearLayout rowLinearLayout = (LinearLayout) mLayoutInflater.inflate(R.layout.calendar_row, rootLayout, false);

                for (int j = 0; j < 7; j++) {
                    int dateIndex = weekIndex * 7 + j;
                    addCell(rowLinearLayout, datesList.get(dateIndex));
                }

                rootLayout.post(() -> rootLayout.addView(rowLinearLayout));
            }
        }).start();
    }

    private void addCell(LinearLayout parent, DateInfo dateInfo) {
        RelativeLayout wrapper = (RelativeLayout) mLayoutInflater.inflate(R.layout.calendar_cell, parent, false);
        TextView textView = (TextView) wrapper.findViewById(R.id.text);
        textView.setText(String.valueOf(dateInfo.dayOfMonth));

        textView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHoverCardCallbacks.showHoverCard(
                            event.getRawX() - (event.getX() - v.getWidth() / 2),
                            event.getRawY() - (event.getY() - v.getHeight() / 2),
                            dateInfo);
                    break;

                case MotionEvent.ACTION_UP:
                    mHoverCardCallbacks.hideHoverCard();
                    break;
            }

            return false;
        });

        if (dateInfo.isDisabled) {
            textView.setTextColor(getResources().getColor(R.color.disabledText));
        }

        if (dateInfo.isHighlighted) {
            mHighlightedCellText = textView;

            if (dateInfo.isAccomplished) {
                textView.setBackgroundResource(R.drawable.bg_calendar_cell_today_filled);
                textView.setTextColor(getResources().getColor(R.color.primaryDark));
            } else {
                textView.setBackgroundResource(R.drawable.bg_calendar_cell_today);
            }

            if (dateInfo.isDisabled) {
                textView.getBackground().setAlpha(128);
            }
        } else if (dateInfo.isAccomplished) {
            View coinView = wrapper.findViewById(R.id.coin);
            coinView.setVisibility(View.VISIBLE);

            if (dateInfo.isDisabled) {
                coinView.setAlpha(0.5f);
            }
        }

        parent.addView(wrapper);
    }

    public void updateHighlightedCells() {
        if (mHighlightedCellText != null) {
            mHighlightedCellText.setBackgroundResource(R.drawable.bg_calendar_cell_today_filled);
            mHighlightedCellText.setTextColor(getResources().getColor(R.color.primaryDark));
        }
    }

    public static interface HoverCardCallbacks {

        /**
         * Called when a date is touched to show the hover card.
         *
         * @param x        The x coordinate of the touch.
         * @param y        The y coordinate of the touch.
         * @param dateInfo A DateInfo object for the touched date.
         */
        public void showHoverCard(float x, float y, DateInfo dateInfo);

        /**
         * Called when a date is no longer being touched to hide the hover card.
         */
        public void hideHoverCard();

    }

}
