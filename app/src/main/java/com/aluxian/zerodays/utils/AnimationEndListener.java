package com.aluxian.zerodays.utils;

import android.view.animation.Animation;

/**
 * An AnimationListener that overrides the methods for start and repeat, so that users of this class don't have to.
 */
public abstract class AnimationEndListener implements Animation.AnimationListener {

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationRepeat(Animation animation) {}

}
