package es.uji.geotec.ipin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.uji.geotec.ipin.model.BLEFingerprint;
import es.uji.geotec.ipin.model.BLERecord;
import es.uji.geotec.ipin.room.FingerprintsLocalDatabase;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private PermissionManager permissionManager;
    private PowerSavingsManager powerManager;
    private BluetoothManager bluetoothManager;

    private ScrollView scrollView;
    private TextView fingerprintsTextView;
    private Button clearFingerprintsButton;
    private Button exportFingerprintsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager(this);
        powerManager = new PowerSavingsManager(this);
        bluetoothManager = new BluetoothManager(this);

        scrollView = findViewById(R.id.fp_scrollview);
        fingerprintsTextView = findViewById(R.id.fingerprints);
        clearFingerprintsButton = findViewById(R.id.clear_fp);
        clearFingerprintsButton.setOnClickListener(view -> {
            Observable.just(FingerprintsLocalDatabase.getInstance(this))
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(fingerprintsLocalDatabase -> fingerprintsLocalDatabase.clearDB());
        });
        exportFingerprintsButton = findViewById(R.id.export_fp);
        exportFingerprintsButton.setOnClickListener(view -> {
            Observable.just(FingerprintsLocalDatabase.getInstance(this))
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(fingerprintsLocalDatabase -> {
                        File exportFile = fingerprintsLocalDatabase.exportDB();

                        ArrayList<Uri> uris = new ArrayList<>();
                        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", exportFile);
                        uris.add(uri);

                        Intent chooserIntent = new Intent();
                        chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        chooserIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        chooserIntent.setType("message/rfc822");
                        chooserIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

                        startActivity(Intent.createChooser(chooserIntent, "Share file"));
                    });
        });

        startSetup();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //startSetup();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionManager.LOCATION_REQUEST_CODE) {
            boolean granted = allGranted(grantResults);

            if (granted) {
                if(permissionManager.needAdditionalPermission()) {
                    permissionManager.requestAdditionalPermissions();
                } else {
                    checkAndRequestBatteryOptimizations();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_permissions_denied_title);
                builder.setMessage(R.string.alert_permissions_denied_body);
                builder.setPositiveButton(R.string.alert_close, (dialog, which) -> System.exit(0));
                builder.show();
            }
        } else if (requestCode == PermissionManager.BG_LOCATION_REQUEST_CODE) {
            boolean granted = allGranted(grantResults);

            if (!granted) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_permissions_denied_title);
                builder.setMessage(R.string.alert_bg_permissions_denied_body);
                builder.setPositiveButton(R.string.alert_close, (dialog, which) -> checkAndRequestBatteryOptimizations());
                builder.show();
                return;
            }
            checkAndRequestBatteryOptimizations();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PowerSavingsManager.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkAndEnableBluetooth();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_battery_title);
                builder.setMessage(R.string.alert_battery_denied_body);
                builder.setPositiveButton(R.string.alert_close, (dialog, which) -> {
                    checkAndEnableBluetooth();
                });
                builder.show();
            }
        } else if (requestCode == BluetoothManager.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setupFinished();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_bluetooth_denied_title);
                builder.setMessage(R.string.alert_bluetooth_denied_body);
                builder.setPositiveButton(R.string.alert_close, (dialog, which) -> System.exit(0));
                builder.show();
            }
        }
    }

    private void startSetup() {
        // Setup flow:
        //      Check/Request permissions
        //      Check/Request battery optimizations
        //      Check/Enable Bluetooth

        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        boolean needed = permissionManager.needToRequestPermissions();

        if (needed) {
            permissionManager.requestPermissions();
        } else {
            checkAndRequestBatteryOptimizations();
        }
    }

    private void checkAndRequestBatteryOptimizations() {
        boolean disabled = powerManager.areDisabled();

        if (!disabled) {
            powerManager.requestDeactivation();
        } else {
            checkAndEnableBluetooth();
        }
    }

    private void checkAndEnableBluetooth() {
        boolean isEnabled = bluetoothManager.isEnabled();

        if (!isEnabled) {
            bluetoothManager.requestEnable();
        } else {
            setupFinished();
        }
    }

    private boolean allGranted(int[] grantResults) {
        boolean granted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                granted = false;
            }
        }

        return granted;
    }

    private void setupFinished() {
        FingerprintsLocalDatabase.getInstance(this).getStoredFingerprints()
                .observeForever(fingerprint -> {
                    fingerprintsTextView.setText(stringifyFingerprintList(fingerprint));
                    scrollView.postDelayed(() -> scrollView.fullScroll(View.FOCUS_DOWN), 200);
                });
    }

    private String stringifyFingerprintList(List<BLEFingerprint> fingerprints) {
        StringBuilder sb = new StringBuilder();

        for (BLEFingerprint fp : fingerprints) {
            sb.append(String.format("[%s - %s]:\n", new Date(fp.getTimestamp()).toString(), fp.getOsInfo()));
            for (BLERecord record : fp.getFingerprint()) {
                sb.append(String.format("\t[MAJOR=%d, MINOR=%d, RSSI=%d]\n", record.getBeacon().getMajor(), record.getBeacon().getMinor(), record.getRssi()));
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}