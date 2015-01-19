package com.aluxian.zerodays.fragments;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.aluxian.zerodays.R;
import com.aluxian.zerodays.db.DayGoal;
import com.aluxian.zerodays.db.YearGoal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InputFragment extends Fragment {

    private Callbacks mCallbacks;
    private AutoCompleteTextView mAutoCompleteTextView;
    private Type mType;

    public static Fragment newInstance(Type type) {
        Fragment fragment = new InputFragment();
        Bundle args = new Bundle();
        args.putString(Type.class.getName(), type.name());
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("Convert2streamapi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mType = Type.valueOf(getArguments().getString(Type.class.getName()));
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
                rootView = new View(getActivity());
                break;
        }

        if (mType != Type.EMPTY) {
            mAutoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.input);
            mAutoCompleteTextView.getBackground().setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
            mAutoCompleteTextView.setImeOptions(mType == Type.YEAR ? EditorInfo.IME_ACTION_NEXT : EditorInfo.IME_ACTION_DONE);
            mAutoCompleteTextView.requestFocus();

            List<String> suggestions = new ArrayList<>();

            switch (mType) {
                case YEAR:
                    for (YearGoal goal : YearGoal.getPreviousEntries()) {
                        suggestions.add(goal.description);
                    }

                    break;

                case DAY:
                    for (DayGoal goal : DayGoal.getPreviousEntries()) {
                        suggestions.add(goal.description);
                    }

                    break;
            }

            mAutoCompleteTextView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.autocomplete_item,
                    suggestions.toArray(new String[suggestions.size()])));

            mAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
                private int mPreviousLength;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mPreviousLength = s.length();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mPreviousLength >= 70 && s.length() >= 70) {
                        animateError(mAutoCompleteTextView).start();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            mAutoCompleteTextView.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    next();
                }

                return false;
            });

            View contentView = getActivity().findViewById(android.R.id.content);
            View hiddenView = rootView.findViewById(R.id.next_container);

            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                private int mPreviousHeight;

                @Override
                public void onGlobalLayout() {
                    int newHeight = contentView.getHeight();

                    if (mPreviousHeight != 0) {
                        if (mPreviousHeight > newHeight) {
                            hiddenView.setVisibility(View.GONE);
                        } else if (mPreviousHeight < newHeight) {
                            hiddenView.setVisibility(View.VISIBLE);
                        }
                    }

                    mPreviousHeight = newHeight;
                }
            });

            rootView.findViewById(R.id.btn_next).setOnClickListener((v) -> next());
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    private void next() {
        String input = mAutoCompleteTextView.getText().toString().trim();

        if (TextUtils.isEmpty(input)) {
            animateError(mAutoCompleteTextView).start();
            return;
        }

        switch (mType) {
            case DAY:
                new DayGoal(Calendar.getInstance(), input).save();
                break;

            case YEAR:
                new YearGoal(Calendar.getInstance(), input).save();
                break;
        }

        mCallbacks.onNextButtonClicked(mType);
    }

    private static ObjectAnimator animateError(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).setDuration(500);
    }

    public static enum Type {
        YEAR, DAY, EMPTY
    }

    public static interface Callbacks {

        /**
         * Called when the 'next' button has been clicked.
         *
         * @param type The type of the fragment where the next button resides.
         */
        public void onNextButtonClicked(Type type);

    }

}
