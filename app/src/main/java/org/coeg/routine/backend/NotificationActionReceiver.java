package org.coeg.routine.backend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class NotificationActionReceiver extends BroadcastReceiver
{
    private static final int ACTION_OK = 0;
    private static final int ACTION_SNOOZE = 1;

    // Time in minutes when the alarm re-announce when user pressed the snooze button
    private static final int SNOOZE_MINUTE = 5;

    private Context context;

    private RoutinesHandler handler;
    private LinkedList<Routine> routineList = new LinkedList<>();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;
        int reqCode = intent.getIntExtra("Request Code", -1);
        int routineID = intent.getIntExtra("Routine ID", -1);
        String routineName = intent.getStringExtra("Routine Name");

        Log.i("Notification Routine ID", "" + routineID);

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

        new preferencesConfig(routineID, actionTime).execute(context);
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

    private void timeCheck(int routineID, Calendar responseTime)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        PreferencesStorage preferences = PreferencesStorage.getInstance();
        preferences.loadPreferences(context);

        Date routineTime = new Date();
        Date userTime;
        // Get routine proper time
        for (Routine routineDB : routineList)
        {
            if (routineDB.getId() == routineID)
            {
                Log.i("Notif Find", "ID" + routineDB.getId());
                userTime = responseTime.getTime();
                try
                {
                    routineTime = formatter.parse(routineDB.getTimeAsString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                long lateTime = getDateDiff(userTime, routineTime);

                Log.i("DIFFERENT TIME", "" + lateTime / (60 * 1000) % 60);
                int lateInMinute = (int) (lateTime / (60 * 1000) % 60);

                if (lateInMinute > 1)
                {
                    preferences.incrementLateCounter();
                }
                else
                {
                    preferences.incrementOnTimeCounter();
                }

                preferences.incrementHistoryCounter();
                preferences.savePreferences();
                break;
            }
        }
    }

    private long getDateDiff(Date date1, Date date2)
    {
        Long late = date1.getTime() - date2.getTime();

        return late;
    }

    private class preferencesConfig extends AsyncTask<Context, Void, Void>
    {
        int routineID;
        Calendar responseTime;

        public preferencesConfig(int routineID, Calendar responseTime)
        {
            this.routineID = routineID;
            this.responseTime = responseTime;
        }


        protected Void doInBackground(Context... context)
        {
            RoutinesHandler localHandler = new RoutinesHandler(context[0]);
            routineList.addAll(localHandler.getAllRoutines());
            return null;
        }

        protected void onProgressUpdate(Void... progress)
        {

        }

        protected void onPostExecute(Void result)
        {
            timeCheck(routineID, responseTime);
        }
    }
}
