package com.example.amaterasu.pchat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by user on 28-04-2016.
 */
public class Notification extends AppCompatActivity {
    Context context;
    NotificationManager mNotifyMgr;
    public Notification(Context context, NotificationManager mNotifyMgr)
    {
        this.context=context;
        this.mNotifyMgr = mNotifyMgr;
    }
    public void startNotification(String Sender,String Text) {
        Log.i("NextActivity", "startNotification");

        // Sets an ID for the notification
        int mNotificationId = 001;

        // Build Notification , setOngoing keeps the notification always in status bar
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(HomeScreen.contacthash.get(Sender))
                        .setContentText(Text)

                        .setAutoCancel(true);

        // Create pending intent, mention the Activity which needs to be
        //triggered when user clicks on notification(StopScript.class in this case)

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, HomeScreen.class), PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentIntent(contentIntent);


        // Gets an instance of the NotificationManager service
        //NotificationManager mNotifyMgr =
          //      (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }
}
