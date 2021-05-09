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

        // Stop current service
        Intent intentServiceToStop = new Intent(context, AlarmService.class);

        switch(reqCode)
        {
            case ACTION_OK:
                Log.i("DEBUG-TEST", "User Clicked Ok in Notification");
                Log.i("DEBUG-TEST", "Receivers Request Code : " + reqCode);
                context.stopService(intentServiceToStop);
                break;

            case ACTION_SNOOZE:
                Log.i("DEBUG-TEST", "User Clicked Snooze in Notification");
                Log.i("DEBUG-TEST", "Receivers Request Code : " + reqCode);
                context.stopService(intentServiceToStop);
                break;
        }
    }
}
