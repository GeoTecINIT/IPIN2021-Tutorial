package es.uji.geotec.ipin.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import es.uji.geotec.ipin.service.ScanService;

public class ScanAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ScanAlarmManager scanAlarmManager = new ScanAlarmManager(context);
        scanAlarmManager.schedule(ScanAlarmManager.INTERVAL);

        // TODO: start service as foreground using ContextCompat.startForegroundService(...)
        Intent serviceIntent = new Intent(context, ScanService.class);
        context.startService(serviceIntent);
    }
}