package es.uji.geotec.ipin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import es.uji.geotec.ipin.NotificationProvider;
import es.uji.geotec.ipin.scan.ScanManager;

public class ScanService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: create and acquire a wake lock
        // Steps:
        //      - Get a PowerManager instance --> getSystemService(Context.POWER_SERVICE)
        //      - Use the PowerManager#newWakeLock(...) to create a new class level wake lock. Parameters:
        //          - levelAndFlags: use PowerManager.PARTIAL_WAKE_LOCK
        //          - tag: use IPIN2021TUTORIAL::ScanServiceWakeLock
        //      - Acquire the wake lock with a timeout (ScanManager.SCANNING_TIME + amount)
        //
        // Android DOCS: https://developer.android.com/reference/android/os/PowerManager#newWakeLock(int,%20java.lang.String)
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

        // TODO: release wake lock if it is being held
    }

    private void startScan() {
        ScanManager scanManager = new ScanManager(this, "a9c04048-a71e-42b5-b569-13b5ac77b618");
        scanManager.startScanAndStoreFingerprints(ScanManager.SCANNING_TIME).subscribe(() -> {
            Log.d("ScanService", "scan completed");
            stopSelf();
        });
    }
}
