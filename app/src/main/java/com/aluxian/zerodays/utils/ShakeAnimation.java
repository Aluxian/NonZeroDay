package com.aluxian.zerodays.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

import com.aluxian.zerodays.R;

/**
 * A helper class that creates an animation which shakes a given view horizontally.
 */
public class ShakeAnimation {

    /** A holder for the different x translation values that are going to be animated. */
    private PropertyValuesHolder mValuesTranslateX;

    public void playOn(View view) {
        if (mValuesTranslateX == null) {
            int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);
            mValuesTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                    Keyframe.ofFloat(0f, 0),
                    Keyframe.ofFloat(.10f, -delta),
                    Keyframe.ofFloat(.26f, delta),
                    Keyframe.ofFloat(.42f, -delta),
                    Keyframe.ofFloat(.58f, delta),
                    Keyframe.ofFloat(.74f, -delta),
                    Keyframe.ofFloat(.90f, delta),
                    Keyframe.ofFloat(1f, 0f)
            );
        }

        ObjectAnimator.ofPropertyValuesHolder(view, mValuesTranslateX).setDuration(500).start();
    }

}
