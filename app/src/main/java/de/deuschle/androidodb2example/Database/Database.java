package de.deuschle.androidodb2example.Database;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {SessionEntity.class, AmbientTemperatureEntity.class, RPMEntity.class, VehicleSpeedEntity.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract SessionDao getSessionDao();

    public abstract AmbientTemperatureDao getAmbientTemperatureDao();

    public abstract RPMDao getRPMDao();

    public abstract VehicleSpeedDao getVehicleSpeedDao();
}
