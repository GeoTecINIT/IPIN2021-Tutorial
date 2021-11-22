package es.uji.geotec.ipin.model;

import android.os.Build;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;
import java.util.Objects;

import es.uji.geotec.ipin.Utils;
import es.uji.geotec.ipin.room.BLERecordListConverter;

@Entity
public class BLEFingerprint {

    private static String OS = "android";

    @PrimaryKey
    @NonNull
    private String uuid;

    private long timestamp;
    private String osInfo;
    private String userId;

    @TypeConverters(BLERecordListConverter.class)
    private List<BLERecord> fingerprint;

    public BLEFingerprint() {
        super();
    }

    public BLEFingerprint(String userId, List<BLERecord> fingerprint) {
        this.uuid = Utils.generateRandomUUID();
        this.timestamp = Utils.elapsedNanosToTimestamp(SystemClock.elapsedRealtimeNanos());
        this.osInfo = buildOsInfo();
        this.userId = userId;
        this.fingerprint = fingerprint;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOsInfo() {
        return this.osInfo;
    }

    public void setOsInfo(String osInfo) {
        this.osInfo = osInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<BLERecord> getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(List<BLERecord> fingerprint) {
        this.fingerprint = fingerprint;
    }

    private String buildOsInfo() {
        String phoneBrand = Build.BRAND;
        String phoneModel = Build.MODEL;
        String apiLevel = String.valueOf(Build.VERSION.SDK_INT);

        return OS + "_" + apiLevel + "_" + phoneBrand + "_" + phoneModel;
    }

    @Override
    public String toString() {
        return "BLEFingerprint{" +
                "uuid='" + uuid + '\'' +
                ", timestamp=" + timestamp +
                ", osInfo='" + osInfo + '\'' +
                ", userId='" + userId + '\'' +
                ", fingerprint=" + fingerprint +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BLEFingerprint that = (BLEFingerprint) o;
        return timestamp == that.timestamp &&
                uuid.equals(that.uuid) &&
                Objects.equals(osInfo, that.osInfo) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(fingerprint, that.fingerprint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, timestamp, osInfo, userId, fingerprint);
    }
}
