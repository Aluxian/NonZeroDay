package com.aluxian.nonzeroday.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aluxian.nonzeroday.R;
import com.aluxian.nonzeroday.models.DayGoal;
import com.aluxian.nonzeroday.models.YearGoal;
import com.aluxian.nonzeroday.utils.Async;

public class GoalsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goals, container, false);

        TextView yearGoalTextView = (TextView) rootView.findViewById(R.id.text_goal_year);
        TextView dayGoalTextView = (TextView) rootView.findViewById(R.id.text_goal_today);

        Async.run(YearGoal::getForThisYear, (yearGoal) -> yearGoalTextView.setText(yearGoal.description));
        Async.run(DayGoal::getForToday, (dayGoal) -> dayGoalTextView.setText(dayGoal.description));

        return rootView;
    }

}
