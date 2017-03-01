package com.fimrc.sensorfusionframework.sensors;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Sven on 27.02.2017.
 */

public abstract class TimeSensorController extends BroadcastReceiver {

    public void setAlarm(Context context, long intervalInSeconds, String FilterName ) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(FilterName);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TimeUnit.SECONDS.toMillis(intervalInSeconds), pi);
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, this.getClass());
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

}
