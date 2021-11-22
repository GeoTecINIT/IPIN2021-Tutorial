package es.uji.geotec.ipin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

public class PowerSavingsManager {
    public static int REQUEST_CODE = 32;

    private Activity activity;
    private PowerManager powerManager;

    public PowerSavingsManager(Activity activity) {
        this.activity = activity;
        this.powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
    }

    public boolean areDisabled() {
        return powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
    }

    public void requestDeactivation() {
        if (this.areDisabled()) {
            return;
        }

        showEducationalMessage();
    }

    private void showEducationalMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.alert_battery_title);
        builder.setMessage(R.string.alert_battery_request_body);
        builder.setPositiveButton(R.string.alert_ok, (dialog, which) -> {
            Intent powerIntent = new Intent(
                    Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    Uri.parse("package:" + activity.getPackageName())
            );
            activity.startActivityForResult(powerIntent, REQUEST_CODE);
        });
        builder.show();
    }
}
