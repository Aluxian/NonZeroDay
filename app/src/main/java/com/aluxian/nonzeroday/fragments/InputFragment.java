package com.aluxian.nonzeroday.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.aluxian.nonzeroday.R;
import com.aluxian.nonzeroday.models.DayGoal;
import com.aluxian.nonzeroday.models.YearGoal;
import com.aluxian.nonzeroday.utils.Async;
import com.aluxian.nonzeroday.utils.ShakeAnimation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class InputFragment extends Fragment {

    /** A callbacks instance. */
    private NextButtonListener mNextButtonListener;

    /** The input TextView. */
    @InjectView(R.id.input) AutoCompleteTextView mAutoCompleteTextView;

    /** An animation used to shake the input TextView horizontally. */
    private ShakeAnimation mShakeAnimation = new ShakeAnimation();

    /** The type of the fragment: year goal input, day goal input or empty. */
    private Type mType;

    public static Fragment newInstance(Type type) {
        Fragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putString(Type.class.getName(), type.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mNextButtonListener = (NextButtonListener) activity;
        mType = Type.valueOf(getArguments().getString(Type.class.getName()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;

        switch (mType) {
            case YEAR:
                rootView = inflater.inflate(R.layout.fragment_goal_input, container, false);
                ((TextView) rootView.findViewById(R.id.header)).setText(R.string.input_goal_year);
                break;

            case DAY:
                rootView = inflater.inflate(R.layout.fragment_goal_input, container, false);
                ((TextView) rootView.findViewById(R.id.header)).setText(R.string.input_goal_day);
                break;

            default:
                return new View(getActivity());
        }

        ButterKnife.inject(this, rootView);

        mAutoCompleteTextView.getBackground().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        mAutoCompleteTextView.setImeOptions(mType == Type.YEAR ? EditorInfo.IME_ACTION_NEXT : EditorInfo.IME_ACTION_DONE);
        mAutoCompleteTextView.requestFocus();
        setAdapter();

        mAutoCompleteTextView.addTextChangedListener(new TextChangedListener());
        mAutoCompleteTextView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                next();
                return true;
            }

            return false;
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * Gets the suggestions from the database and sets the adapter.
     */
    @SuppressWarnings("Convert2streamapi")
    private void setAdapter() {
        switch (mType) {
            case YEAR:
                Async.run(YearGoal::getPreviousEntries, (previousYearGoals) -> {
                    List<String> suggestions = new ArrayList<>();

                    for (YearGoal goal : previousYearGoals) {
                        suggestions.add(goal.description);
                    }

                    mAutoCompleteTextView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_autocomplete, suggestions));
                });

                break;

            case DAY:
                Async.run(DayGoal::getPreviousEntries, (previousDayGoals) -> {
                    List<String> suggestions = new ArrayList<>();

                    for (DayGoal goal : previousDayGoals) {
                        suggestions.add(goal.description);
                    }

                    mAutoCompleteTextView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_autocomplete, suggestions));
                });

                break;
        }
    }

    /**
     * Called when the Next button is clicked.
     */
    @OnClick(R.id.btn_next)
    void next() {
        String input = mAutoCompleteTextView.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            mShakeAnimation.playOn(mAutoCompleteTextView);
            return;
        }

        switch (mType) {
            case DAY:
                Async.run(() -> new DayGoal(Calendar.getInstance(), input).save(), (id) -> {});

                // Hide the soft keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mAutoCompleteTextView.getWindowToken(), 0);

                break;

            case YEAR:
                Async.run(() -> new YearGoal(Calendar.getInstance(), input).save(), (id) -> {});
                break;
        }

        mNextButtonListener.onGoalInputNextClicked(mType);
    }

    private class TextChangedListener implements TextWatcher {

        /** The length of the text before the text changed. */
        private int mPreviousLength;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mPreviousLength = s.length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mPreviousLength >= 70 && s.length() >= 70) {
                mShakeAnimation.playOn(mAutoCompleteTextView);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}

    }

    public static enum Type {
        YEAR, DAY, EMPTY
    }

    public static interface NextButtonListener {

        /**
         * Called when the 'next' button has been clicked.
         *
         * @param type The type of the fragment where the next button resides.
         */
        public void onGoalInputNextClicked(Type type);

    }

}
