package es.uji.geotec.ipin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

public class NotificationProvider {

    public static int NOTIFICATION_ID_FOREGROUND = 63;

    private static String CHANNEL_ID_FOREGROUND = "IPIN2021_TUTORIAL";
    private static String CHANNEL_NAME_FOREGORUND = "IPIN2021 Tutorial foreground service";

    private Context context;
    private NotificationManagerCompat notificationManager;

    public NotificationProvider(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
    }

    public Notification getForegroundNotification() {
        setupNotificationChannelIfNeeded(CHANNEL_ID_FOREGROUND, CHANNEL_NAME_FOREGORUND);

        Notification.Builder notification = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.foreground_notification_title))
                .setContentText(context.getString(R.string.foreground_notification_text))
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notification.setChannelId(CHANNEL_ID_FOREGROUND);

        return notification.build();
    }

    private void setupNotificationChannelIfNeeded(String id, String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    id,
                    name,
                    NotificationManager.IMPORTANCE_LOW
            );

            notificationManager.createNotificationChannel(channel);
        }
    }
}
