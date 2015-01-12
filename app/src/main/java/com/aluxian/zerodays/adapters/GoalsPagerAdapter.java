package com.aluxian.zerodays.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aluxian.zerodays.fragments.MiniFragment;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the goal pages.
 */
public class GoalsPagerAdapter extends FragmentPagerAdapter {

    public GoalsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MiniFragment.newInstance(MiniFragment.Type.values()[position]);
    }

    @Override
    public int getCount() {
        return MiniFragment.Type.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MiniFragment.Type.values()[position].name();
    }

}
