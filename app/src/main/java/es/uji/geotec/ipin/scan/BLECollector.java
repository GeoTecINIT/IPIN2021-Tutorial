package es.uji.geotec.ipin.scan;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import es.uji.geotec.ipin.model.BLEBeacon;
import es.uji.geotec.ipin.model.BLERecord;
import es.uji.geotec.ipin.model.RSSIComparator;

public class BLECollector {

    private HashMap<BLEBeacon, List<BLERecord>> results;

    public BLECollector() {
        this.results = new HashMap<>();
    }

    public void receiveBLERecord(BLERecord bleRecord) {
        BLEBeacon beacon = bleRecord.getBeacon();

        if (!results.containsKey(beacon))
            results.put(beacon, new LinkedList<>());

        results.get(beacon).add(bleRecord);
    }

    public List<BLERecord> getBestBLERecordForBeacon() {
        List<BLERecord> bestRecords = new LinkedList<>();

        if (results.isEmpty())
            return bestRecords;

        for (List<BLERecord> recordsForBeacon : results.values()) {
            bestRecords.add(getMedianRecord(recordsForBeacon));
        }

        return bestRecords;
    }

    public List<BLERecord> getRecordsForBeacon(BLEBeacon beacon) {
        List<BLERecord> records = results.get(beacon);
        return records != null ? records : new LinkedList<>();
    }

    public void clearRecords() {
        results.clear();
    }

    private BLERecord getMedianRecord(List<BLERecord> records) {
        Collections.sort(records, new RSSIComparator());

        int size = records.size();
        int medianIndex =  size / 2;

        BLERecord bestRecord = records.get(medianIndex);

        if (size % 2 == 0) {
            bestRecord = interpolateMedianRecords(records.get(medianIndex - 1), records.get(medianIndex));
        }

        return bestRecord;
    }

    private BLERecord interpolateMedianRecords(BLERecord recordA, BLERecord recordB) {
        int interpolatedRssi = (recordA.getRssi() + recordB.getRssi()) / 2;

        BLERecord interpolatedRecord = new BLERecord(recordA);
        interpolatedRecord.setRssi(interpolatedRssi);

        return interpolatedRecord;
    }
}
