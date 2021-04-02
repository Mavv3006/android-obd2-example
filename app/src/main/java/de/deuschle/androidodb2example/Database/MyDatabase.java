package de.deuschle.androidodb2example.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.deuschle.androidodb2example.Util.LocalDateTimeConverter;

@Database(entities = {SessionEntity.class, AmbientTemperatureEntity.class, RPMEntity.class, VehicleSpeedEntity.class}, version = 1, exportSchema = false)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract SessionDao getSessionDao();

    public abstract AmbientTemperatureDao getAmbientTemperatureDao();

    public abstract RPMDao getRPMDao();

    public abstract VehicleSpeedDao getVehicleSpeedDao();
}
