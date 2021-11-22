package es.uji.geotec.ipin.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

        // TODO: replace with the correct scheduling method depending on API level
        // Steps:
        //      - Get API level from Build.VERSION.SDK_INT
        //      - Use alarmManager private member most appropriate method:
        //          - 23 <= API --> setExactAndAllowWhileIdle(...)
        //          - 19 <= API < 23 --> setExact(...)
        //          - API < 19 --> set(...)
        //          - All methods use the same parameters
        //
        // Android DOCS --> https://developer.android.com/reference/android/app/AlarmManager#setExactAndAllowWhileIdle(int,%20long,%20android.app.PendingIntent)
        //                  https://developer.android.com/reference/android/app/AlarmManager#setExact(int,%20long,%20android.app.PendingIntent)
        //                  https://developer.android.com/reference/android/app/AlarmManager#set(int,%20long,%20android.app.PendingIntent)
        this.alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + interval,
                getPendingIntent()
        );
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
