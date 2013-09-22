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
        intent.putExtra("alarmId", id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long currTime; //current time in ms
        long setTime; // time set by the user in ms
        long nextAlarm; // time the alarm will go off next

        Calendar cal = Calendar.getInstance();

        currTime = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        setTime = cal.getTimeInMillis() + time;

        if (setTime > (currTime - 60 * 1000)) {
            nextAlarm = setTime;
        } else {
            nextAlarm = setTime + 24 * 3600 * 1000;
        }

        if (on) {
            am.set(AlarmManager.RTC_WAKEUP, nextAlarm, pi);
        } else {
            am.cancel(pi);
        }
    }
}
