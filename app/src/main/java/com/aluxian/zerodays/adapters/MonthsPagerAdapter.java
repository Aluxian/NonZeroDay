package com.aluxian.zerodays.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aluxian.zerodays.fragments.HistoryFragment;
import com.aluxian.zerodays.fragments.MonthFragment;

import java.util.Calendar;

/**
 * A pager adapter that supplies fragments for the calendar widget.
 */
public class MonthsPagerAdapter extends FragmentStatePagerAdapter {

    public MonthsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        int offset = position - getCount() / 2;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, offset);

        return MonthFragment.newInstance(calendar.getTimeInMillis());
    }

    @Override
    public int getCount() {
        return HistoryFragment.MONTHS_COUNT;
    }

}
