package es.uji.geotec.ipin.scan;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.uji.geotec.ipin.BluetoothManager;
import es.uji.geotec.ipin.Utils;
import es.uji.geotec.ipin.model.BLEFingerprint;
import es.uji.geotec.ipin.model.BLERecord;
import es.uji.geotec.ipin.room.FingerprintsLocalDatabase;
import io.reactivex.rxjava3.core.Completable;

public class ScanManager {

    public static final int SCANNING_TIME = 10000;

    private static String TAG = "ScanManager";

    private Context context;
    private BluetoothManager bluetoothManager;
    private BLECollector bleCollector;
    private BLEScan bleScan;

    private boolean running = false;

    public ScanManager(Context context, String uuid) {
        this.context = context;
        this.bluetoothManager = new BluetoothManager(context);
        this.bleCollector = new BLECollector();
        this.bleScan = new BLEScan(
                this.bluetoothManager.getAdapter(),
                this.bleCollector,
                Utils.HexToSignedInt(uuid)
        );
    }

    public Completable startScanAndStoreFingerprints(int scanningTime) {
        startBLEScan();

        return Completable.create(emitter -> {
            Timer scanTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    stopBLEScan();

                    List<BLERecord> bestRecords = getBestRecordForBeacon();
                    BLEFingerprint fingerprint = new BLEFingerprint("1234", bestRecords);

                    FingerprintsLocalDatabase.getInstance(context).saveFingerprint(fingerprint);

                    emitter.onComplete();
                }
            };

            scanTimer.schedule(timerTask, scanningTime);
        });
    }

    private void startBLEScan() {
        if (running) {
            return;
        }

        if (!bluetoothManager.isEnabled()) {
            return;
        }

        bleScan.startScan();
        running = true;
    }

    private void stopBLEScan() {
        if (!running) {
            return;
        }

        bleScan.stopScan();
        running = false;
    }

    private List<BLERecord> getBestRecordForBeacon() {
        List<BLERecord> bestRecords = bleCollector.getBestBLERecordForBeacon();
        bleCollector.clearRecords();

        return bestRecords;
    }
}
