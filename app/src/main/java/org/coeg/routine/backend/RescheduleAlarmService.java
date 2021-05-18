package org.coeg.routine.backend;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.LinkedList;

public class RescheduleAlarmService extends Service
{
    private final static int REQ_ADD_SCHEDULE = 1;
    private final static int REQ_DELETE_SCHEDULE = 2;

    private RoutinesHandler handler;
    private LinkedList<Routine> routines = new LinkedList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Context context = getApplicationContext();

        Runnable r = () -> {
            handler = new RoutinesHandler(context);
            routines.addAll(handler.getAllRoutines());

            for (Routine routine : routines)
            {
                routine.schedule(context, 1);
            }
        };

        Thread t = new Thread(r);
        t.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
