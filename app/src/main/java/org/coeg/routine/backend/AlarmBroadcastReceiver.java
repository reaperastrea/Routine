package org.coeg.routine.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("Routine", "BROADCAST RECEIVED");
        if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.getAction()))
        {
            // Reschedule routines to alarm manager
            // When user reboot their phone
            return;
        }

        // Start alarm service
        startAlarmService(context, intent);
    }

    private void startAlarmService(Context context, Intent intent)
    {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra("Routine Name", intent.getStringExtra("Routine Name"));
        intentService.putExtra("Routine ID", intent.getIntExtra("Routine ID", -1));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            context.startForegroundService(intentService);
        }
        else
        {
            context.startService(intentService);
        }
    }
}
