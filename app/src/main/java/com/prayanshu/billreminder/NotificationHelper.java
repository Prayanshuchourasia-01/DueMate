package com.prayanshu.billreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class NotificationHelper {

    public static void scheduleNotification(

            Context context,

            String billName,

            int year,

            int month,

            int day,

            int hour,

            int minute

    ) {

        // ===== INTENT =====

        Intent intent =
                new Intent(
                        context,
                        NotificationReceiver.class
                );

        intent.putExtra(
                "billName",
                billName
        );

        // ===== PENDING INTENT =====

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(

                        context,

                        (int) System.currentTimeMillis(),

                        intent,

                        PendingIntent.FLAG_UPDATE_CURRENT
                                |
                                PendingIntent.FLAG_IMMUTABLE
                );

        // ===== CALENDAR =====

        Calendar calendar =
                Calendar.getInstance();

        calendar.set(
                Calendar.YEAR,
                year
        );

        calendar.set(
                Calendar.MONTH,
                month
        );

        calendar.set(
                Calendar.DAY_OF_MONTH,
                day
        );

        calendar.set(
                Calendar.HOUR_OF_DAY,
                hour
        );

        calendar.set(
                Calendar.MINUTE,
                minute
        );

        calendar.set(
                Calendar.SECOND,
                0
        );

        // ===== ALARM MANAGER =====

        AlarmManager alarmManager =
                (AlarmManager)
                        context.getSystemService(
                                Context.ALARM_SERVICE
                        );

        if (alarmManager != null) {
            if (alarmManager.canScheduleExactAlarms()) {

                alarmManager.setExactAndAllowWhileIdle(

                        AlarmManager.RTC_WAKEUP,

                        calendar.getTimeInMillis(),

                        pendingIntent
                );
            }
        }
    }
}