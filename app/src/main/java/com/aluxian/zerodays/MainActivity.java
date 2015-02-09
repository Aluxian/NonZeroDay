package com.aluxian.zerodays;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aluxian.zerodays.adapters.GoalsPagerAdapter;
import com.aluxian.zerodays.adapters.MainPagerAdapter;
import com.aluxian.zerodays.fragments.HistoryFragment;
import com.aluxian.zerodays.fragments.InputFragment;
import com.aluxian.zerodays.fragments.MainFragment;
import com.aluxian.zerodays.models.DayGoal;
import com.aluxian.zerodays.models.YearGoal;
import com.aluxian.zerodays.utils.Async;
import com.aluxian.zerodays.views.ContentAwareViewPager;
import com.aluxian.zerodays.views.ContentAwareViewPagerCompat;
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
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        mGoalsViewPager.setAdapter(new GoalsPagerAdapter(getSupportFragmentManager()));

        mMainViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mMainViewPager.setOffscreenPageLimit(3);
        mMainViewPager.setCurrentItem(1);

        mPageIndicator.setViewPager(mMainViewPager);

        Async.run(DayGoal::getCountForToday, (countToday) -> {
            if (countToday > 0) {
                mMainViewPager.post(() -> {
                    mMainViewPager.setVisibility(View.VISIBLE);
                    mMainViewPager.animate().setStartDelay(200).alpha(1);

                    mPageIndicator.setVisibility(View.VISIBLE);
                    mPageIndicator.animate().setStartDelay(200).alpha(1);
                });
            } else {
                mGoalsViewPager.post(() -> {
                    mGoalsViewPager.setVisibility(View.VISIBLE);
                    mGoalsViewPager.animate().setStartDelay(200).alpha(1);
                });

                // If a goal for the year is already set, move to the day goal input screen
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
                mGoalsViewPager.setCurrentItem(1); // move to today's goal fragment
                break;

            case DAY:
                mGoalsViewPager.setCurrentItem(2); // move to empty fragment
                mGoalsViewPager.postDelayed(() -> mGoalsViewPager.setVisibility(View.GONE), 1000);

                mPageIndicator.setVisibility(View.VISIBLE);
                mPageIndicator.setTranslationY(mPageIndicator.getHeight());
                mPageIndicator.animate().alpha(1).translationY(0);

                mMainViewPager.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void setContentAwareViewPagerCallbacks(ContentAwareViewPager.SwipeListener swipeListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ContentAwareViewPager) mMainViewPager).setCallbacks(swipeListener);
        } else {
            ((ContentAwareViewPagerCompat) mMainViewPager).setCallbacks(swipeListener);
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
        HistoryFragment historyFragment = (HistoryFragment) getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.main_pager + ":0");
        historyFragment.updateStreakText();
    }

}
