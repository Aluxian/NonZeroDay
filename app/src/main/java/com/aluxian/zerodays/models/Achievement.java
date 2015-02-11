package com.aluxian.zerodays.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "Achievements")
public class Achievement extends Model {

    @Column public String name;
    @Column public int requirement;
    @Column public boolean unlocked;
    @Column public Date date;

    public Achievement() {
        super();
    }

    public Achievement(String name, int requirement, boolean unlocked, Date date) {
        this.name = name;
        this.requirement = requirement;
        this.unlocked = unlocked;
        this.date = date;
    }

    /**
     * @return All the achievements stored in the database.
     */
    public static List<Achievement> getAll() {
        return new Select().from(Achievement.class).execute();
    }

}
