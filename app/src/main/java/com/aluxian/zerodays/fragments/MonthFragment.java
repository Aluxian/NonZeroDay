package com.aluxian.zerodays.fragments;

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

import com.aluxian.zerodays.R;
import com.aluxian.zerodays.models.DayGoal;
import com.aluxian.zerodays.utils.Async;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthFragment extends Fragment {

    private static final String KEY_DATE_MILLIS = "date_millis";
    private Callbacks mCallbacks;

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
        mCallbacks = (Callbacks) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Calendar monthCalendar = Calendar.getInstance();
        monthCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        monthCalendar.setTimeInMillis(getArguments().getLong(KEY_DATE_MILLIS));

        LinearLayout rootLayout = (LinearLayout) inflater.inflate(R.layout.calendar_grid, container, false);
        Async.run(() -> buildList(monthCalendar), (datesList) -> populateLayout(datesList, rootLayout));

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
            dateInfo.isAccomplished = dateInfo.before(todayDateInfo) &&
                    DayGoal.hasAccomplished(dateInfo.dayOfMonth, dateInfo.month, dateInfo.year);

            datesList.add(dateInfo);
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
            totalDays--;
        }

        return datesList;
    }

    private void populateLayout(List<DateInfo> datesList, LinearLayout rootLayout) {
        int numWeeks = datesList.size() / 7;

        for (int weekIndex = 0; weekIndex < numWeeks; weekIndex++) {
            LinearLayout rowLinearLayout = (LinearLayout) LayoutInflater.from(getActivity())
                    .inflate(R.layout.calendar_row, rootLayout, false);

            for (int j = 0; j < 7; j++) {
                int dateIndex = weekIndex * 7 + j;
                addCell(rowLinearLayout, datesList.get(dateIndex));
            }

            rootLayout.addView(rowLinearLayout);
        }
    }

    private void addCell(LinearLayout parent, DateInfo dateInfo) {
        RelativeLayout wrapper = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.calendar_cell, parent, false);
        TextView textView = (TextView) wrapper.findViewById(R.id.text);
        textView.setText(String.valueOf(dateInfo.dayOfMonth));

        textView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mCallbacks.showHoverCard(
                            event.getRawX() - (event.getX() - v.getWidth() / 2),
                            event.getRawY() - (event.getY() - v.getHeight() / 2),
                            dateInfo);
                    break;

                case MotionEvent.ACTION_UP:
                    mCallbacks.hideHoverCard();
                    break;
            }

            return false;
        });

        if (dateInfo.isDisabled) {
            textView.setTextColor(getActivity().getResources().getColor(R.color.disabledText));
        }

        if (dateInfo.isHighlighted) {
            textView.setBackgroundResource(R.drawable.bg_calendar_cell_today);
            textView.setTextColor(getResources().getColor(R.color.primaryDark));

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

    public static final class DateInfo {

        public final int dayOfMonth;
        public final int month;
        public final int year;

        public boolean isDisabled;
        public boolean isHighlighted;
        public boolean isAccomplished;

        public DateInfo(Calendar date) {
            dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
            month = date.get(Calendar.MONTH);
            year = date.get(Calendar.YEAR);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DateInfo) {
                DateInfo info = (DateInfo) o;
                return info.dayOfMonth == dayOfMonth && info.month == month && info.year == year;
            }

            return super.equals(o);
        }

        /**
         * @param dateInfo A DateInfo object that represents the date for comparison.
         * @return Whether this instance represents a date in time before the given one.
         */
        public boolean before(DateInfo dateInfo) {
            return year < dateInfo.year
                    || year == dateInfo.year && month < dateInfo.month
                    || year == dateInfo.year && month == dateInfo.month && dayOfMonth < dateInfo.dayOfMonth;
        }

    }

    public static interface Callbacks {

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
