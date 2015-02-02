package com.aluxian.zerodays.utils;

import android.animation.Animator;

/**
 * An AnimationListener that overrides the methods for start and repeat, so that users of this class don't have to.
 */
public abstract class AnimatorEndListener implements Animator.AnimatorListener {

    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public void onAnimationRepeat(Animator animation) {}

    @Override
    public void onAnimationCancel(Animator animation) {}

}
