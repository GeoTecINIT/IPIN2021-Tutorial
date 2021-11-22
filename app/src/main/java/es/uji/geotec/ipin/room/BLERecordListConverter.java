package es.uji.geotec.ipin.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import es.uji.geotec.ipin.model.BLERecord;

public class BLERecordListConverter {

    private static Gson gson = new GsonBuilder().create();

    @TypeConverter
    public static String fromBLERecordList(List<BLERecord> records) {
        if (records == null)
            return null;

        return gson.toJson(records);
    }

    @TypeConverter
    public static List<BLERecord> toBLERecordList(String records) {
        if (records == null)
            return null;

        Type type = new TypeToken<List<BLERecord>>() {}.getType();
        return gson.fromJson(records, type);
    }
}