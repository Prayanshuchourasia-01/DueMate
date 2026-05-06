package com.prayanshu.billreminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String billName =
                intent.getStringExtra("billName");

        if (billName == null) {

            billName = "Bill";
        }

        // ===== OPEN APP =====

        Intent openIntent =
                new Intent(
                        context,
                        MainActivity.class
                );

        PendingIntent pendingIntent =
                PendingIntent.getActivity(

                        context,

                        0,

                        openIntent,

                        PendingIntent.FLAG_UPDATE_CURRENT
                                |
                                PendingIntent.FLAG_IMMUTABLE
                );

        // ===== DEVICE DEFAULT SOUND =====

        Uri soundUri =
                RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_NOTIFICATION
                );

        // ===== NOTIFICATION =====

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        "bill_channel"
                )

                        .setSmallIcon(
                                android.R.drawable.ic_dialog_info
                        )

                        .setContentTitle(
                                "Bill Reminder"
                        )

                        .setContentText(
                                billName + " is due now!"
                        )

                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH
                        )

                        .setAutoCancel(true)

                        .setSound(soundUri)

                        .setContentIntent(
                                pendingIntent
                        );

        // ===== MANAGER =====

        NotificationManager manager =
                (NotificationManager)
                        context.getSystemService(
                                Context.NOTIFICATION_SERVICE
                        );

        // ===== CHANNEL =====

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(

                            "bill_channel",

                            "Bill Reminder",

                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription(
                    "Bill reminder notifications"
            );

            if (manager != null) {

                manager.createNotificationChannel(
                        channel
                );
            }
        }

        // ===== SHOW NOTIFICATION =====

        if (manager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                if (context.checkSelfPermission(
                        android.Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED) {

                    manager.notify(
                            1001,
                            builder.build()
                    );
                }

            } else {

                manager.notify(
                        1001,
                        builder.build()
                );
            }
        }
    }
}