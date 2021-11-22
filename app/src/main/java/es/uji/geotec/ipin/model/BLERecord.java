package es.uji.geotec.ipin.model;

import android.bluetooth.le.ScanResult;

import java.util.Date;
import java.util.Objects;

import es.uji.geotec.ipin.Utils;

public class BLERecord {

    private int rssi;
    private long timestamp;
    private BLEBeacon beacon;

    public BLERecord() {
        super();
        this.rssi = 100;
        this.timestamp = new Date().getTime();
        this.beacon = null;
    }

    public BLERecord(ScanResult result) {
        this.rssi = result.getRssi();
        this.timestamp = Utils.elapsedNanosToTimestamp(result.getTimestampNanos());
        this.beacon = new BLEBeacon(result);
    }

    public BLERecord(BLERecord record) {
        this.rssi = record.rssi;
        this.timestamp = record.timestamp;
        this.beacon = record.beacon;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public BLEBeacon getBeacon() {
        return beacon;
    }

    public void setBeacon(BLEBeacon beacon) {
        this.beacon = beacon;
    }

    @Override
    public String toString() {
        return "BLERecord{" +
                "rssi=" + rssi +
                ", timestamp=" + timestamp +
                ", beacon=" + beacon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BLERecord bleRecord = (BLERecord) o;
        return rssi == bleRecord.rssi &&
                timestamp == bleRecord.timestamp &&
                Objects.equals(beacon, bleRecord.beacon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rssi, timestamp, beacon);
    }
}
