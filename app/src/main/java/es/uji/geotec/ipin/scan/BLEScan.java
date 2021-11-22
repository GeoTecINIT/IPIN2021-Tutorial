package es.uji.geotec.ipin.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.uji.geotec.ipin.model.BLERecord;

public class BLEScan {

    private static String TAG = "BLEScan";

    private BLECollector collector;
    private BluetoothLeScanner bleScanner;
    private BluetoothAdapter bleAdapter;
    private ScanSettings scanSettings;
    private List<ScanFilter> scanFilters;
    private ScanCallback scanCallback;

    private byte[] uuid;

    public BLEScan(BluetoothAdapter adapter, BLECollector collector, byte[] uuid) {
        this.bleAdapter = adapter;
        this.collector = collector;
        this.uuid = uuid;

        defaultConfiguration();
    }

    public void startScan() {
        this.bleScanner = bleAdapter.getBluetoothLeScanner();
        this.bleScanner.startScan(
                scanFilters,
                scanSettings,
                scanCallback
        );
        Log.d(TAG, "startScan --> started!");
    }

    public void stopScan() {
        bleScanner.flushPendingScanResults(scanCallback);
        bleScanner.stopScan(scanCallback);
        bleScanner.flushPendingScanResults(scanCallback);
        Log.d(TAG, "stopScan --> scan stopped!");
    }

    private void defaultConfiguration() {
        setScanSettings();
        setScanFilter();
        setScanCallback();
    }

    private void setScanSettings() {
        this.scanSettings = new ScanSettings.Builder()
                .setReportDelay(0)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
    }

    private void setScanFilter() {
        this.scanFilters = new ArrayList<>();

        byte[] manufacturerData = new byte[] {
                0,0,
                uuid[0], uuid[1], uuid[2], uuid[3],
                uuid[4], uuid[5],
                uuid[6], uuid[7],
                uuid[8], uuid[9],
                uuid[10], uuid[11], uuid[12], uuid[13], uuid[14], uuid[15],
                0,0,
                0,0,
                0
        };

        byte[] manufacturerMask = new byte[] {
                0,0,
                1,1,1,1,
                1,1,
                1,1,
                1,1,
                1,1,1,1,1,1,
                0,0,
                0,0,
                0
        };

        scanFilters.add(new ScanFilter.Builder()
                .setManufacturerData(76, manufacturerData, manufacturerMask)
                .build());
    }

    private void setScanCallback() {
        this.scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BLERecord bleRecord = new BLERecord(result);
                collector.receiveBLERecord(bleRecord);
            }
        };
    }
}
