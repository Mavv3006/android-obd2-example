package de.deuschle.androidodb2example.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import de.deuschle.androidodb2example.Session.SessionData;

@Dao
public interface AmbientTemperatureDao {
    @Query("SELECT * FROM AmbientTemperatureEntity WHERE sessionId = :sessionId")
    AmbientTemperatureEntity getAmbientTemperatureById(int sessionId);

    @Insert(entity = AmbientTemperatureEntity.class)
    void insert(SessionData ambientTemperatureEntities);

    @Delete
    void delete(AmbientTemperatureEntity sessionId);
}
