package es.uji.geotec.ipin.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ScanAlarmManager {

    public static final int INTERVAL = 60000; // One minute

    private Context context;
    private AlarmManager alarmManager;

    public ScanAlarmManager(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void schedule(int interval) {
        if (isAlarmUp()) {
            cancelAlarm();
        }

        int alarmType = AlarmManager.RTC_WAKEUP;
        long triggerAtMillis = System.currentTimeMillis() + interval;
        PendingIntent pendingIntent = getPendingIntent();

        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= 23) { // Android 6 (Marshmallow) or higher
            this.alarmManager.setExactAndAllowWhileIdle(alarmType, triggerAtMillis, pendingIntent);
        } else if (sdkVersion >= 19) { // Android 4.4 (KitKat) - Android 5.1 (Lollipop)
            this.alarmManager.setExact(alarmType, triggerAtMillis, pendingIntent);
        } else { // Older versions
            this.alarmManager.set(alarmType, triggerAtMillis, pendingIntent);
        }
    }

    public void cancel() {
        if (!isAlarmUp()) {
            return;
        }

        this.cancelAlarm();
    }

    public boolean isAlarmUp() {
        return getPendingIntent(PendingIntent.FLAG_NO_CREATE) != null;
    }

    private PendingIntent getPendingIntent() {
        return getPendingIntent(0);
    }

    private PendingIntent getPendingIntent(int flag) {
        Intent receiverIntent = new Intent(
                context,
                ScanAlarmReceiver.class
        );

        return PendingIntent.getBroadcast(
                context,
                0,
                receiverIntent,
                flag
        );
    }

    private void cancelAlarm() {
        PendingIntent pendingIntent = getPendingIntent(0);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
