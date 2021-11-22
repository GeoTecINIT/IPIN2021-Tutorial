package es.uji.geotec.ipin;

import android.os.SystemClock;

import java.util.UUID;

public class Utils {

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static String IntToHex2(int i) {
        char hex_2[] = {Character.forDigit((i >> 4) & 0x0f, 16), Character.forDigit(i & 0x0f, 16)};
        String hex_2_str = new String(hex_2);
        return hex_2_str.toUpperCase();
    }

    public static byte[] HexToSignedInt(String uuid) {
        int hexStep = 2;
        uuid = uuid.replace("-", "");

        byte[] uuid_bytes = new byte[uuid.length() / hexStep];

        for (int i = 0; i < uuid.length(); i += hexStep) {
            String part = uuid.substring(i, i + hexStep);
            uuid_bytes[i / hexStep] = (byte) Integer.parseInt(part,16);
        }

        return uuid_bytes;
    }

    public static long elapsedNanosToTimestamp(long elapsedNanos) {
        long nanosSinceBoot = SystemClock.elapsedRealtimeNanos();
        long now = System.currentTimeMillis();
        long diff = nanosSinceBoot - elapsedNanos;
        long elapsedMillis = (long)(diff / 1e6);
        return now - elapsedMillis;
    }
}
