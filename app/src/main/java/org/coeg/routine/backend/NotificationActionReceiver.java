package org.coeg.routine.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class NotificationActionReceiver extends BroadcastReceiver
{
    private static final int ACTION_OK = 0;
    private static final int ACTION_SNOOZE = 1;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        int reqCode = intent.getIntExtra("Request Code", -1);
        int routineID = intent.getIntExtra("Routine ID", -1);

        // Stop current service
        Intent intentServiceToStop = new Intent(context, AlarmService.class);

        switch(reqCode)
        {
            case ACTION_OK:
                Log.i("Routine", "User Clicked Ok in Notification");
                Log.i("Routine", "Receivers Request Code : " + reqCode);
                Log.i("Routine", "Routine ID : " + routineID);
                context.stopService(intentServiceToStop);
                break;

            case ACTION_SNOOZE:
                Log.i("Routine", "User Clicked Snooze in Notification");
                Log.i("Routine", "Receivers Request Code : " + reqCode);
                Log.i("Routine", "Routine ID : " + routineID);
                context.stopService(intentServiceToStop);
                break;
        }
    }

    private void saveActionToDatabase()
    {
        
    }
}
