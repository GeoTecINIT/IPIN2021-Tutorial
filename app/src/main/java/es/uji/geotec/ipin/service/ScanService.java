package es.uji.geotec.ipin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import es.uji.geotec.ipin.NotificationProvider;
import es.uji.geotec.ipin.scan.ScanManager;

public class ScanService extends Service {

    private PowerManager.WakeLock wakeLock;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "IPIN2021TUTORIAL::ScanServiceWakeLock"
        );

        wakeLock.acquire(ScanManager.SCANNING_TIME + 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int flag = super.onStartCommand(intent, flags, startId);

        NotificationProvider notificationProvider = new NotificationProvider(this);
        startForeground(
                NotificationProvider.NOTIFICATION_ID_FOREGROUND,
                notificationProvider.getForegroundNotification()
        );

        startScan();

        return flag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForeground(true);

        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    private void startScan() {
        ScanManager scanManager = new ScanManager(this, "a9c04048-a71e-42b5-b569-13b5ac77b618");
        scanManager.startScanAndStoreFingerprints(ScanManager.SCANNING_TIME).subscribe(() -> {
            Log.d("ScanService", "scan completed");
            stopSelf();
        });
    }
}
