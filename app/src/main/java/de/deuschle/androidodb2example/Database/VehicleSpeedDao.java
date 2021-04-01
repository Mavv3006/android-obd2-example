package de.deuschle.androidodb2example.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import de.deuschle.androidodb2example.Session.SessionData;

@Dao
public interface VehicleSpeedDao {
    @Query("SELECT * FROM VehicleSpeedEntity WHERE sessionId = :sessionId")
    VehicleSpeedEntity getVehicleSpeedById(int sessionId);

    @Insert(entity = VehicleSpeedEntity.class)
    void insert(SessionData vehicleSpeedEntities);

    @Delete
    void delete(VehicleSpeedEntity sessionId);
}
