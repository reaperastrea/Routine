package org.coeg.routine.backend;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
public class Routine {
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @PrimaryKey int id;
    String name;
    String time; // use SimpleDateFormat to parse
    int days;
    boolean active; // probably unused, but may be used in the future

    public int getId() { return id; }
    public String getName() { return name; }
    public Date getTime() { return formatter.parse(time, null); }
    public String getTimeAsString() { return time; }
    public boolean isActive() { return active; }
    public int getDaysAsInteger() { return days; }
    public Days[] getDays() {
        ArrayList<Days> temp = new ArrayList<>();

        if((this.days & 1) == 1) temp.add(Days.Monday);
        if((this.days & 2) == 2) temp.add(Days.Tuesday);
        if((this.days & 4) == 4) temp.add(Days.Wednesday);
        if((this.days & 8) == 8) temp.add(Days.Thursday);
        if((this.days & 16) == 16) temp.add(Days.Friday);
        if((this.days & 32) == 32) temp.add(Days.Saturday);
        if((this.days & 64) == 64) temp.add(Days.Sunday);

        return temp.toArray(new Days[0]);
    }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setTime(Date date) { this.time = formatter.format(date); }
    public void setActive(boolean active) { this.active = active; }
    public void setDays(Days[] days) {
        int temp = 0;
        List<Days> daysList = Arrays.asList(days);

        if(daysList.contains(Days.Monday)) temp++;
        if(daysList.contains(Days.Tuesday)) temp += 2;
        if(daysList.contains(Days.Wednesday)) temp += 4;
        if(daysList.contains(Days.Thursday)) temp += 8;
        if(daysList.contains(Days.Friday)) temp += 16;
        if(daysList.contains(Days.Saturday)) temp += 32;
        if(daysList.contains(Days.Sunday)) temp += 64;

        this.days = temp;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(" - ");
        builder.append(name);
        builder.append(" (");
        builder.append(time);
        builder.append("), active: ");
        builder.append(active);

        return builder.toString();
    }
}
