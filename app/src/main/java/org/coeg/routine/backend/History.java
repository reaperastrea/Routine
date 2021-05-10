package org.coeg.routine.backend;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class History {
    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    @PrimaryKey int id;
    int routineId;
    String date;
    String time;

    public int getId() { return id; }
    public int getRoutineId() { return routineId; }
    public Date getDate() { return dateFormatter.parse(date, null); }
    //public String getDateAsString() { return date; }
    public Date getTime() { return formatter.parse(time, null); }
    //public String getTimeAsString() { return time; }

    public void setId(int id) { this.id = id; }
    public void setRoutineId(int id) { routineId = id; }
    public void setDate(Date date) { this.date = dateFormatter.format(date); }
    public void setTime(Date date) { this.time = formatter.format(date); }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(" - Routine ID ");
        builder.append(routineId);
        builder.append(" (late time: ");
        builder.append(time);
        builder.append(")");

        return builder.toString();
    }
}