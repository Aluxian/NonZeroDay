package com.aluxian.zerodays.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.aluxian.zerodays.MainActivity;
import com.aluxian.zerodays.R;
import com.aluxian.zerodays.models.DayGoal;
import com.aluxian.zerodays.utils.AnimationEndListener;
import com.aluxian.zerodays.utils.Async;
import com.aluxian.zerodays.utils.DpConverter;

import java.util.Calendar;
import java.util.Random;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class HistoryFragment extends Fragment implements MonthFragment.Callbacks {

    private static final String PREF_LONGEST_STREAK = "longest_streak";
    private static final int MONTHS_COUNT = 2400; // ~200 years

    private TextView mCalendarTitleTextView;
    private TextView mHoverCardView;
    private TextView mStreakTextView;

    private String[] mStreakVariants;
    private String[] mMonthNames;

    private SharedPreferences mSharedPrefs;
    private int mScreenWidth;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);

        // Calculate the width of the screen
        Point screenSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(screenSize);
        mScreenWidth = screenSize.x;

        // Extract resources
        mStreakVariants = getResources().getStringArray(R.array.streak_variants);
        mMonthNames = getActivity().getResources().getStringArray(R.array.months);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Find views
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        mCalendarTitleTextView = (TextView) rootView.findViewById(R.id.month_name);
        mStreakTextView = (TextView) rootView.findViewById(R.id.text_streak);
        mHoverCardView = (TextView) rootView.findViewById(R.id.hover_card);

        // Set the calendar title
        mCalendarTitleTextView.setText(getCalendarTitle(Calendar.getInstance()));

        // Set up the week days strip
        String[] weekDays = getActivity().getResources().getStringArray(R.array.week_days);
        GridView weekDaysGrid = (GridView) rootView.findViewById(R.id.week_days);
        weekDaysGrid.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.calendar_week_day, weekDays));

        // Create the month ViewPager of the calendar view
        VerticalViewPager monthPager = (VerticalViewPager) rootView.findViewById(R.id.month_pager);
        monthPager.setAdapter(new MonthsPagerAdapter(getChildFragmentManager()));
        monthPager.setOnPageChangeListener(new OnPageChangeListener());
        monthPager.setCurrentItem(MONTHS_COUNT / 2);

        // Set the click listeners for the calendar buttons
        rootView.findViewById(R.id.btn_previous)
                .setOnClickListener(v -> monthPager.setCurrentItem(monthPager.getCurrentItem() - 1, true));
        rootView.findViewById(R.id.btn_next)
                .setOnClickListener(v -> monthPager.setCurrentItem(monthPager.getCurrentItem() + 1, true));

        updateStreakText();
        return rootView;
    }

    private void updateStreakText() {
        Async.run(DayGoal::getStreak, (streak) -> {
            int longestStreak = mSharedPrefs.getInt(PREF_LONGEST_STREAK, 0);

            switch (streak) {
                case 0:
                    if (longestStreak > 0) {
                        mStreakTextView.setText(getResources().getString(R.string.streak_longest, longestStreak));
                    } else {
                        mStreakTextView.setText(R.string.streak_nothing);
                    }

                    break;

                case 1:
                    mStreakTextView.setText(R.string.streak_one_day);
                    break;

                default:
                    mStreakTextView.setText(String.format(mStreakVariants[new Random().nextInt(mStreakVariants.length)], streak));
                    break;
            }

            if (streak > longestStreak) {
                mSharedPrefs.edit().putInt(PREF_LONGEST_STREAK, streak).apply();
            }
        });
    }

    private String getCalendarTitle(Calendar calendar) {
        return mMonthNames[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
    }

    @Override
    public void showHoverCard(float x, float y, MonthFragment.DateInfo dateInfo) {
        ((MainActivity) getActivity()).canSwipe(false);

        Async.run(() -> DayGoal.getForDate(dateInfo.dayOfMonth, dateInfo.month, dateInfo.year), (dayGoal) -> {
            if (dayGoal != null) {
                mHoverCardView.setText(dayGoal.description);
            } else {
                mHoverCardView.setText(R.string.hover_nothing);
            }

            mHoverCardView.post(() -> {
                DpConverter dp = new DpConverter(getActivity());
                int marginPx = dp.toPx(16);

                mHoverCardView.setX(x - mHoverCardView.getWidth() / 2);
                mHoverCardView.setY(y - dp.toPx(56) - mHoverCardView.getHeight());

                if (mHoverCardView.getX() + mHoverCardView.getWidth() + marginPx > mScreenWidth) {
                    mHoverCardView.setX(mScreenWidth - marginPx - mHoverCardView.getWidth());
                }

                if (mHoverCardView.getX() < marginPx) {
                    mHoverCardView.setX(marginPx);
                }
            });

            mHoverCardView.animate().alpha(1).setDuration(100);
        });
    }

    @Override
    public void hideHoverCard() {
        ((MainActivity) getActivity()).canSwipe(true);
        mHoverCardView.animate().alpha(0).setDuration(200);
    }

    private class MonthsPagerAdapter extends FragmentStatePagerAdapter {

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
            return MONTHS_COUNT;
        }

    }

    private class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        private int mPreviousPage;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            int offset = position - MONTHS_COUNT / 2;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, offset);

            if (TextUtils.isEmpty(mCalendarTitleTextView.getText())) {
                mCalendarTitleTextView.setText(getCalendarTitle(calendar));
            } else {
                TitleAnimation titleAnimation = position > mPreviousPage ? TitleAnimation.UP : TitleAnimation.DOWN;
                Animation animation = AnimationUtils.loadAnimation(getActivity(), titleAnimation.anim1);
                animation.setAnimationListener(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mCalendarTitleTextView.setText(getCalendarTitle(calendar));
                        mCalendarTitleTextView.startAnimation(AnimationUtils.loadAnimation(getActivity(), titleAnimation.anim2));
                    }
                });

                mCalendarTitleTextView.startAnimation(animation);
            }

            mPreviousPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {}

    }

    private static enum TitleAnimation {
        UP(R.anim.fade_up_above, R.anim.fade_up_below),
        DOWN(R.anim.fade_down_below, R.anim.fade_down_above);

        public final int anim1;
        public final int anim2;

        TitleAnimation(int anim1, int anim2) {
            this.anim1 = anim1;
            this.anim2 = anim2;
        }
    }

}
