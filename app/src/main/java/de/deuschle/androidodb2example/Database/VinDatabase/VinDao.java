package de.deuschle.androidodb2example.Database.VinDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VinDao {
    @Query("SELECT COUNT(*) AS bool FROM vin WHERE vin = :vin")
    int isVinInDb(String vin);

    @Insert
    void insert(VinEntity entity);

    @Query("SELECT * FROM vin")
    LiveData<List<VinEntity>> getAll();
}
