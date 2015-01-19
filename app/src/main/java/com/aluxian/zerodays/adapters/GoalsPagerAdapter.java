package com.aluxian.zerodays.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aluxian.zerodays.fragments.InputFragment;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the goal pages.
 */
public class GoalsPagerAdapter extends FragmentPagerAdapter {

    public GoalsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return InputFragment.newInstance(InputFragment.Type.values()[position]);
    }

    @Override
    public int getCount() {
        return InputFragment.Type.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return InputFragment.Type.values()[position].name();
    }

}
