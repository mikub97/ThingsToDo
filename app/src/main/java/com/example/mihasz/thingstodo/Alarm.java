package com.example.mihasz.thingstodo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import static android.os.Build.*;

/**
 * Created by mihasz on 05.11.17.
 */

public class Alarm extends BroadcastReceiver {
    NotificationCompat.Builder mBuilder;
    @RequiresApi(api = VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        Notification.Builder mBuilder = new Notification.Builder(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("things_channel",
                    "Channel for thing notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            mBuilder.setChannelId("things_channel");
        }
        mBuilder.setSmallIcon(R.drawable.notify_icon);
        mBuilder.setContentTitle("There is a Thing To Do ...");
        String text = new String("Click for more");
        Bundle bundle = intent.getBundleExtra("bundle");
        Thing thing = (Thing) bundle.getSerializable("thing");
        mBuilder.setContentText(thing.getName());
        mBuilder.setAutoCancel(true);
        mBuilder.setWhen(System.currentTimeMillis());
        NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context,ViewThing.class);
        intent1.putExtra("thing",thing);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(32921,mBuilder.build());
    }
}
