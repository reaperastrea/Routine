package org.coeg.routine.backend;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AlarmSystem
{
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

    private final Context context;
    private AlarmManager alarmManager;

    public AlarmSystem(Context context, AlarmManager alarmService)
    {
        this.context = context;
        this.alarmManager = alarmService;
    }

    /**
     * Schedule routine to alarm manager
     * @param routine
     * @apiNote PLEASE CHECK TO MAKE SURE THERE AREN'T ANY OF THE SAME ROUTINE WITH
     * THE SAME DAY + HOURS IN THE DATABASE BEFORE CALLING THIS METHOD
     */
    public void schedule(Routine routine)
    {
        int routineID = routine.getId();
        String routineName = routine.getName();

        Calendar routineTime = Calendar.getInstance();

        try
        {
            routineTime.setTime(Objects.requireNonNull(timeFormatter.parse(routine.getTimeAsString())));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Schedule for each selected days
        for (Days day : routine.getDays())
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

            Log.i("DEBUG-TEST", "---------");

            // Check if whether the day selected has passed, if yes then
            // add 7 days into the scheduler
            if (scheduleTime.getTimeInMillis() <= System.currentTimeMillis())
            {
                scheduleTime.add(Calendar.DAY_OF_MONTH, 7);
                Log.i("DEBUG-TEST", "ADD 7 DAYS");
            }

            // DEBUG LOG
            Log.i("DEBUG-TEST", "Day     : " + scheduleTime.get(Calendar.DAY_OF_WEEK));
            Log.i("DEBUG-TEST", "Hours   : " + scheduleTime.get(Calendar.HOUR_OF_DAY));
            Log.i("DEBUG-TEST", "Minutes : " + scheduleTime.get(Calendar.MINUTE));
            Log.i("DEBUG-TEST", "DOM     : " + scheduleTime.get(Calendar.DAY_OF_MONTH));
            Log.i("DEBUG-TEST", "Month   : " + scheduleTime.get(Calendar.MONTH));

            // Intent to handle broadcast from alarm manager
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
            intent.putExtra("Routine ID", routineID);
            intent.putExtra("Routine Name", routineName);

            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, routineID, intent, 0);

            // Routine will always repeating
            int RUN_WEEKLY = 24 * 7 * 60 * 60 * 1000;

            // Schedule to alarm manager
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    scheduleTime.getTimeInMillis(),
                    RUN_WEEKLY,
                    alarmPendingIntent
            );
        }
    }
}
