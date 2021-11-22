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

        // TODO: Add call to AlarmManager#setRepeating(...) method for alarm scheduling
        // Steps:
        //      - Use alarmManager private member to call setRepeating(...). Parameters:
        //          - Alarm type: we will use AlarmManager.RTC_WAKEUP
        //          - Trigger at: alarm first execution timestamp --> now + interval
        //          - Interval: alarm repeating interval.
        //          - Pending intent: who will receive the trigger. Use getPendingIntent()
        //
        // Android DOCS --> https://developer.android.com/reference/android/app/AlarmManager#setRepeating(int,%20long,%20long,%20android.app.PendingIntent)
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
