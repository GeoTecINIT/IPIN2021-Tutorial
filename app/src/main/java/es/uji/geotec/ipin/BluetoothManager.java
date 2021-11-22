package es.uji.geotec.ipin;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

public class BluetoothManager {

    public static int REQUEST_CODE = 2;

    private Context context;
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothManager(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public BluetoothAdapter getAdapter() {
        return this.bluetoothAdapter;
    }

    public boolean isEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void requestEnable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.alert_bluetooth_request_title);
        builder.setMessage(R.string.alert_bluetooth_request_body);
        builder.setPositiveButton(R.string.alert_ok, (dialog, which) -> {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableIntent, REQUEST_CODE);
        });
        builder.show();
    }
}
