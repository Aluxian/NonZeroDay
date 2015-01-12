package com.aluxian.zerodays.fragments;

public interface GoalFragmentCallbacks {

    /**
     * Called when the Next button has been clicked.
     *
     * @param position The position of the fragment in the view pager adapter.
     */
    public void onNextButtonClicked(int position);

}
