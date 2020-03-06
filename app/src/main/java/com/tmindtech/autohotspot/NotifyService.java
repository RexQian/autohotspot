package com.tmindtech.autohotspot;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author RexQian
 * @date 2020-03-06
 */
public class NotifyService extends Service {
    private static final String TAG = NotifyService.class.getSimpleName();
    private static FrontServiceInfo frontServiceInfo;
    private static BackgroundListener listener;

    public NotifyService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public NotifyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startBackground(@NonNull Context context, @NonNull FrontServiceInfo info,
                                       @NonNull final BackgroundListener blistener) {
        if (listener == null) {
            frontServiceInfo = info;
            listener = blistener;
            Intent intent = new Intent(context, NotifyService.class);

            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getFlags() == PendingIntent.FLAG_CANCEL_CURRENT && listener != null) {
            listener.closeApplication();
            return START_NOT_STICKY;
        }

        if (listener != null) {
            Intent notificationIntent = new Intent();
            notificationIntent.setComponent(new ComponentName(getApplicationContext(),
                frontServiceInfo.activityClass));
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 10, notificationIntent, 0);

            Intent intentStop = new Intent(this, NotifyService.class);
            PendingIntent pendingIntentStop =
                PendingIntent.getService(this, 0, intentStop,
                    PendingIntent.FLAG_CANCEL_CURRENT);


            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), frontServiceInfo.largeIcon);
            Notification notification = notificationBuilder()
                .setContentTitle(frontServiceInfo.appName)
                .setContentText(frontServiceInfo.runContent)
                .setSmallIcon(frontServiceInfo.smallIcon)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .addAction(frontServiceInfo.closeIcon, frontServiceInfo.closeContent, pendingIntentStop)
                .build();


            startForeground(1, notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private Notification.Builder notificationBuilder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NotifyService.class.getSimpleName(),
                getText(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            return new Notification.Builder(this, NotifyService.class.getSimpleName());
        } else {
            return new Notification.Builder(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener = null;
    }

    public interface BackgroundListener {
        void closeApplication();
    }
}
