package com.aluxian.zerodays.fragments.goals;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.aluxian.zerodays.R;
import com.aluxian.zerodays.db.YearGoal;
import com.aluxian.zerodays.fragments.GoalFragmentCallbacks;

import java.util.Calendar;

public class YearGoalFragment extends Fragment {

    private GoalFragmentCallbacks mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goal_year, container, false);

        EditText inputField = (EditText) rootView.findViewById(R.id.goal_input);
        inputField.requestFocus();

        rootView.findViewById(R.id.btn_next).setOnClickListener((v) -> {
            String input = inputField.getText().toString();

            if (!TextUtils.isEmpty(input)) {
                new YearGoal(Calendar.getInstance(), input).save();
                mCallback.onNextButtonClicked(0);
            } else {
                Toast.makeText(getActivity(), "Feeling pessimistic, eh?", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (GoalFragmentCallbacks) activity;
    }

}
