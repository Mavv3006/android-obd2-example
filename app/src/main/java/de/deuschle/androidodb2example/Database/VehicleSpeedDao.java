package de.deuschle.androidodb2example.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface VehicleSpeedDao {
    @Query("SELECT * FROM VehicleSpeedEntity WHERE sessionId = :sessionId")
    VehicleSpeedEntity getVehicleSpeedById(int sessionId);

    @Insert
    void insert(VehicleSpeedEntity... vehicleSpeedEntities);

    @Delete
    void delete(VehicleSpeedEntity sessionId);
}
