package com.ym.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by ym on 16-10-24.
 */

public class Receiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.ym.lab4.receiver2")) {
            String content = intent.getStringExtra("content");

            Notification.Builder builder = new Notification.Builder(context);

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.dynamic);

            Intent newIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);

            builder.setContentTitle("动态广播")
                    .setContentText(content)
                    .setTicker("您有一条新消息")
                    .setLargeIcon(bm)
                    .setSmallIcon(R.mipmap.dynamic)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = builder.build();
            manager.notify(0, notification);
        }
    }
}
