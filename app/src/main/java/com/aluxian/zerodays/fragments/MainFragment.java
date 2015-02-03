package com.aluxian.zerodays.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;

import com.aluxian.zerodays.MainActivity;
import com.aluxian.zerodays.R;
import com.aluxian.zerodays.models.DayGoal;
import com.aluxian.zerodays.utils.Async;

public class MainFragment extends Fragment implements View.OnTouchListener {

    private View mZeroDayView;
    private View mShadowView;
    private View mDoneView;

    private float mPreviousX;
    private float mPreviousY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mZeroDayView = rootView.findViewById(R.id.text_zeroday);
        mDoneView = rootView.findViewById(R.id.text_nonzeroday);
        mShadowView = rootView.findViewById(R.id.shadow);

        Async.run(DayGoal::getForToday, (dayGoal) -> {
            if (dayGoal.accomplished) {
                mDoneView.setVisibility(View.VISIBLE);
                mDoneView.animate().alpha(1f);
            } else {
                mZeroDayView.setOnTouchListener(this);
                mZeroDayView.setVisibility(View.VISIBLE);
                mZeroDayView.animate().alpha(1f);
            }
        });

        return rootView;
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
                ((MainActivity) getActivity()).canSwipe(true);
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

                    mDoneView.setVisibility(View.VISIBLE);
                    mDoneView.setScaleX(0);
                    mDoneView.setScaleY(0);

                    mDoneView.animate()
                            .setStartDelay(500)
                            .setInterpolator(new OvershootInterpolator())
                            .scaleX(1)
                            .scaleY(1)
                            .alpha(1);

                    ((MainActivity) getActivity()).getHistoryFragment().updateStreakText();
                    Async.run(DayGoal::setAccomplishedToday);
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
                ((MainActivity) getActivity()).canSwipe(false);
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

}
