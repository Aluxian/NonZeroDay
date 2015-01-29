package com.aluxian.zerodays.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

@Table(name = "DayGoals")
public class DayGoal extends Model {

    @Column()
    public int day;

    @Column()
    public int month;

    @Column()
    public int year;

    @Column()
    public boolean accomplished;

    @Column()
    public String description;

    public DayGoal() {
        super();
    }

    public DayGoal(Calendar calendar, String description) {
        super();
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
        this.accomplished = false;
        this.description = description;
    }

    /**
     * @return The number of goals set for the current day.
     */
    public static long getCountForToday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new Select()
                .from(DayGoal.class)
                .where("day = ? AND month = ? AND year = ?", day, month, year)
                .count();
    }

    /**
     * @return The goal set for today.
     */
    public static DayGoal getForToday() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new Select()
                .from(DayGoal.class)
                .where("day = ? AND month = ? AND year = ?", day, month, year)
                .executeSingle();
    }

    public static List<DayGoal> getPreviousEntries() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return new Select()
                .from(DayGoal.class)
                .where("day < ? AND month = ? AND year = ?", day, month, year)
                .or("month < ? AND year = ?", month, year)
                .or("year < ?", year)
                .groupBy("description")
                .orderBy("year DESC, month DESC, day DESC")
                .limit(10)
                .execute();
    }

    public static DayGoal getForDate(int day, int month, int year) {
        return new Select()
                .from(DayGoal.class)
                .where("day = ? AND month = ? AND year = ?", day, month, year)
                .executeSingle();
    }

    public static boolean hasAccomplished(Calendar date) {
        return hasAccomplished(
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.MONTH),
                date.get(Calendar.YEAR));
    }

    public static boolean hasAccomplished(int day, int month, int year) {
        return new Select()
                .from(DayGoal.class)
                .where("day = ? AND month = ? AND year = ? AND accomplished = 1", day, month, year)
                .count() > 0;
    }

    public static int getStreak() {
        Calendar date = Calendar.getInstance();
        int streak = 0;

        while (hasAccomplished(date)) {
            streak++;
            date.add(Calendar.DAY_OF_MONTH, -1);
        }

        return streak;
    }

}