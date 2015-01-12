package com.aluxian.zerodays.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aluxian.zerodays.fragments.goals.DayGoalFragment;
import com.aluxian.zerodays.fragments.goals.EmptyFragment;
import com.aluxian.zerodays.fragments.goals.YearGoalFragment;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the goal pages.
 */
public class GoalsPagerAdapter extends FragmentPagerAdapter {

    public GoalsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new YearGoalFragment();
            case 1:
                return new DayGoalFragment();
            case 2:
                return new EmptyFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }

}
