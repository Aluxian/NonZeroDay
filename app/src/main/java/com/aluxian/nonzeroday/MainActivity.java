package com.aluxian.nonzeroday;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aluxian.nonzeroday.adapters.GoalsPagerAdapter;
import com.aluxian.nonzeroday.adapters.MainPagerAdapter;
import com.aluxian.nonzeroday.fragments.HistoryFragment;
import com.aluxian.nonzeroday.fragments.InputFragment;
import com.aluxian.nonzeroday.fragments.MainFragment;
import com.aluxian.nonzeroday.models.DayGoal;
import com.aluxian.nonzeroday.models.YearGoal;
import com.aluxian.nonzeroday.utils.Async;
import com.aluxian.nonzeroday.views.ContentAwareViewPager;
import com.aluxian.nonzeroday.views.ContentAwareViewPagerCompat;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends FragmentActivity
        implements MainFragment.DismissListener, MainFragment.SwipeListener, InputFragment.NextButtonListener {

    /** The ViewPager that displays the goal input fragments. */
    @InjectView(R.id.goals_pager) ViewPager mGoalsViewPager;

    /** The main ViewPager that displays the 3 fragments: history, status and active goals. */
    @InjectView(R.id.main_pager) ViewPager mMainViewPager;

    /** The page indicator shown at the bottom, linked to the main ViewPager. */
    @InjectView(R.id.indicator) CirclePageIndicator mPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // Set the strict mode policy
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
//        }

        // Set up the view pagers
        mGoalsViewPager.setAdapter(new GoalsPagerAdapter(getSupportFragmentManager()));
        mMainViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mMainViewPager.setOffscreenPageLimit(3);
        mMainViewPager.setCurrentItem(1);
        mPageIndicator.setViewPager(mMainViewPager);

        // Check if there's a goal set for today and display the appropriate view pager
        Async.run(DayGoal::getCountForToday, (countToday) -> {
            if (countToday > 0) {
                mMainViewPager.setVisibility(View.VISIBLE);
                mPageIndicator.setVisibility(View.VISIBLE);

                mMainViewPager.post(() -> mMainViewPager.animate().setStartDelay(200).alpha(1));
                mPageIndicator.post(() -> mPageIndicator.animate().setStartDelay(200).alpha(1));
            } else {
                mGoalsViewPager.setVisibility(View.VISIBLE);
                mGoalsViewPager.post(() -> mGoalsViewPager.animate().setStartDelay(200).alpha(1));

                // If a goal for the year is already set, move to the input screen for today's goal
                Async.run(YearGoal::getCountForThisYear, (countYear) -> {
                    if (countYear > 0) {
                        mGoalsViewPager.setCurrentItem(1);
                    }
                });
            }
        });
    }

    @Override
    public void onGoalInputNextClicked(InputFragment.Type type) {
        switch (type) {
            case YEAR:
                mGoalsViewPager.setCurrentItem(1); // move to today's goal input fragment
                break;

            case DAY:
                mGoalsViewPager.setCurrentItem(2); // move to the empty fragment
                mGoalsViewPager.postDelayed(() -> mGoalsViewPager.setVisibility(View.GONE), 1000);

                mPageIndicator.setVisibility(View.VISIBLE);
                mPageIndicator.setTranslationY(mPageIndicator.getHeight());
                mPageIndicator.animate().alpha(1).translationY(0);

                mMainViewPager.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void canSwipe(boolean canSwipe) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ContentAwareViewPager) mMainViewPager).setSwipingEnabled(canSwipe);
        } else {
            ((ContentAwareViewPagerCompat) mMainViewPager).setSwipingEnabled(canSwipe);
        }
    }

    @Override
    public void zeroDayDismissed() {
        HistoryFragment historyFragment = getHistoryFragment();
        historyFragment.updateStreakText();
        historyFragment.updateHighlightedCells();
    }

    public void setSwipeListener(ContentAwareViewPager.SwipeListener swipeListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ContentAwareViewPager) mMainViewPager).setSwipeListener(swipeListener);
        } else {
            ((ContentAwareViewPagerCompat) mMainViewPager).setSwipeListener(swipeListener);
        }
    }

    /**
     * @return An instance of HistoryFragment from the main view pager.
     */
    private HistoryFragment getHistoryFragment() {
        return (HistoryFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.main_pager + ":0");
    }

}
