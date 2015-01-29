package com.aluxian.zerodays.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.aluxian.zerodays.R;
import com.aluxian.zerodays.models.DayGoal;
import com.aluxian.zerodays.utils.AnimationEndListener;
import com.aluxian.zerodays.utils.DpConverter;

import java.util.Calendar;
import java.util.Random;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class HistoryFragment extends Fragment implements MonthFragment.Callbacks {

    private static final String PREF_LONGEST_STREAK = "longest_streak";
    private static final int MONTHS_COUNT = 2400; // ~200 years

    private TextView mHoverCard;
    //private View mHoverCardArrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        TextView monthName = (TextView) rootView.findViewById(R.id.month_name);
        String[] monthNames = getActivity().getResources().getStringArray(R.array.months);

        mHoverCard = (TextView) rootView.findViewById(R.id.hover_card);
        //mHoverCardArrow = rootView.findViewById(R.id.hover_card_arrow);

        Calendar cal = Calendar.getInstance();
        monthName.setText(monthNames[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));

        String[] weekDays = getActivity().getResources().getStringArray(R.array.week_days);
        GridView weekDaysGrid = (GridView) rootView.findViewById(R.id.week_days);
        weekDaysGrid.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.calendar_week_day, weekDays));

        VerticalViewPager monthPager = (VerticalViewPager) rootView.findViewById(R.id.month_pager);
        monthPager.setAdapter(new MonthsPagerAdapter(getChildFragmentManager()));

        monthPager.setOnPageChangeListener(new OnPageChangeListener(getActivity(), monthName, monthNames));
        monthPager.setCurrentItem(MONTHS_COUNT / 2);

        rootView.findViewById(R.id.btn_previous)
                .setOnClickListener(v -> monthPager.setCurrentItem(monthPager.getCurrentItem() - 1, true));
        rootView.findViewById(R.id.btn_next)
                .setOnClickListener(v -> monthPager.setCurrentItem(monthPager.getCurrentItem() + 1, true));

        TextView streakText = (TextView) rootView.findViewById(R.id.text_streak);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        int streak = DayGoal.getStreak();
        int longestStreak = sharedPrefs.getInt(PREF_LONGEST_STREAK, 0);

        switch (streak) {
            case 0:
                if (longestStreak > 0) {
                    streakText.setText(getResources().getString(R.string.streak_longest, longestStreak));
                } else {
                    streakText.setText(R.string.streak_nothing);
                }

                break;

            case 1:
                streakText.setText(R.string.streak_one_day);
                break;

            default:
                String[] streakVariants = getResources().getStringArray(R.array.streak_variants);
                streakText.setText(String.format(streakVariants[new Random().nextInt(streakVariants.length)], streak));
        }

        if (streak > longestStreak) {
            sharedPrefs.edit().putInt(PREF_LONGEST_STREAK, streak).apply();
        }

        return rootView;
    }

    @Override
    public void showHoverCard(float x, float y, MonthFragment.DateInfo dateInfo) {
        DayGoal dayGoal = DayGoal.getForDate(dateInfo.dayOfMonth, dateInfo.month, dateInfo.year);

        if (dayGoal != null) {
            mHoverCard.setText(dayGoal.description);
        } else {
            mHoverCard.setText(R.string.hover_nothing);
        }

        mHoverCard.post(() -> {
            DpConverter dp = new DpConverter(getActivity());
            int marginPx = dp.toPx(16);

            mHoverCard.setX(x - mHoverCard.getWidth() / 2);
            mHoverCard.setY(y - dp.toPx(24 + 56 + 24 - 6));

            Point screenSize = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(screenSize);

            if (mHoverCard.getX() + mHoverCard.getWidth() + marginPx > screenSize.x) {
                mHoverCard.setX(screenSize.x - marginPx - mHoverCard.getWidth());
            }

            if (mHoverCard.getX() < marginPx) {
                mHoverCard.setX(marginPx);
            }

//            mHoverCardArrow.setX(mHoverCard.getX() + mHoverCard.getWidth() / 2 - mHoverCardArrow.getWidth() / 2);
//            mHoverCardArrow.setY(mHoverCard.getY() + mHoverCard.getHeight());
        });

        mHoverCard.animate().alpha(1).setDuration(100);
        //mHoverCardArrow.animate().alpha(1).setDuration(100);
    }

    @Override
    public void hideHoverCard() {
        mHoverCard.animate().alpha(0).setDuration(200);
        //mHoverCardArrow.animate().alpha(0).setDuration(200);
    }

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
            return MONTHS_COUNT;
        }

    }

    private static class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        private Context mContext;
        private TextView mMonthName;
        private String[] mMonthNames;
        private int mPreviousPage;

        public OnPageChangeListener(Context context, TextView monthName, String[] monthNames) {
            mContext = context;
            mMonthName = monthName;
            mMonthNames = monthNames;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            int offset = position - MONTHS_COUNT / 2;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, offset);

            if (mMonthName.length() == 0) {
                mMonthName.setText(mMonthNames[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
            } else {
                Animation animation;

                if (position > mPreviousPage) {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_up_above);
                    animation.setAnimationListener(new AnimationEndListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mMonthName.setText(mMonthNames[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
                            mMonthName.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_up_below));
                        }
                    });
                } else {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_down_below);
                    animation.setAnimationListener(new AnimationEndListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mMonthName.setText(mMonthNames[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));
                            mMonthName.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_down_above));
                        }
                    });
                }

                mMonthName.startAnimation(animation);
            }

            mPreviousPage = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {}

    }

}
