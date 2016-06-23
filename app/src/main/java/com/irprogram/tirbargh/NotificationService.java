package com.irprogram.tirbargh;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Reza on 6/14/2016.
 */
public class NotificationService extends Service {
    @Override
    public IBinder onBind(Intent arg)
    {return null;}

Context _context;
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId)
    {
        _context = this;
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

                Notification notification = new Notification(R.drawable.ok,"Chakar mikoni??",System.currentTimeMillis());
                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                Intent notificationIntent = new Intent(_context, welcome.class);
                PendingIntent contentIntent = PendingIntent.getActivity(_context, 0, notificationIntent, 0);
                notification.setLatestEventInfo(_context, "Content Title","Contect Text", contentIntent);
                notificationManager.notify(50, notification);
                timer.cancel();
            }
        },10000,10000);
        return START_STICKY;
    }

    private void stopService()
    {
        this.stopSelf();
    }
}
