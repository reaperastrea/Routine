package org.coeg.routine.backend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Routine implements Serializable
{
    private final static int REQ_ADD_SCHEDULE = 1;
    private final static int REQ_DELETE_SCHEDULE = 2;

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @PrimaryKey (autoGenerate = true)
    int id;

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

    /**
     * Schedule routine to alarm manager
     * @param context application context
     * @param requestCode either adding or delete schedule
     * @apiNote PLEASE CHECK TO MAKE SURE THERE AREN'T ANY OF THE SAME ROUTINE WITH
     * THE SAME DAY + HOURS IN THE DATABASE BEFORE CALLING THIS METHOD
     */
    public void schedule(Context context, int requestCode)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int alarmID = 0;
        Days[] days = getDays();
        int routineID = id;
        String routineName = name;

        Calendar routineTime = Calendar.getInstance();

        try
        {
            routineTime.setTime(Objects.requireNonNull(formatter.parse(time)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Schedule for each selected days
        for (Days day : days)
        {
            Calendar scheduleTime = Calendar.getInstance();

            // Set current calendar to current system time
            scheduleTime.setTimeInMillis(System.currentTimeMillis());
            scheduleTime.set(Calendar.HOUR_OF_DAY, routineTime.get(Calendar.HOUR_OF_DAY));
            scheduleTime.set(Calendar.MINUTE, routineTime.get(Calendar.MINUTE));
            scheduleTime.set(Calendar.SECOND, 0);
            scheduleTime.set(Calendar.MILLISECOND, 0);
            scheduleTime.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);

            switch(day)
            {
                case Sunday:
                    scheduleTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    break;

                case Monday:
                    scheduleTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    break;

                case Tuesday:
                    scheduleTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                    break;

                case Wednesday:
                    scheduleTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                    break;

                case Thursday:
                    scheduleTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                    break;

                case Friday:
                    scheduleTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                    break;

                case Saturday:
                    scheduleTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    break;
            }

            Log.i("Routine", "----- Scheduling -----");

            // Check if whether the day selected has passed, if yes then
            // add 7 days into the scheduler
            if (scheduleTime.getTimeInMillis() <= System.currentTimeMillis())
            {
                scheduleTime.add(Calendar.DAY_OF_MONTH, 7);
                Log.i("Routine", "ADD 7 DAYS");
            }

            // DEBUG LOG
            Log.i("Routine", "Routine ID : " + routineID);
            Log.i("Routine", "Day     : " + scheduleTime.get(Calendar.DAY_OF_WEEK));
            Log.i("Routine", "Hours   : " + scheduleTime.get(Calendar.HOUR_OF_DAY));
            Log.i("Routine", "Minutes : " + scheduleTime.get(Calendar.MINUTE));
            Log.i("Routine", "DOM     : " + scheduleTime.get(Calendar.DAY_OF_MONTH));
            Log.i("Routine", "Month   : " + scheduleTime.get(Calendar.MONTH));

            // Intent to handle broadcast from alarm manager
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
            intent.putExtra("Routine ID", routineID);
            intent.putExtra("Routine Name", routineName);

            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, (routineID * 10) + alarmID, intent, 0);

            // Routine will always repeating
            int RUN_WEEKLY = 24 * 7 * 60 * 60 * 1000;

            switch(requestCode)
            {
                case REQ_ADD_SCHEDULE:
                    Log.i("Routine", "SCHEDULE ACTION ADD");
                    // Schedule to alarm manager
                    alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            scheduleTime.getTimeInMillis(),
                            RUN_WEEKLY,
                            alarmPendingIntent
                    );
                    alarmID++;
                    break;

                case REQ_DELETE_SCHEDULE:
                    Log.i("Routine", "SCHEDULE ACTION REMOVE");
                    alarmManager.cancel(alarmPendingIntent);
                    alarmID++;
                    break;
            }

        }
    }
}
