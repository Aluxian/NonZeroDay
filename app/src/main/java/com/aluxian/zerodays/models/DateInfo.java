package com.aluxian.zerodays.models;

import java.util.Calendar;

public final class DateInfo {

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

    /**
     * @param dateInfo A DateInfo object that represents the date for comparison.
     * @return Whether this instance represents a date in time before the given one.
     */
    public boolean before(DateInfo dateInfo) {
        return year < dateInfo.year
                || year == dateInfo.year && month < dateInfo.month
                || year == dateInfo.year && month == dateInfo.month && dayOfMonth < dateInfo.dayOfMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DateInfo) {
            DateInfo info = (DateInfo) o;
            return info.dayOfMonth == dayOfMonth && info.month == month && info.year == year;
        }

        return super.equals(o);
    }

}
