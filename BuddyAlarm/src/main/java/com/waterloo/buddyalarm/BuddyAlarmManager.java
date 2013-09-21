package com.waterloo.buddyalarm;

import android.*;
import android.R;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Contacts;

import java.util.Calendar;

/**
 * Created by Andersen on 21/09/13.
 */

public class BuddyAlarmManager {
    public static void setAlarmState(Context context, int id, long time, boolean on) {
        AlarmManager am = (AlarmManager) context.getSystemService((Context.ALARM_SERVICE));
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        time = cal.getTimeInMillis() + time;

        if (on) {
            am.set(AlarmManager.RTC_WAKEUP, time, pi);
        } else {
            am.cancel(pi);
        }
    }
}
