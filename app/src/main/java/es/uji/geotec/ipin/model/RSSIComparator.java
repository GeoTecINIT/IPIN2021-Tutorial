package es.uji.geotec.ipin.model;

import java.util.Comparator;

public class RSSIComparator implements Comparator<BLERecord> {
    @Override
    public int compare(BLERecord o1, BLERecord o2) {
        return Math.abs(o1.getRssi()) - Math.abs(o2.getRssi());
    }
}