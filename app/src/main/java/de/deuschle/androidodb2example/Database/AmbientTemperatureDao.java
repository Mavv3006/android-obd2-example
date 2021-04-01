package de.deuschle.androidodb2example.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AmbientTemperatureDao {
    @Query("SELECT * FROM AmbientTemperatureEntity WHERE sessionId = :sessionId")
    AmbientTemperatureEntity getAmbientTemperatureById(int sessionId);

    @Insert
    void insert(AmbientTemperatureEntity... ambientTemperatureEntities);

    @Delete
    void delete(AmbientTemperatureEntity sessionId);
}
