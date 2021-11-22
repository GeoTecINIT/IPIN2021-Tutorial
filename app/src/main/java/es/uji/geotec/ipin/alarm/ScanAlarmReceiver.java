package es.uji.geotec.ipin.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.uji.geotec.ipin.scan.ScanManager;

public class ScanAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: reschedule the alarm
        // Steps:
        //      - Create a new ScanAlarmManager instance using context.
        //          - Schedule alarm --> ScanAlarmManager#schedule(interval).
        //          - Interval --> ScanAlarmManager.INTERVAL (one minute).

        ScanManager scanManager = new ScanManager(context, "a9c04048-a71e-42b5-b569-13b5ac77b618");
        scanManager.startScanAndStoreFingerprints(ScanManager.SCANNING_TIME)
                .subscribe(() -> Log.d("ScanAlarmReceiver", "scan ended!"));
    }
}