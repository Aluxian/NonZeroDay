package com.aluxian.zerodays.fragments;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;

import com.aluxian.zerodays.R;
import com.aluxian.zerodays.models.DayGoal;
import com.aluxian.zerodays.utils.Async;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainFragment extends Fragment implements View.OnTouchListener {

    @InjectView(R.id.text_zeroday) View mZeroDayView;
    @InjectView(R.id.text_nonzeroday) View mNonZeroDayView;
    @InjectView(R.id.shadow) View mShadowView;

    private float mPreviousX;
    private float mPreviousY;

    private DismissListener mDismissListener;
    private SwipeListener mSwipeListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mDismissListener = (DismissListener) activity;
        mSwipeListener = (SwipeListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);

        Async.run(DayGoal::getForToday, (dayGoal) -> {
            if (dayGoal.accomplished) {
                mNonZeroDayView.setVisibility(View.VISIBLE);
                mNonZeroDayView.post(() -> mNonZeroDayView.animate().alpha(1f));
            } else {
                mZeroDayView.setOnTouchListener(this);
                mZeroDayView.setVisibility(View.VISIBLE);
                mZeroDayView.post(() -> mZeroDayView.animate().alpha(1f));
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mPreviousX > 0) {
                    mZeroDayView.setX(mZeroDayView.getX() + event.getRawX() - mPreviousX);
                    mZeroDayView.setY(mZeroDayView.getY() + event.getRawY() - mPreviousY);
                }

                mPreviousX = event.getRawX();
                mPreviousY = event.getRawY();

                break;

            case MotionEvent.ACTION_UP:
                mSwipeListener.canSwipe(true);
                mShadowView.animate().alpha(0);

                Point screenSize = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(screenSize);

                if (event.getRawY() > screenSize.y * 3 / 4) {
                    mZeroDayView.animate()
                            .setInterpolator(new AnticipateOvershootInterpolator())
                            .setDuration(1000)
                            .translationX(0)
                            .translationY(screenSize.y)
                            .scaleX(0.5f)
                            .scaleY(0.5f);

                    mNonZeroDayView.setVisibility(View.VISIBLE);
                    mNonZeroDayView.setScaleX(0);
                    mNonZeroDayView.setScaleY(0);

                    mNonZeroDayView.animate()
                            .setStartDelay(500)
                            .setInterpolator(new OvershootInterpolator())
                            .scaleX(1)
                            .scaleY(1)
                            .alpha(1);

                    // Update the database
                    Async.run(DayGoal::setAccomplishedToday, mDismissListener::zeroDayDismissed);
                } else {
                    mZeroDayView.animate()
                            .setInterpolator(new OvershootInterpolator())
                            .translationX(0)
                            .translationY(0)
                            .scaleX(1f)
                            .scaleY(1f);
                }

                mPreviousX = 0;
                mPreviousY = 0;

                break;

            case MotionEvent.ACTION_DOWN:
                mSwipeListener.canSwipe(false);
                mShadowView.animate().alpha(1);

                mZeroDayView.animate()
                        .setInterpolator(new OvershootInterpolator())
                        .scaleX(0.9f)
                        .scaleY(0.9f);

                mPreviousX = event.getRawX();
                mPreviousY = event.getRawY();

                break;
        }

        return true;
    }

    public static interface DismissListener {

        /**
         * Called when the 'ZERODAY' text is pulled down and the day is marked as accomplished.
         */
        public void zeroDayDismissed();

    }

    public static interface SwipeListener {

        /**
         * Called to notify the MainActivity whether the main pager can be swiped.
         */
        public void canSwipe(boolean canSwipe);

    }

}
