package com.ym.lab4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

/**
 * Created by ym on 16-10-24.
 */

public class Receiver1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.ym.lab4.receiver1")) {
            int index = intent.getIntExtra("index", -1);
            FruitsModel.Fruit fruit = FruitsModel.getInstance().data.get(index);

            Notification.Builder builder = new Notification.Builder(context);

            Intent newIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, 0);

            builder.setContentTitle("静态广播")
                    .setContentText(fruit.name)
                    .setTicker("您有一条新消息")
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), fruit.res))
                    .setSmallIcon(fruit.res)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = builder.build();
            manager.notify(0, notification);
        }
    }
}
