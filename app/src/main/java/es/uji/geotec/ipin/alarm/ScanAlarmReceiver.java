package es.uji.geotec.ipin.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScanAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: Start a BLE scan using the ScanManager
        // Steps:
        //      - Create a new ScanManager instance using context and a Beacon UUID filter:
        //          - UUID filter: a9c04048-a71e-42b5-b569-13b5ac77b618
        //      - Start a scan using ScanManager#startScanAndStoreFingerprints(scanTime):
        //          - Scan time --> ScanManager.SCANNING_TIME (10 seconds)
        //      - Subscribe to previous call to know when the scan ends:
        //          - (...).subscribe(() -> Log.d("ScanAlarmReceiver", "scan ended!"))
    }
}