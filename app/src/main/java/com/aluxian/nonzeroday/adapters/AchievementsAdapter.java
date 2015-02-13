package com.aluxian.nonzeroday.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aluxian.nonzeroday.R;
import com.aluxian.nonzeroday.models.Achievement;
import com.aluxian.nonzeroday.utils.Async;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AchievementsAdapter extends BaseAdapter {

    private List<Achievement> mAchievementsList = new ArrayList<>();

    public AchievementsAdapter() {
        Async.run(Achievement::getAll, achievements -> {
            mAchievementsList.addAll(achievements);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getCount() {
        return mAchievementsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAchievementsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Achievement achievement = mAchievementsList.get(position);

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievement, parent, false);
            view.setTag(new ViewHolder(view));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(achievement.name);
        holder.description.setText("Unlocked on " + new SimpleDateFormat("MMM d, yyyy").format(achievement.date));

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.name) TextView name;
        @InjectView(R.id.description) TextView description;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
