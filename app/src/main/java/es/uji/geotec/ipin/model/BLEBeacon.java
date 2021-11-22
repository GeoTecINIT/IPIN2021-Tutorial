package es.uji.geotec.ipin.model;

import android.bluetooth.le.ScanResult;

import java.util.Objects;

import es.uji.geotec.ipin.Utils;

public class BLEBeacon {

    private static final int MIN_LENGHT_RECORD = 28;
    private static final int[] UUID_COMPONENTS_INDEX = new int[]{9, 10, 11, 12,
            13, 14, 15, 16,
            17, 18, 19, 20,
            21, 22, 23, 24};
    private static final int[] UUID_DASHES_INDEX = new int[]{8, 13, 18, 23};

    private int major;
    private int minor;

    public BLEBeacon() {
        this.major = -1;
        this.minor = -1;
    }

    public BLEBeacon(int major, int minor) {
        this.major = major;
        this.minor = minor;
    }

    public BLEBeacon(ScanResult scanResult) {
        byte[] scanRecord = scanResult.getScanRecord().getBytes();

        this.major = extractMajor(scanRecord);
        this.minor = extractMinor(scanRecord);
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    private String extractUUID(byte[] scanRecord) {
        if (isValidScanRecord(scanRecord)){
            String uuid = extractUUIDComponents(scanRecord);

            return formatUUID(uuid);
        }
        return "";
    }

    private String extractUUIDComponents(byte[] scanRecord) {
        StringBuilder sb = new StringBuilder();
        for (int index : UUID_COMPONENTS_INDEX) {
            String component = Utils.IntToHex2(scanRecord[index] & 0xff);
            sb.append(component);
        }

        return sb.toString();
    }

    private String formatUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        for (int index : UUID_DASHES_INDEX) {
            sb.insert(index, "-");
        }

        return sb.toString();
    }

    private int extractMajor(byte[] scanRecord) {
        if (isValidScanRecord(scanRecord)) {
            return (scanRecord[25] & 0xff) * 0x100 + (scanRecord[26] & 0xff);
        }
        return -1;
    }

    private int extractMinor(byte[] scanRecord) {
        if (isValidScanRecord(scanRecord)) {
            return (scanRecord[27] & 0xff) * 0x100 + (scanRecord[28] & 0xff);
        }
        return -1;
    }

    private boolean isValidScanRecord(byte[] scanRecord) {
        return scanRecord.length > MIN_LENGHT_RECORD;
    }

    @Override
    public String toString() {
        return "BLEBeacon{" +
                "major=" + major +
                ", minor=" + minor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BLEBeacon bleBeacon = (BLEBeacon) o;
        return major == bleBeacon.major &&
                minor == bleBeacon.minor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor);
    }
}
