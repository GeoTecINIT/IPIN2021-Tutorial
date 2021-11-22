package es.uji.geotec.ipin.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.uji.geotec.ipin.model.BLEFingerprint;

@Dao
public interface BLEFingerprintDAO {

    @Insert
    public void insertFingerprint(BLEFingerprint fingerprint);

    @Query("SELECT * FROM BLEFingerprint")
    public LiveData<List<BLEFingerprint>> getLiveFingerprints();

    @Query("SELECT * FROM BLEFingerprint")
    public List<BLEFingerprint> getFingerprints();

    @Query("DELETE FROM BLEFingerprint")
    public void clear();
}
