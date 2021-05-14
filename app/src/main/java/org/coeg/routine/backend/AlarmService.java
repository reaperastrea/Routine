package org.coeg.routine.backend;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.coeg.routine.R;
import org.coeg.routine.activities.MainActivity;

public class AlarmService extends Service
{
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";
    public static final String CHANNEL_NAME = "Routine";
    public static final String CHANNEL_DESC = "Routine Remainder";
    public static final int NOTIFICATION_ID = 1337;

    private static final int ACTION_OK = 0;
    private static final int ACTION_SNOOZE = 1;

    private NotificationManagerCompat notificationManager;
    private Notification notification;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.notification_sound);
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        setupNotification(intent);
        startForeground(NOTIFICATION_ID, notification);
        mediaPlayer.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        notificationManager.cancel(NOTIFICATION_ID);
        mediaPlayer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void setupNotification(Intent intent)
    {
        String routineName = intent.getStringExtra("Routine Name");
        int routineID = intent.getIntExtra("Routine ID", -1);

        long[] vibratePattern = new long[] { 500, 500, 500 };

        Intent receiver = new Intent(this, NotificationActionReceiver.class);
        PendingIntent pOkIntent = PendingIntent.getBroadcast(
                this, ACTION_OK, receiver
                        .putExtra("Request Code", ACTION_OK)
                        .putExtra("Routine ID", routineID), 0);

        PendingIntent pSnoozeIntent = PendingIntent.getBroadcast(
                this, ACTION_SNOOZE, receiver
                        .putExtra("Request Code", ACTION_SNOOZE)
                        .putExtra("Routine ID", routineID), 0);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                // To enable heads up notification
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVibrate(vibratePattern)
                .setSound(null)
                // Make Heads up notification will be persistent until action given
                .setFullScreenIntent(pendingIntent, true)
                .setContentTitle("Routine Remainder")
                .setContentText(routineName)
                .setSmallIcon(R.drawable.ic_logo_routine)
                .setOngoing(true)
                .addAction(0, getString(R.string.notification_ok), pOkIntent)
                .addAction(0, getString(R.string.notification_snooze), pSnoozeIntent)
                .build();

        notificationManager = NotificationManagerCompat.from(this);

        // For API >26
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setSound(null, null);
            mChannel.setDescription(CHANNEL_DESC);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(vibratePattern);

            notificationManager.createNotificationChannel(mChannel);
        }

        Log.i("Routine", "ALARM SERVICE RUNNING");
        Log.i("Routine ID", "Routine ID : " + routineID);
    }
}
