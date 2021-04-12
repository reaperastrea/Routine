package org.coeg.routine.backend;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Routine {
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @PrimaryKey int id;
    String name;
    String time; // use SimpleDateFormat to parse
    boolean active; // probably unused, but may be used in the future

    public int getId() { return id; }
    public String getName() { return name; }
    public Date getTime() { return formatter.parse(time, null); }
    public boolean isActive() { return active; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setTime(Date date) { this.time = formatter.format(date); }
    public void setActive(boolean active) { this.active = active; }

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
