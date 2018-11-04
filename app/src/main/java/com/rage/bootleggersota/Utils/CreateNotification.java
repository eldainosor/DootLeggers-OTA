package com.rage.bootleggersota.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.rage.bootleggersota.Activity.MainActivity;
import com.rage.bootleggersota.R;

public class CreateNotification {

    private Context context;
    private String content;
    private final String CHANNEL_ID = "Update Notifications";
    private NotificationCompat.Builder builder;

    public CreateNotification(Context context, String content) {
        this.context = context;
        this.content = content;
    }

    public void buildNotification () {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_update)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        createNotificationChannel();
    }

    private void createNotificationChannel () {
        CharSequence channelName = context.getString(R.string.notification_channel_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        String description = context.getString(R.string.notification_channel_description);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        notificationChannel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    public void showNotification () {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(123, builder.build());
    }
}
