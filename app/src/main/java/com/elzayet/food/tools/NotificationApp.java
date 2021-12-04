package com.elzayet.food.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.elzayet.food.MainActivity;
import com.elzayet.food.R;


public class NotificationApp {

    public static void addNotificationWithAction(Context context,String MSG){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"51");
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(MSG);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, My_Request_Code.ZERO_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(My_Request_Code.ZERO_REQUEST_CODE, builder.build());
    }

}