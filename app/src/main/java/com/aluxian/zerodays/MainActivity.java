package com.aluxian.zerodays;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;

import com.aluxian.zerodays.adapters.GoalsPagerAdapter;
import com.aluxian.zerodays.adapters.MainPagerAdapter;
import com.aluxian.zerodays.db.DayGoal;
import com.aluxian.zerodays.db.YearGoal;
import com.aluxian.zerodays.fragments.MiniFragment;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends FragmentActivity implements MiniFragment.Callbacks {

    private ViewPager mGoalsViewPager;
    private ViewPager mMainViewPager;

    private CirclePageIndicator mPageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoalsViewPager = (ViewPager) findViewById(R.id.goals_pager);
        mGoalsViewPager.setAdapter(new GoalsPagerAdapter(getSupportFragmentManager()));
        // goalsViewPager prevent sliding

        mMainViewPager = (ViewPager) findViewById(R.id.main_pager);
        mMainViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mMainViewPager.setCurrentItem(1);

        mPageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPageIndicator.setViewPager(mMainViewPager);

        if (DayGoal.getCountForToday() > 0) {
            mMainViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < 16) {
                        mMainViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mMainViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    mMainViewPager.setVisibility(View.VISIBLE);
                    mMainViewPager.animate().setStartDelay(200).alpha(1);

                    mPageIndicator.setVisibility(View.VISIBLE);
                    mPageIndicator.animate().setStartDelay(200).alpha(1);
                }
            });
        } else {
            mGoalsViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < 16) {
                        mGoalsViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mGoalsViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    mGoalsViewPager.setVisibility(View.VISIBLE);
                    mGoalsViewPager.animate().setStartDelay(200).alpha(1);
                }
            });

            if (YearGoal.getCountForThisYear() > 0) {
                mGoalsViewPager.setCurrentItem(1);
            }
        }
    }

    @Override
    public void onNextButtonClicked(MiniFragment.Type type) {
        switch (type) {
            case YEAR:
                mGoalsViewPager.setCurrentItem(1); // today's goal fragment
                break;

            case DAY:
                mGoalsViewPager.setCurrentItem(2); // empty fragment
                mGoalsViewPager.postDelayed(() -> mGoalsViewPager.setVisibility(View.GONE), 300);

                mPageIndicator.setVisibility(View.VISIBLE);
                mPageIndicator.animate().alpha(1).translationY(0);

                mMainViewPager.setVisibility(View.VISIBLE);
                mMainViewPager.animate().alpha(1);

                break;
        }
    }

}
