package com.aluxian.nonzeroday.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.aluxian.nonzeroday.MainActivity;
import com.aluxian.nonzeroday.R;
import com.aluxian.nonzeroday.adapters.MonthsPagerAdapter;
import com.aluxian.nonzeroday.models.DateInfo;
import com.aluxian.nonzeroday.models.DayGoal;
import com.aluxian.nonzeroday.utils.Async;
import com.aluxian.nonzeroday.utils.Dp;
import com.aluxian.nonzeroday.views.ContentAwareViewPager;
import com.devspark.robototextview.util.RobotoTypefaceManager;

import java.util.Calendar;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class HistoryFragment extends Fragment implements MonthFragment.HoverCardCallbacks, ContentAwareViewPager.SwipeListener {

    /** The number of months displayed in the calendar widget. */
    public static final int MONTHS_COUNT = 600; // ~50 years

    @InjectView(R.id.month_name) TextView mCalendarTitleTextView;
    @InjectView(R.id.hover_card) TextView mHoverCardView;
    @InjectView(R.id.text_streak) TextView mStreakTextView;
    @InjectView(R.id.month_pager) VerticalViewPager mMonthPager;

    private String[] mStreakVariants;
    private String[] mMonthNames;

    private long mLastPageChange;
    private boolean mHoverCardShown;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Extract resources
        mStreakVariants = getResources().getStringArray(R.array.streak_variants);
        mMonthNames = getActivity().getResources().getStringArray(R.array.months);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.inject(this, rootView);

        // Set the calendar title
        mCalendarTitleTextView.setText(generateCalendarTitle(Calendar.getInstance()));

        // Set up the week days strip
        String[] weekDays = getActivity().getResources().getStringArray(R.array.week_days);
        GridView weekDaysGrid = (GridView) rootView.findViewById(R.id.week_days);
        weekDaysGrid.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.calendar_week_day, weekDays));

        // Create the month ViewPager of the calendar view
        mMonthPager.setAdapter(new MonthsPagerAdapter(getChildFragmentManager()));
        mMonthPager.setOnPageChangeListener(new OnPageChangeListener());
        mMonthPager.setCurrentItem(MONTHS_COUNT / 2);

        // Delay adding more pages
        mMonthPager.postDelayed(() -> mMonthPager.setOffscreenPageLimit(2), 500);
        mMonthPager.postDelayed(() -> mMonthPager.setOffscreenPageLimit(3), 750);

        ((MainActivity) getActivity()).setSwipeListener(this);
        updateStreakText();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.btn_previous)
    void buttonClickPrevious() {
        if (System.currentTimeMillis() - mLastPageChange > 200) {
            mMonthPager.setCurrentItem(mMonthPager.getCurrentItem() - 1, true);
            mLastPageChange = System.currentTimeMillis();
        }
    }

    @OnClick(R.id.btn_next)
    void buttonClickNext() {
        if (System.currentTimeMillis() - mLastPageChange > 200) {
            mMonthPager.setCurrentItem(mMonthPager.getCurrentItem() + 1, true);
            mLastPageChange = System.currentTimeMillis();
        }
    }

    /**
     * Retrieves the streak from the database and updates its TextView.
     */
    public void updateStreakText() {
        Async.run(DayGoal::getStreak, (streak) -> {
            switch (streak) {
                case 0:
                    mStreakTextView.setText(R.string.streak_nothing);
                    break;

                case 1:
                    mStreakTextView.setText(R.string.streak_one_day);
                    break;

                default:
                    mStreakTextView.setText(String.format(mStreakVariants[new Random().nextInt(mStreakVariants.length)], streak));
                    break;
            }
        });
    }

    /**
     * Updates the background and the text color of the currently highlighted cell.
     */
    @SuppressWarnings("Convert2streamapi")
    public void updateHighlightedCells() {
        int currentItem = mMonthPager.getCurrentItem();
        int pageLimit = mMonthPager.getOffscreenPageLimit();

        for (int i = -pageLimit; i <= pageLimit; i++) {
            MonthFragment fragment = (MonthFragment) mMonthPager.getAdapter().instantiateItem(mMonthPager, currentItem + i);

            if (fragment != null) {
                fragment.updateHighlightedCells();
            }
        }
    }

    /**
     * Generates a title for the calendar based on the given Calendar.
     *
     * @param calendar A Calendar instance to use.
     * @return The generated title.
     */
    private String generateCalendarTitle(Calendar calendar) {
        return mMonthNames[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
    }

    @Override
    public void showHoverCard(float x, float y, DateInfo dateInfo) {
        if (mHoverCardShown) {
            return;
        }

        mHoverCardShown = true;

        Async.run(() -> DayGoal.getForDate(dateInfo.dayOfMonth, dateInfo.month, dateInfo.year), (dayGoal) -> {
            if (dayGoal != null) {
                mHoverCardView.setText(dayGoal.description);
                mHoverCardView.setTypeface(
                        RobotoTypefaceManager.obtainTypeface(getActivity(), RobotoTypefaceManager.Typeface.ROBOTO_SLAB_BOLD));
//            } else if (dateInfo.before(new DateInfo(Calendar.getInstance()))) {
//                mHoverCardView.setText(R.string.hover_no_goal);
//            } else {
//                mHoverCardView.setText(R.string.hover_future);
//            }
            } else {
                mHoverCardView.setText(R.string.hover_no_goal);
                mHoverCardView.setTypeface(
                        RobotoTypefaceManager.obtainTypeface(getActivity(), RobotoTypefaceManager.Typeface.ROBOTO_SLAB_REGULAR));
            }

            mHoverCardView.post(() -> {
                int marginPx = Dp.toPx(16);
                int screenWidth = getResources().getDisplayMetrics().widthPixels;

                // Set position above the user's finger
                mHoverCardView.setX(x - mHoverCardView.getWidth() / 2);
                mHoverCardView.setY(y - Dp.toPx(56) - mHoverCardView.getHeight());

                // The card is too much to the right
                if (mHoverCardView.getX() + mHoverCardView.getWidth() + marginPx > screenWidth) {
                    mHoverCardView.setX(screenWidth - marginPx - mHoverCardView.getWidth());
                }

                // The card is too much to the left
                if (mHoverCardView.getX() < marginPx) {
                    mHoverCardView.setX(marginPx);
                }
            });

            mHoverCardView.animate().alpha(1).setDuration(100);
        });
    }

    @Override
    public void hideHoverCard() {
        mHoverCardShown = false;
        mHoverCardView.animate().alpha(0).setDuration(200);
    }

    @Override
    public void isSwipingAway() {
        if (mHoverCardShown) {
            hideHoverCard();
        }
    }

    private class OnPageChangeListener implements ViewPager.OnPageChangeListener {

        /** Store the previous position of the selected page. */
        private int mPreviousPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            int offset = position - MONTHS_COUNT / 2;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, offset);

            // Update the title of the calendar widget; skip animation the first time
            if (mPreviousPosition == -1) {
                mCalendarTitleTextView.setText(generateCalendarTitle(calendar));
            } else {
                TitleAnimation titleAnimation = position > mPreviousPosition ? TitleAnimation.UP : TitleAnimation.DOWN;
                Animation animation = AnimationUtils.loadAnimation(getActivity(), titleAnimation.anim1);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mCalendarTitleTextView.setText(generateCalendarTitle(calendar));
                        mCalendarTitleTextView.startAnimation(AnimationUtils.loadAnimation(getActivity(), titleAnimation.anim2));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                mCalendarTitleTextView.startAnimation(animation);
            }

            mPreviousPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {}

    }

    /**
     * Stores animation configurations for the title TextView.
     */
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
