package com.aluxian.zerodays.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

@Table(name = "YearGoals")
public class YearGoal extends Model {

    @Column()
    public int year;

    @Column()
    public boolean accomplished;

    @Column()
    public String description;

    public YearGoal() {
        super();
    }

    public YearGoal(Calendar calendar, String description) {
        super();
        this.year = calendar.get(Calendar.YEAR);
        this.accomplished = false;
        this.description = description;
    }

    /**
     * @return The number of goals set for the current year.
     */
    public static long getCountForThisYear() {
        return new Select()
                .from(YearGoal.class)
                .where("year = ?", Calendar.getInstance().get(Calendar.YEAR))
                .count();
    }

    /**
     * @return The goal set for this year.
     */
    public static YearGoal getForThisYear() {
        return new Select()
                .from(YearGoal.class)
                .where("year = ?", Calendar.getInstance().get(Calendar.YEAR))
                .executeSingle();
    }

    /**
     * @return A list of the last 10 entered year goals.
     */
    public static List<YearGoal> getPreviousEntries() {
        return new Select()
                .from(YearGoal.class)
                .where("year < ?", Calendar.getInstance().get(Calendar.YEAR))
                .groupBy("description")
                .orderBy("year DESC")
                .limit(10)
                .execute();
    }

}
