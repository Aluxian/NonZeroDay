package com.aluxian.nonzeroday.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aluxian.nonzeroday.R;
import com.aluxian.nonzeroday.models.DayGoal;
import com.aluxian.nonzeroday.utils.Async;
import com.aluxian.nonzeroday.utils.Dp;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainFragment extends Fragment implements View.OnTouchListener {

    @InjectView(R.id.text_zeroday) View mZeroDayView;
    @InjectView(R.id.text_nonzeroday) View mNonZeroDayView;
    @InjectView(R.id.shadow) View mShadowView;

    @InjectView(R.id.coins_number) TextView mCoinsNumber;
    @InjectView(R.id.leaderboard_coins_number) TextView mCoinsNumberLeaderboard;

    @InjectView(R.id.leaderboard) LinearLayout mLeaderboardLayout;
    @InjectView(R.id.leaderboard_container) LinearLayout mLeaderboardContainer;
    @InjectView(R.id.leaderboard_trigger) View mLeaderboardTrigger;
    @InjectView(R.id.achievements) ListView mAchievementsList;

    @InjectView(R.id.coin_pocket) View mCoinPocket;
    @InjectView(R.id.big_coin) View mBigCoin;
    @InjectView(R.id.big_coin_frame) View mBigCoinFrame;

    private MediaPlayer mCoinSoundPlayer;

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

        //new Achievement("My cool achievement", 4, true, new Date()).save();

        DayGoal dayGoal = DayGoal.getForToday();
        if (dayGoal.accomplished) {
            mNonZeroDayView.setVisibility(View.VISIBLE);
            mNonZeroDayView.post(() -> mNonZeroDayView.animate().alpha(1f));
        } else {
            mZeroDayView.setOnTouchListener(this);
            mZeroDayView.setVisibility(View.VISIBLE);
            mZeroDayView.post(() -> mZeroDayView.animate().alpha(1f));
        }

        Async.run(DayGoal::getCountDaysAccomplished, days -> {
            mCoinsNumber.setText(days + " coins");
            //mCoinsNumberLeaderboard.setText(days + " coins");
        });

        //mAchievementsList.setAdapter(new AchievementsAdapter());

        mCoinSoundPlayer = MediaPlayer.create(getActivity(), R.raw.coin);

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
                    // Move the 'ZERODAY' text out of the view
                    mZeroDayView.animate()
                            .setInterpolator(new AnticipateOvershootInterpolator(2.5f))
                            .setDuration(1000)
                            .translationX(0)
                            .translationY(screenSize.y)
                            .scaleX(0.5f)
                            .scaleY(0.5f);

                    mCoinPocket.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setStartDelay(500)
                            .setDuration(400)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {}

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    int width = Resources.getSystem().getDisplayMetrics().widthPixels * 5 / 8;
                                    ValueAnimator animator = ValueAnimator.ofFloat(mCoinPocket.getWidth(), width);

                                    animator.addUpdateListener(anim -> {
                                        mCoinPocket.getLayoutParams().width = (int) Math.floor((Float) anim.getAnimatedValue());
                                        mCoinPocket.requestLayout();
                                    });

                                    animator.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {}

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            mBigCoin.animate()
                                                    .setInterpolator(new OvershootInterpolator(1.5f))
                                                    .translationY(-mBigCoin.getHeight() / 3)
                                                    .setDuration(700)
                                                    .setListener(new Animator.AnimatorListener() {
                                                        @Override
                                                        public void onAnimationStart(Animator animation) {
                                                            mCoinSoundPlayer.seekTo(0);
                                                            mCoinSoundPlayer.start();
                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            mBigCoin.animate()
                                                                    .setInterpolator(new AccelerateInterpolator())
                                                                    .scaleX(0.1111f)
                                                                    .scaleY(0.1111f)
                                                                    .translationX(mBigCoin.getX() - mCoinsNumber.getX() - Dp.toPx(4))
                                                                    .translationY(-mBigCoinFrame.getHeight() + Dp.toPx(82))
                                                                    .setDuration(700)
                                                                    .setListener(new Animator.AnimatorListener() {
                                                                        @Override
                                                                        public void onAnimationStart(Animator animation) {}

                                                                        @Override
                                                                        public void onAnimationEnd(Animator animation) {
                                                                            mBigCoin.setVisibility(View.GONE);

                                                                            Async.run(DayGoal::getCountDaysAccomplished, days -> {
                                                                                mCoinsNumber.setText(days + " coins");
                                                                                //mCoinsNumberLeaderboard.setText(days + " coins");
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onAnimationCancel(Animator animation) {}

                                                                        @Override
                                                                        public void onAnimationRepeat(Animator animation) {}
                                                                    });

                                                            mCoinPocket.animate()
                                                                    .translationY(-Dp.toPx(8))
                                                                    .setListener(null)
                                                                    .setDuration(400)
                                                                    .alpha(0);

                                                            mNonZeroDayView.setVisibility(View.VISIBLE);
                                                            mNonZeroDayView.animate()
                                                                    .setStartDelay(500)
                                                                    .alpha(1)
                                                                    .setListener(null);
                                                        }

                                                        @Override
                                                        public void onAnimationCancel(Animator animation) {}

                                                        @Override
                                                        public void onAnimationRepeat(Animator animation) {}
                                                    });
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {}

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {}
                                    });

                                    animator.setDuration(400);
                                    animator.start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {}

                                @Override
                                public void onAnimationRepeat(Animator animation) {}
                            });

                    // Update the database
                    Async.run(DayGoal::setAccomplishedToday, mDismissListener::zeroDayDismissed);
                    //mDismissListener.zeroDayDismissed();
                } else {
                    // Move the text back to its position
                    mZeroDayView.animate()
                            .setInterpolator(new OvershootInterpolator(2.5f))
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
