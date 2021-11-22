package es.uji.geotec.ipin.room;

import android.content.Context;
import android.os.Environment;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import es.uji.geotec.ipin.model.BLEFingerprint;

public class FingerprintsLocalDatabase {

    private static final String DB_NAME = "ble-fingerprints";

    private static FingerprintsLocalDatabase instance;

    private Context context;
    private BLEFingerprintDAO bleFingerprintDAO;

    private FingerprintsLocalDatabase(Context context) {
        this.context = context;
        LocalDatabase db = Room.databaseBuilder(context, LocalDatabase.class, DB_NAME).build();
        bleFingerprintDAO = db.bleFingerprintDAO();
    }

    public static FingerprintsLocalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new FingerprintsLocalDatabase(context);
        }

        return instance;
    }

    public void saveFingerprint(BLEFingerprint fingerprint) {
        bleFingerprintDAO.insertFingerprint(fingerprint);
    }

    public LiveData<List<BLEFingerprint>> getStoredFingerprints() {
        return bleFingerprintDAO.getLiveFingerprints();
    }

    public File exportDB() {
        List<BLEFingerprint> fingerprints = bleFingerprintDAO.getFingerprints();

        File exportDir = new File(context.getFilesDir(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File jsonFile = new File(exportDir, "exports_" + new Date().getTime() + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(jsonFile)){
            jsonFile.createNewFile();
            gson.toJson(fingerprints, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonFile;
    }

    public void clearDB() {
        bleFingerprintDAO.clear();
    }
}
