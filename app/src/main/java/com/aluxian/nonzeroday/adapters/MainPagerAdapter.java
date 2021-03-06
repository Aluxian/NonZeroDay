package com.aluxian.nonzeroday.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aluxian.nonzeroday.fragments.GoalsFragment;
import com.aluxian.nonzeroday.fragments.HistoryFragment;
import com.aluxian.nonzeroday.fragments.MainFragment;

/**
 * A PagerAdapter that returns a fragment corresponding to one of the main pages.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HistoryFragment();
            case 1:
                return new MainFragment();
            case 2:
                return new GoalsFragment();
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
