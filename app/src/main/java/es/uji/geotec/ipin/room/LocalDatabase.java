package es.uji.geotec.ipin.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import es.uji.geotec.ipin.model.BLEFingerprint;

@Database(entities = {BLEFingerprint.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract BLEFingerprintDAO bleFingerprintDAO();
}
