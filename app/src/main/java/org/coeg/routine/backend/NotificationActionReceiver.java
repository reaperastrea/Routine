package org.coeg.routine.backend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.LinkedList;

public class NotificationActionReceiver extends BroadcastReceiver
{
    private static final int ACTION_OK = 0;
    private static final int ACTION_SNOOZE = 1;

    // Time in minutes when the alarm re-announce when user pressed the snooze button
    private static final int SNOOZE_MINUTE = 1;

    private RoutinesHandler handler;
    private LinkedList<Routine> routines = new LinkedList<>();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int reqCode = intent.getIntExtra("Request Code", -1);
        int routineID = intent.getIntExtra("Routine ID", -1);
        String routineName = intent.getStringExtra("Routine Name");

        // Stop current service
        Intent intentServiceToStop = new Intent(context, AlarmService.class);

        switch(reqCode)
        {
            case ACTION_OK:
                Log.i("Routine", "User Clicked Ok in Notification");
                Log.i("Routine", "Receivers Request Code : " + reqCode);
                Log.i("Routine", "Routine ID : " + routineID);

                userAcknowledgement(context, routineID, routineName);

                context.stopService(intentServiceToStop);
                break;

            case ACTION_SNOOZE:
                Log.i("Routine", "User Clicked Snooze in Notification");
                Log.i("Routine", "Receivers Request Code : " + reqCode);
                Log.i("Routine", "Routine ID : " + routineID);

                remindMeLater(context, routineID, routineName);

                context.stopService(intentServiceToStop);
                break;
        }
    }

    /**
     * Acknowledge user action and save to history database
     * @param context app context
     * @param routineID the routine itself
     */
    private void userAcknowledgement(Context context, int routineID, String routineName)
    {
        // Save current time with routine id to database
        // Get the current system time
        Calendar actionTime = Calendar.getInstance();
        actionTime.setTimeInMillis(System.currentTimeMillis());

        History userLog = new History();

        userLog.setRoutineId(routineID);
        userLog.setDate(actionTime.getTime());
        userLog.setTime(actionTime.getTime());

        Runnable r = () -> {
            handler = new RoutinesHandler(context);
            handler.addHistory(userLog);
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void remindMeLater(Context context, int routineID, String routineName)
    {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar remindTime = Calendar.getInstance();
        remindTime.setTimeInMillis(System.currentTimeMillis());

        // Set snooze time in the future
        remindTime.add(Calendar.MINUTE, SNOOZE_MINUTE);

        // Intent to handle broadcast from alarm manager
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra("Routine ID", routineID);
        intent.putExtra("Routine Name", routineName);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, routineID, intent, 0);

        // Set another alarm
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                remindTime.getTimeInMillis(),
                alarmPendingIntent
        );
    }
}
