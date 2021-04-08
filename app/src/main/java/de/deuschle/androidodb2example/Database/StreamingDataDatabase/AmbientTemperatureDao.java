package de.deuschle.androidodb2example.Database.StreamingDataDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.deuschle.androidodb2example.Session.SessionData;

@Dao
public interface AmbientTemperatureDao {
    @Query("SELECT * FROM AmbientTemperatureEntity WHERE sessionId = :sessionId")
    AmbientTemperatureEntity getAmbientTemperatureById(int sessionId);

    @Query("SELECT * FROM AmbientTemperatureEntity")
    LiveData<List<AmbientTemperatureEntity>> getAll();

    @Insert(entity = AmbientTemperatureEntity.class)
    void insert(SessionData ambientTemperatureEntities);

    @Delete
    void delete(AmbientTemperatureEntity sessionId);
}
