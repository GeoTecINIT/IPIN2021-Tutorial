package es.uji.geotec.ipin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

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

        startScan();

        return flag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startScan() {
        // TODO: Start scan using ScanManager#startScanAndStoreFingerprints(scanningTime) and destroy service when scan is completed
        // Steps:
        //      - Create a new ScanManager instance using context (this) and a Beacon UUID filter:
        //          - UUID filter: a9c04048-a71e-42b5-b569-13b5ac77b618
        //      - Start a scan using ScanManager#startScanAndStoreFingerprints(scanTime):
        //          - Scan time --> ScanManager.SCANNING_TIME (10 seconds)
        //      - Subscribe to previous call to know when the scan ends:
        //          - (...).subscribe(() -> Log.d("ScanAlarmReceiver", "scan ended!"))
        //      - To stop service call to stopSelf()
    }
}
