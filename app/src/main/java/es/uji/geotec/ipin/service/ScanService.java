package es.uji.geotec.ipin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int flag = super.onStartCommand(intent, flags, startId);

        // TODO: set service in foreground mode with an explaining notification
        // Steps:
        //      - Create a new NotificationProvider instance:
        //          - Provides a notification --> NotificationProvider#getForegroundNotification()
        //      - Call startForeground(...). Parameters:
        //          - id: notification id --> NotificationProvider.NOTIFICATION_ID_FOREGROUND
        //          - notification: the notification from NotificationProvider

        startScan();

        return flag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO: remove service from foreground mode and remove notification using stopForeground(...)
        //
        // Android DOCS: https://developer.android.com/reference/android/app/Service#stopForeground(int)
    }

    private void startScan() {
        ScanManager scanManager = new ScanManager(this, "a9c04048-a71e-42b5-b569-13b5ac77b618");
        scanManager.startScanAndStoreFingerprints(ScanManager.SCANNING_TIME).subscribe(() -> {
            Log.d("ScanService", "scan completed");
            stopSelf();
        });
    }
}