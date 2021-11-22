package es.uji.geotec.ipin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.LinkedList;
import java.util.List;

public class PermissionManager {

    public static final int LOCATION_REQUEST_CODE = 53;
    public static final int BG_LOCATION_REQUEST_CODE = 54;

    private static String[] permissions = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static String additionalPermission = Manifest.permission.ACCESS_BACKGROUND_LOCATION;

    private Activity activity;
    private boolean additionalPermissionRequested = false;

    public PermissionManager(Activity activity) {
        this.activity = activity;
    }

    public boolean needToRequestPermissions() {
        for (String permission : permissions) {
            boolean granted = ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            if (!granted)
                return true;
        }

        return false;
    }

    public void requestPermissions() {
        List<String> toBeRequested = new LinkedList<>();

        for (String permission : permissions) {
            boolean granted = ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
            if (!granted)
                toBeRequested.add(permission);
        }

        if (toBeRequested.size() == 0)
            return;

        boolean needEducationalUI = false;
        for (String permission : toBeRequested) {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                needEducationalUI = true;
                break;
            }
        }

        if (needEducationalUI) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(R.string.alert_location_request_title);
            builder.setMessage(R.string.alert_location_request_body);
            builder.setPositiveButton(R.string.alert_ok, (dialog, which) -> activity.requestPermissions(toArray(toBeRequested), LOCATION_REQUEST_CODE));
            builder.show();
        } else {
            activity.requestPermissions(toArray(toBeRequested), LOCATION_REQUEST_CODE);
        }
    }

    // Can only be requested one time!!
    public boolean needAdditionalPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !additionalPermissionRequested;
    }

    public void requestAdditionalPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(R.string.alert_bg_location_request_title);
                builder.setMessage(R.string.alert_bg_location_request_body);
                builder.setPositiveButton(R.string.alert_ok, (dialog, which) -> activity.requestPermissions(new String[]{ Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BG_LOCATION_REQUEST_CODE));
                builder.show();
            } else {
                activity.requestPermissions(new String[]{ Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BG_LOCATION_REQUEST_CODE);
            }
        }

        additionalPermissionRequested = true;
    }

    private String[] toArray(List<String> list) {
        String[] array = new String[list.size()];
        return list.toArray(array);
    }
}